package org.speakmon.babble.events;

import java.util.EventObject;

/**
 * This event models a PING message from the server.
 * @version $Id: PingEvent.java 239 2004-07-28 05:09:17Z speakmon $
 * @author Ben Speakmon
 */
public class PingEvent extends EventObject {

    /**
     * Holds value of property message.
     */
    private String message;

    /**
     * Creates a new PingEvent.
     * @param source the ping message
     */
    public PingEvent(Object source) {
        super(source);
        message = (String) source;
    }

    /**
     * Returns the PING message.
     * @return the PING message
     */
    public String getMessage() {
        return this.message;
    }
}
