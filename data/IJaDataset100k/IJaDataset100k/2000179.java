package de.uni_bremen.informatik.p2p.peeranha42.core.network.iccp.msgReplyQueue;

/**
 * @author M.Wuerthele
 * This class represents a TimeoutThread. If used it fires a timeout Event in ICCPMsgReplyQueue
 * after a specified (long timeout) time. 
 */
public class ICCPMsgReplyThread implements Runnable {

    private long timeout;

    private String messageID;

    public ICCPMsgReplyThread(ICCPMsgReplyObject replyObject) {
        this.timeout = replyObject.getTimeout();
        this.messageID = replyObject.getMessageID();
    }

    public void run() {
        try {
            Thread.sleep(timeout);
        } catch (Exception e) {
        }
        ICCPMsgReplyQueue.fireTimeoutEvent(this.messageID);
    }
}
