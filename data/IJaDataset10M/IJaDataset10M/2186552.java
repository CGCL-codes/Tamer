package org.langkiss.google;

import org.langkiss.gui.IFeedback;

/**
 *
 * @author tom
 */
public class FeedbackReceiverDummy implements IFeedback {

    private String receivedMessage = "";

    @Override
    public synchronized void receiveFeedback(String message) {
        this.setReceivedMessage(message);
    }

    /**
     * @return the receivedMessage
     */
    public synchronized String getReceivedMessage() {
        return receivedMessage;
    }

    /**
     * @param receivedMessage the receivedMessage to set
     */
    public synchronized void setReceivedMessage(String receivedMessage) {
        this.receivedMessage = receivedMessage;
    }
}
