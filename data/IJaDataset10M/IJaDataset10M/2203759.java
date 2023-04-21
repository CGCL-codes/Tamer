package jist.swans.net;

import jist.swans.mac.MacAddress;
import jist.swans.misc.Message;

/**
 * A message object that can be queued in <code>MessageQueue</code>.
 *
 * @author Rimon Barr &lt;barr+jist@cs.cornell.edu&gt;
 * @version $Id: QueuedMessage.java,v 1.1 2006/10/21 00:03:54 lmottola Exp $
 * @since SWANS1.0
 */
public class QueuedMessage implements Message {

    /**
   * Queued message payload.
   */
    private Message payload;

    /**
   * Next hop that message should traverse.
   */
    private MacAddress nextHop;

    /**
   * Pointer to next queued message.
   */
    public QueuedMessage next;

    /**
   * Create new queued message.
   *
   * @param payload actual message being queued
   * @param nextHop nextHop of message
   */
    public QueuedMessage(Message payload, MacAddress nextHop) {
        this.payload = payload;
        this.nextHop = nextHop;
    }

    /**
   * Return payload.
   *
   * @return payload
   */
    public Message getPayload() {
        return payload;
    }

    /**
   * Return next link hop.
   *
   * @return next link hop
   */
    public MacAddress getNextHop() {
        return nextHop;
    }

    /** {@inheritDoc} */
    public int getSize() {
        return payload.getSize();
    }

    /** {@inheritDoc} */
    public void getBytes(byte[] msg, int offset) {
        payload.getBytes(msg, offset);
    }
}
