package net.suberic.pooka.gui;

import net.suberic.pooka.event.*;
import javax.swing.JProgressBar;

/**
 * This is a JProgressBar which is also a MessageLoadedListener.  It can be
 * used to show the count of messages as they are loaded (not surprisingly).
 */
public class LoadMessageTracker extends JProgressBar implements MessageLoadedListener {

    private int initialValue;

    public LoadMessageTracker(int newInitialValue) {
        super();
        initialValue = newInitialValue;
    }

    public LoadMessageTracker(int newInitialValue, int newMin, int newMax) {
        super(newMin, newMax);
        initialValue = newInitialValue;
    }

    /**
   * Defined in net.suberic.pooka.event.MessageLoadedListener
   */
    public void handleMessageLoaded(MessageLoadedEvent e) {
        if (e.getNumMessages() != this.getMaximum()) this.setMaximum(e.getNumMessages());
        setValue(this.getValue() + e.getLoadedMessageCount());
    }
}
