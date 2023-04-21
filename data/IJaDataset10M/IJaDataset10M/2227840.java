package net.sf.genethello.applet;

import java.awt.Component;
import java.beans.PropertyChangeListener;

/**
 * Interface for common message printing tasks.
 *
 * @author praz
 */
public interface IMessage {

    /**
     * Draws the board.
     *
     * @param b board
     */
    public void printBoard(int[] b);

    /**
     * Update message board of certain player.
     *
     * @param turn      color of player: Game.BLACK, Game.WHITE
     * @param message   message to update
     */
    public void updateMessage(int turn, String message);

    /**
     * Enables/disables pass button for certain player.
     *
     * @param turn      color of player: Game.BLACK, Game.WHITE
     * @param enable    if true then enable, or disable otherwise
     */
    public void setEnabledPassButton(int turn, boolean enable);

    /**
     * Prints formatted integer to dialog box.
     *
     * @param format    format
     * @param i         integer to print
     */
    public void printMessage(String format, int i);

    public Component getGlassPane();

    /**
     * Enables or disables event listener on game board.
     *
     * @param b true to enable, otherwise disable
     */
    public void enableBoardListener(boolean b);

    /**
     * Removes property change listener.
     * 
     * @param name      property name
     * @param listener  listener
     */
    public void removePropertyChangeListener(String name, PropertyChangeListener listener);

    /**
     * Gets called after the game finished.
     */
    public void gameFinished();
}
