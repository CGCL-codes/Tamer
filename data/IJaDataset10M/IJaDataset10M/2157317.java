package org.gwtoolbox.comet.client;

/**
 * Sample comet event for sending a message to the client.
 *
 * @author Tom van Zummeren
 */
public class MessageCometEvent extends CometEvent {

    private String message;

    /**
     * Constructs a new {@link MessageCometEvent} with a message.
     *
     * @param message message to send
     */
    public MessageCometEvent(String message) {
        this.message = message;
    }

    /**
     * {@inheritDoc}
     */
    public String getDescription() {
        return message;
    }
}
