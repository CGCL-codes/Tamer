package teamx.tictactoe;

/**
 * The GUI will call the controller to check if a move is
 * allowed.
 * @author ralph
 */
public interface IController {

    /**
     *
     * @return 1 for player 1 and 2 for player 2
     */
    public int getCurrentPlayer();

    /**
     *
     * @param x
     * @param y
     * @return true if the move is allowed, otherwise false
     */
    public boolean isMoveAllowed(int x, int y);

    /**
     * Sets the new position
     * @param x
     * @param y
     */
    public void setMove(int x, int y);

    /**
     *
     * @return -1 if game is not over, 0 if game is a tie, otherwise the player id
     */
    public int gameOver();
}
