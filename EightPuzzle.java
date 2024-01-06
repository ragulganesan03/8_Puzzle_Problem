import java.util.*;

public class EightPuzzle {
    public static void main(String[] args) {
        int[][] initial = new int[3][3];
        int[][] goal = new int[3][3];
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the initial state of the 8 puzzle (use numbers 0-8, 0 represents the empty space):");

        // Taking user input for initial state
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                initial[i][j] = scanner.nextInt();
            }
        }

        System.out.println("Enter the goal state of the 8 puzzle (use numbers 0-8, 0 represents the empty space):");

        // Taking user input for goal state
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                goal[i][j] = scanner.nextInt();
            }
        }

        System.out.println("Entered initial puzzle:");
        printPuzzle(initial);
        System.out.println("Entered goal puzzle:");
        printPuzzle(goal);
        System.out.println("Solving...");

        // Solve the puzzle using A* algorithm
        Node solution = solvePuzzle(initial, goal);

        if (solution != null) {
            System.out.println("Solution found!");
            System.out.println("Steps to reach the solution:");
            printSolutionPath(solution);
        } else {
            System.out.println("No solution found!");
        }
        scanner.close();
    }

    // Function to print the puzzle
    public static void printPuzzle(int[][] puzzle) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                System.out.print(puzzle[i][j] + " ");
            }
            System.out.println();
        }
    }

    // Node class to represent puzzle states
    static class Node {
        int[][] puzzle;
        int cost; // Total cost (f = g + h)
        int level; // Level of the node in the search tree
        String move; // Movement made to reach this state
        Node parent; // Parent node

        Node(int[][] puzzle, int cost, int level, String move, Node parent) {
            this.puzzle = puzzle;
            this.cost = cost;
            this.level = level;
            this.move = move;
            this.parent = parent;
        }
    }

    // Function to find empty space in the puzzle
    public static int[] findEmptySpace(int[][] puzzle) {
        int[] pos = new int[2];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (puzzle[i][j] == 0) {
                    pos[0] = i;
                    pos[1] = j;
                    return pos;
                }
            }
        }
        return pos;
    }

    // Function to calculate the heuristic cost
    public static int calculateCost(int[][] puzzle, int[][] goal) {
        int cost = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (puzzle[i][j] != goal[i][j] && puzzle[i][j] != 0) {
                    cost++;
                }
            }
        }
        return cost;
    }

    // Function to check if a move is valid
    public static boolean isValid(int x, int y) {
        return x >= 0 && x < 3 && y >= 0 && y < 3;
    }

    // Function to copy contents of one array to another
    public static void copyArray(int[][] src, int[][] dest) {
        for (int i = 0; i < 3; i++) {
            System.arraycopy(src[i], 0, dest[i], 0, 3);
        }
    }

    // Function to print the solution path
    public static void printSolutionPath(Node node) {
        if (node == null) {
            return;
        }
        printSolutionPath(node.parent);
        printPuzzle(node.puzzle);
        System.out.println("Move: " + node.move);
        System.out.println("Cost: " + node.cost);
        System.out.println();
    }

    // A* algorithm to solve the puzzle
    public static Node solvePuzzle(int[][] initial, int[][] goal) {
        // Create priority queue for A* search
        PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparingInt(a -> (a.cost + a.level)));
        Map<String, Boolean> visited = new HashMap<>();

        // Create initial node
        Node root = new Node(initial, 0, 0, "", null);
        root.cost = calculateCost(initial, goal);

        pq.add(root);

        while (!pq.isEmpty()) {
            Node min = pq.poll();

            if (min.cost == 0) {
                return min;
            }

            visited.put(Arrays.deepToString(min.puzzle), true);

            // Find position of empty space
            int[] pos = findEmptySpace(min.puzzle);

            // Generate all possible moves
            int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
            for (int[] dir : directions) {
                int x = pos[0] + dir[0];
                int y = pos[1] + dir[1];

                if (isValid(x, y)) {
                    int[][] newPuzzle = new int[3][3];
                    copyArray(min.puzzle, newPuzzle);

                    // Swap tiles
                    int temp = newPuzzle[pos[0]][pos[1]];
                    newPuzzle[pos[0]][pos[1]] = newPuzzle[x][y];
                    newPuzzle[x][y] = temp;

                    // Create a new node
                    Node newNode = new Node(newPuzzle, 0, min.level + 1, "", min);

                    // Calculate cost for the new node
                    newNode.cost = calculateCost(newPuzzle, goal);
                    newNode.move = "(" + x + "," + y + ")";

                    // Add to priority queue if not visited
                    if (!visited.containsKey(Arrays.deepToString(newPuzzle))) {
                        pq.add(newNode);
                    }
                }
            }
        }
        return null;
    }
}