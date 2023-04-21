package org.bastion.message;

/**
 * Domain message that can be used for sending text messages to a queue.
 * 
 * @author Danny
 */
public class SendToQueueMessage implements DomainMessage {

    private final String text;

    public SendToQueueMessage(String text) {
        super();
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
