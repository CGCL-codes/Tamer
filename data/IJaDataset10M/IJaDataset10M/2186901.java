package net.sourceforge.jsweeper.model;

/**
 * @author neal
 */
public interface Model {

    /**
     * Starts a new game of minesweeper and notifies any observers of the
     * changes to the model
     */
    public void newGame();

    /**
     * Changes the number of rows columns and mines on the game board.
     * 
     * @param rows
     *            Number of rows of nines in the minefield
     * @param cols
     *            Number of columns of nines in the minefield
     * @param mines
     *            The Number of mines to distribute throughout the minefield
     */
    public void setGameSize(int rows, int cols, int mines);

    /**
     * Reveals the square at the specified location, if a mine is found the
     * gameover message is raised otherwise the square is revealed if the square
     * has no adjacencies then any other adjacent squares which are not mines
     * are also revealed. If the revealSquare method is invoked for a square
     * which has already been revealed then the message is ignored.
     * 
     * @param row
     *            y position of mine
     * @param col
     *            x position pf mine
     */
    public void revealSquare(int row, int col);

    /**
     * Marks the specified square with a flag. If the square is already flagged
     * then the flag is removed. Any observers of the modified square are
     * notified.
     * 
     * @param row
     *            y position of mine
     * @param col
     *            x position pf mine
     */
    public void flagSquare(int row, int col);

    /**
     * determines if the game has been lost.
     * 
     * @return return true if the game is lost
     */
    public boolean isGameOver();

    /**
     * determines if the game has been won
     * 
     * @return returns true if the game has been won
     */
    public boolean isWon();

    /**
     * @return returns true if the game has been started
     */
    public boolean isStarted();

    /**
     * Pauses and unpauses the game
     * 
     * @param paused set to true to pause the game
     */
    public void pause(boolean paused);
}
