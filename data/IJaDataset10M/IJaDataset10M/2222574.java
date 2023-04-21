package egs.games;

public abstract class Game {

    int[][] currentBoard;

    short width;

    short height;

    short game_version;

    public static final int VALID = 0;

    public static final int INVALID_MOVE = 1;

    public static final int INVALID_BOARD = 2;

    /**
     * Empty game constructor, sets default state in subclass
     */
    public Game() {
    }

    /**
     * Game constructor sets the internal values for the game representation
     *
     * @param board - The raw board data
     */
    public Game(int[][] board) {
        this.height = (short) board.length;
        this.width = (short) board[0].length;
        currentBoard = board;
    }

    /**
     * Verifies that the board state passed in is a valid transition from the 
     * current board state.  Board state is not updated in this function.
     * 
     * @param board - The new board state to verify
     * @param player - The player making the move
     * @return 0 if it is a valid transition, 1 for illegal move, 2 for invalid board, 
     * 			128+ for game-specific error code otherwise.
     */
    public abstract int verifyMove(int[][] board, int player);

    /**
     * Performs the necessary steps to update the internal board state.
     * If false is returned, the board state is not updated.
     *
     * @param board - The new board state
     * @return 0 if the state update was a succcess, 2 for invalid board.
     */
    public abstract int updateState(int[][] board);

    /** 
     * Verifies that a move is valid and updates the state if so
     * If false is returned, the board state is not updated.
     *
     * @param board - The requested new board state
     * @param player - The player making the move
     * @return 0 if the move is a valid transition, 1 for illegal move, 2 for invalid board, 
     * 			128+ for game-specific error code otherwise.
     */
    public abstract int makeMove(int[][] board, int player);

    /**
     * A check to see if the game has completed
     * 
     *
     * @return A game specific value indicating if the game has completed
     */
    public abstract int getGameOver();

    /**
     * Return the raw board state
     * 
     * @return The raw board state
     */
    public int[][] getRawBoard() {
        return currentBoard;
    }

    /**
     * Returns a basic string representation of the board
     *
     * @return A basic string representation of the board
     */
    public String toString() {
        String ret = "";
        for (int h = 0; h < height; h++) {
            String line = "";
            for (int w = 0; w < width; w++) {
                line = line + "  " + currentBoard[h][w];
            }
            ret = ret + "\n" + line;
        }
        return ret;
    }

    /**
     * Getters for the internal values
     */
    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public short getGameVersion() {
        return game_version;
    }
}
