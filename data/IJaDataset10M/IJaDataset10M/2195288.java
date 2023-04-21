package net.sf.flophase.app.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Indicates that the class handles a "New" action.
 */
public interface HasNewActionListeners {

    /**
     * Add a listener to be notified of a "New" action.
     * 
     * @param listener The listener.
     */
    public void addNewActionListener(ActionListener listener);

    /**
     * Notifies all listeners that the "New" action occurred.
     * 
     * @param event The event
     */
    public void notifyNewActionListeners(ActionEvent event);

    /**
     * Removes a listener.
     * 
     * @param listener The listener.
     */
    public void removeNewActionListener(ActionListener listener);
}
