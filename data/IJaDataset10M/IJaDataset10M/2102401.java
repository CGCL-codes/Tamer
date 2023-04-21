package com.continuent.tungsten.replicator;

import com.continuent.tungsten.commons.patterns.fsm.Event;

/**
 * This class defines a ErrorNotification, which denotes a severe replication
 * error that causes replication to fail. 
 * 
 * @author <a href="mailto:teemu.ollakka@continuent.com">Teemu Ollakka</a>
 * @version 1.0
 */
public class ErrorNotification extends Event {

    private final String userMessage;

    private final long seqno;

    private final String eventId;

    /**
     * Create new instance with underlying error and message for presentation to
     * users.
     */
    public ErrorNotification(String userMessage, Throwable e) {
        super(e);
        this.userMessage = userMessage;
        this.seqno = -1;
        this.eventId = null;
    }

    /**
     * Creates an error notification with user, a message, and replication
     * position information.
     */
    public ErrorNotification(String userMessage, long seqno, String eventId, Throwable e) {
        super(e);
        this.userMessage = userMessage;
        this.seqno = seqno;
        this.eventId = eventId;
    }

    /**
     * Returns the original source of the error.
     */
    public Throwable getThrowable() {
        return (Throwable) getData();
    }

    /**
     * Returns a message suitable for users.
     */
    public String getUserMessage() {
        return userMessage;
    }

    /**
     * Returns the log sequence number associated with failure or -1 if there is
     * no such number.
     */
    public long getSeqno() {
        return seqno;
    }

    /**
     * Returns the native event ID associated with failure or null if there is
     * no such ID.
     */
    public String getEventId() {
        return eventId;
    }
}
