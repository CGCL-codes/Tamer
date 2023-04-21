package org.speakmon.babble.events;

import java.util.EventObject;

/**
 * This event is a notification that a raw message has been sent to the server.
 * @version $Id: RawMessageSentEvent.java 239 2004-07-28 05:09:17Z speakmon $
 * @author Ben Speakmon
 */
public class RawMessageSentEvent extends EventObject {

    /**
     * Holds value of property message.
     */
    private String message;

    /**
     * Creates a new RawMessageSentEvent.
     * @param source the raw message sent to the server
     */
    public RawMessageSentEvent(Object source) {
        super(source);
        message = (String) source;
    }

    /**
     * Returns the raw message sent to the server.
     * @return the raw message
     */
    public String getMessage() {
        return this.message;
    }
}
