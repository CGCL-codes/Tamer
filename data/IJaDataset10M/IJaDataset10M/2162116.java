package eu.popeye.middleware.publishsubscribe;

import java.io.Serializable;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Topic;
import javax.jms.TopicPublisher;
import eu.popeye.middleware.groupmanagement.management.GroupHandle;
import eu.popeye.networkabstraction.communication.basic.adapter.MessageAdapter;

/**
 * Implementation of the TopicPublisher. Allows users to send messages
 * under a certain topic
 * @author Marcel Arrufat Arias
 */
public class TopicPublisherImpl implements TopicPublisher {

    private Topic topic;

    private GroupHandle groupHandle;

    private String topicId;

    /**
	 * Assign the corresponding topic to the topicPublisher
	 * @param topic
	 */
    public TopicPublisherImpl(GroupHandle groupHandle, Topic topic) {
        this.topic = topic;
        this.groupHandle = groupHandle;
        try {
            String name = topic.getTopicName();
            this.topicId = TopicImpl.getTopicId(name).toString();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    /**
	 * Returns the topic
	 */
    public Topic getTopic() throws JMSException {
        return topic;
    }

    /**
	 * Allows to send a message (in this case, just an ObjectMessage)
	 */
    public void publish(Message msg) throws JMSException {
        publishMessageToTopic(msg, topicId);
    }

    /**
	 * Sends a message to a speficied topic
	 * @param msg
	 * @param topicIdentifier
	 * @throws JMSException
	 */
    private void publishMessageToTopic(Message msg, String topicIdentifier) throws JMSException {
        if (msg instanceof ObjectMessage) {
            Serializable obj = ((ObjectMessage) msg).getObject();
            try {
                groupHandle.getCommAdapter().sendGroup(topicIdentifier, MessageAdapter.createPopeyeObjectMessage(obj));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            throw new JMSException("Only ObjectMessage delivery is allowed");
        }
    }

    public void publish(Message arg0, int arg1, int arg2, long arg3) throws JMSException {
        publish(arg0);
    }

    /**
	 * Publishes a message to any topic
	 */
    public void publish(Topic arg0, Message arg1) throws JMSException {
        publishMessageToTopic(arg1, TopicImpl.getTopicId(arg0.getTopicName()).toString());
    }

    public void publish(Topic arg0, Message arg1, int arg2, int arg3, long arg4) throws JMSException {
        publishMessageToTopic(arg1, TopicImpl.getTopicId(arg0.getTopicName()).toString());
    }

    public void close() throws JMSException {
    }

    public int getDeliveryMode() throws JMSException {
        raiseJMSException();
        return 0;
    }

    public boolean getDisableMessageID() throws JMSException {
        raiseJMSException();
        return false;
    }

    public boolean getDisableMessageTimestamp() throws JMSException {
        raiseJMSException();
        return false;
    }

    public int getPriority() throws JMSException {
        raiseJMSException();
        return 0;
    }

    public long getTimeToLive() throws JMSException {
        raiseJMSException();
        return 0;
    }

    public void setDeliveryMode(int arg0) throws JMSException {
        raiseJMSException();
    }

    public void setDisableMessageID(boolean arg0) throws JMSException {
        raiseJMSException();
    }

    public void setDisableMessageTimestamp(boolean arg0) throws JMSException {
        raiseJMSException();
    }

    public void setPriority(int arg0) throws JMSException {
        raiseJMSException();
    }

    public void setTimeToLive(long arg0) throws JMSException {
        raiseJMSException();
    }

    public Destination getDestination() throws JMSException {
        raiseJMSException();
        return null;
    }

    public void send(Message message) throws JMSException {
        raiseJMSException();
    }

    public void send(Message message, int deliveryMode, int priority, long timeToLive) throws JMSException {
        raiseJMSException();
    }

    public void send(Destination destination, Message message) throws JMSException {
        raiseJMSException();
    }

    public void send(Destination destination, Message message, int deliveryMode, int priority, long timeToLive) throws JMSException {
        raiseJMSException();
    }

    /**
	 * This method is invoked when a non-implemented JMS method is invoked 
	 * @throws JMSException
	 */
    private void raiseJMSException() throws JMSException {
        new PopeyeNotImplementedException(this.getClass().getName());
    }
}
