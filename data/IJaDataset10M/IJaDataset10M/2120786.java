package net.sf.freesimrc;

import java.util.Date;

/**
 * Basic class for holding information about a (text-only) communication
 * message.
 */
public class CommMessage {

    /**
     * The time at which the message was received. This field is
     * set by the constructor.
     */
    protected Date time;

    /**
     * The callsign (or other unique ID) of the sender.
     */
    protected String sender;

    /**
     * The content of the message.
     */
    protected String content;

    /**
     * Constructs a new message with the given sender and content.
     * The {@link #time} field is set to the current time of the system.
     * @param sender The callsign (or other unique ID) of the sender.
     * @param content The content of the message.
     */
    public CommMessage(String sender, String content) {
        this.time = new Date();
        this.sender = sender;
        this.content = content;
    }

    /**
     * Returns the time at which the message was received (more precise:
     * at which the constructor was called).
     * @return The time at which the message was received.
     */
    public Date getTime() {
        return time;
    }

    /**
     * Returns the callsign of the sender.
     * @return The callsign of the sender.
     */
    public String getSender() {
        return sender;
    }

    /**
     * Returns the content of the message.
     * @return The content of the message.
     */
    public String getContent() {
        return content;
    }
}
