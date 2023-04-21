package org.exolab.jms.messagemgr;

import javax.jms.JMSException;
import javax.jms.MessageConsumer;

/**
 * A {@link MessageHandle} used by the {@link TopicDestinationCache}.
 *
 * @author <a href="mailto:tma@netspace.net.au">Tim Anderson</a>
 * @version $Revision: 1.2 $ $Date: 2005/05/13 12:57:02 $
 */
class TopicConsumerMessageHandle extends AbstractConsumerMessageHandle {

    /**
     * If <code>true</code>, indicates that the message associated with the
     * handle has been delivered, but not acknowledged.
     * This overrides the delivery status of the underlying handle, which
     * may be shared between multiple consumers.
     */
    private boolean _delivered = false;

    /**
     * Construct a new <code>TopicConsumerMessageHandle</code>.
     *
     * @param handle   the underlying handle
     * @param consumer the consumer of the handle
     * @throws JMSException if the underlying message can't be referenced
     */
    public TopicConsumerMessageHandle(MessageHandle handle, ConsumerEndpoint consumer) throws JMSException {
        super(handle, consumer);
        init(handle);
    }

    /**
     * Construct a new <code>TopicConsumerMessageHandle</code>
     * for a durable consumer.
     *
     * @param handle       the underlying handle
     * @param persistentId the persistent identity of the consumer
     * @throws JMSException if the underlying message can't be referenced
     */
    public TopicConsumerMessageHandle(MessageHandle handle, String persistentId) throws JMSException {
        super(handle, persistentId);
        init(handle);
    }

    /**
     * Indicates if a message has been delivered to a {@link MessageConsumer},
     * but not acknowledged.
     *
     * @param delivered if <code>true</code> indicates that an attempt has been
     *                  made to deliver the message
     */
    public void setDelivered(boolean delivered) {
        _delivered = delivered;
    }

    /**
     * Returns if an attempt has already been made to deliver the message.
     *
     * @return <code>true</code> if delivery has been attempted
     */
    public boolean getDelivered() {
        return _delivered;
    }

    /**
     * Initialise this handle.
     *
     * @param handle the underlying handle
     */
    private void init(MessageHandle handle) {
        _delivered = handle.getDelivered();
    }
}
