package com.sun.mmedia;

/**
 * This interfcae provides a set of methods to handle
 * media related events; foregournd / background switch
 */
public interface MediaEventConsumer {

    /**
     * Called by event delivery when MIDlet controller (in AMS Isolate)
     * notifies MIDlet and its display that there is a change in its foreground status
     */
    public void handleMediaForegroundNotify();

    /**
     * Called by event delivery when MIDlet controller (in AMS Isolate)
     * notifies MIDlet and its display that there is a change in its foreground status
     */
    public void handleMediaBackgroundNotify();
}
