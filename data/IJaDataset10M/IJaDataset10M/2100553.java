package net.sf.genethello.applet;

/**
 * Game interface shared by several modules.
 *
 * @author praz
 */
public interface IGame {

    /**
     * Gets board.
     *
     * @return  board
     */
    public IBoard getBoard();

    /**
     * Gets current color fo player to turn.
     *
     * @return  color to turn: Game.BLACK, Game.WHITE
     */
    public int getTurn();

    /**
     * Sets current color turn to play.
     *
     * @param turn  color to play: Game.BLACK, Game.WHITE
     */
    public void setTurn(int turn);

    /**
     * Prints current board position to standard output.
     */
    public void printBoard();
}
