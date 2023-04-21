package org.speakmon.babble.events;

import java.util.EventObject;

/**
 * This event models a response to the TIME command.
 * @version $Id: TimeEvent.java 239 2004-07-28 05:09:17Z speakmon $
 * @author Ben Speakmon
 */
public class TimeEvent extends EventObject {

    /**
     * Holds value of property time.
     */
    private String time;

    /**
     * Creates a new TimeEvent.
     * @param source the server's local time
     */
    public TimeEvent(Object source) {
        super(source);
        time = (String) source;
    }

    /**
     * Returns the server's local time.
     * @return the server's local time
     */
    public String getTime() {
        return this.time;
    }
}
