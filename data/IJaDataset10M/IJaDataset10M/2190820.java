package org.exolab.jms.server;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import javax.jms.JMSException;
import javax.jms.Session;
import org.exolab.jms.messagemgr.MessageHandle;

/**
 * Helper class to cache all sent messages and unacked messages for a session.
 * It also does some other processing like marking the message as sent to
 * minimize the number of transactions.
 * <p/>
 * Messages will only be added to the cache, if the session is transacted or the
 * ack mode for the session is set to CLIENT_ACKNOWLEDGE
 *
 * @author <a href="mailto:jima@exoffice.com">Jim Alateras</a>
 * @author <a href="mailto:tma@netspace.net.au">Tim Anderson</a>
 * @version $Revision: 1.5 $ $Date: 2005/12/20 20:31:59 $
 * @see ServerSessionImpl
 */
class SentMessageCache {

    /**
     * The message acknowledgement mode, or <code>Session.TRANSACTED_SESSION</code>
     * if the session is transactional.
     */
    private final int _ackMode;

    /**
     * Holds a list of unacked messages in the order they were sent.
     */
    private List _unackedMessages = Collections.synchronizedList(new LinkedList());

    /**
     * Construct a new <code>SentMessageCache</code>.
     *
     * @param ackMode  the message acknowledgement mode, or
     *                 <code>Session.TRANSACTED_SESSION</code>
     *                 if the session is transactional
     */
    public SentMessageCache(int ackMode) {
        _ackMode = ackMode;
    }

    /**
     * Process a messge handle prior to it being sent to the client.
     * Applicable to both synchronous and asynchronous delivery.
     *
     * @param handle the message handle
     * @throws JMSException for any error
     */
    public void preSend(MessageHandle handle) throws JMSException {
        handle.setDelivered(true);
        _unackedMessages.add(handle);
        if (handle.isPersistent()) {
            handle.update();
        }
    }

    /**
     * Process a message handle after it has been successfully sent to the
     * client using asynchronous delivery.
     *
     * @param handle the message handle
     * @throws JMSException for any error
     */
    public void postSend(MessageHandle handle) throws JMSException {
        if (_ackMode == Session.AUTO_ACKNOWLEDGE || _ackMode == Session.DUPS_OK_ACKNOWLEDGE) {
            _unackedMessages.remove(handle);
            handle.destroy();
        }
    }

    /**
     * Acknowledge the specified messages in the cache and all previously sent
     * messages.
     *
     * @param messageId  the id of the message to ack
     * @param consumerId the consumer id that sent the ack.
     * @throws JMSException if the acknowledge fails
     */
    public void acknowledge(String messageId, long consumerId) throws JMSException {
        boolean exists = false;
        Iterator iterator = _unackedMessages.iterator();
        while (iterator.hasNext()) {
            MessageHandle handle = (MessageHandle) iterator.next();
            if (handle.getConsumerId() == consumerId && handle.getMessageId().equals(messageId)) {
                exists = true;
                break;
            }
        }
        if (exists) {
            while (!_unackedMessages.isEmpty()) {
                MessageHandle handle = (MessageHandle) _unackedMessages.remove(0);
                handle.destroy();
                if (handle.getConsumerId() == consumerId && handle.getMessageId().equals(messageId)) {
                    break;
                }
            }
        }
    }

    /**
     * Acknowledge all the messages in the cache.
     *
     * @throws JMSException for any error
     */
    public void acknowledgeAll() throws JMSException {
        while (!_unackedMessages.isEmpty()) {
            MessageHandle handle = (MessageHandle) _unackedMessages.remove(0);
            handle.destroy();
        }
    }

    /**
     * Release all unacknowledged message handles.
     *
     * @throws JMSException for any error
     */
    public void clear() throws JMSException {
        if (!_unackedMessages.isEmpty()) {
            MessageHandle[] handles = (MessageHandle[]) _unackedMessages.toArray(new MessageHandle[0]);
            _unackedMessages.clear();
            for (int i = 0; i < handles.length; ++i) {
                MessageHandle handle = handles[i];
                handle.release();
            }
        }
    }
}
