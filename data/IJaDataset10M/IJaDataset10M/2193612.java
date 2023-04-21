package org.xi8ix.jms;

import javax.jms.*;
import javax.naming.Context;
import java.io.Serializable;

/**
 * @author Iain Shigeoka
 */
public class JMSSession implements Session {

    private MessageListener messageListener;

    private Context context;

    public JMSSession(Context context) {
        this.context = context;
    }

    public BytesMessage createBytesMessage() throws JMSException {
        throw new JMSException("Operation not supported");
    }

    public MapMessage createMapMessage() throws JMSException {
        throw new JMSException("Operation not supported");
    }

    public Message createMessage() throws JMSException {
        return new JMSMessage();
    }

    public ObjectMessage createObjectMessage() throws JMSException {
        throw new JMSException("Operation not supported");
    }

    public ObjectMessage createObjectMessage(Serializable serializable) throws JMSException {
        throw new JMSException("Operation not supported");
    }

    public StreamMessage createStreamMessage() throws JMSException {
        return new JMSStreamMessage();
    }

    public TextMessage createTextMessage() throws JMSException {
        return new JMSTextMessage();
    }

    public TextMessage createTextMessage(String string) throws JMSException {
        return new JMSTextMessage(string);
    }

    public boolean getTransacted() throws JMSException {
        return false;
    }

    public int getAcknowledgeMode() throws JMSException {
        return 0;
    }

    public void commit() throws JMSException {
    }

    public void rollback() throws JMSException {
    }

    public void close() throws JMSException {
    }

    public void recover() throws JMSException {
    }

    public MessageListener getMessageListener() throws JMSException {
        return messageListener;
    }

    public void setMessageListener(MessageListener messageListener) throws JMSException {
        this.messageListener = messageListener;
    }

    public void run() {
    }

    public MessageProducer createProducer(Destination destination) throws JMSException {
        if (destination instanceof JMSDestination) {
            return new JMSMessageProducer((JMSDestination) destination);
        } else {
            throw new InvalidDestinationException("Destinations must be of type JMSMessage");
        }
    }

    public MessageConsumer createConsumer(Destination destination) throws JMSException {
        return createConsumer(destination, null, false);
    }

    public MessageConsumer createConsumer(Destination destination, String messageSelector) throws JMSException {
        return createConsumer(destination, messageSelector, false);
    }

    public MessageConsumer createConsumer(Destination destination, String messageSelector, boolean noLocal) throws JMSException {
        if (destination instanceof JMSDestination) {
            return new JMSMessageConsumer((JMSDestination) destination);
        } else {
            throw new InvalidDestinationException("Destinations must be of type JMSMessage");
        }
    }

    public Queue createQueue(String string) throws JMSException {
        throw new JMSException("Operation not supported");
    }

    public Topic createTopic(String string) throws JMSException {
        throw new JMSException("Operation not supported");
    }

    public TopicSubscriber createDurableSubscriber(Topic topic, String string) throws JMSException {
        throw new JMSException("Operation not supported");
    }

    public TopicSubscriber createDurableSubscriber(Topic topic, String string, String string1, boolean b) throws JMSException {
        throw new JMSException("Operation not supported");
    }

    public QueueBrowser createBrowser(Queue queue) throws JMSException {
        throw new JMSException("Operation not supported");
    }

    public QueueBrowser createBrowser(Queue queue, String string) throws JMSException {
        throw new JMSException("Operation not supported");
    }

    public TemporaryQueue createTemporaryQueue() throws JMSException {
        throw new JMSException("Operation not supported");
    }

    public TemporaryTopic createTemporaryTopic() throws JMSException {
        throw new JMSException("Operation not supported");
    }

    public void unsubscribe(String string) throws JMSException {
        throw new JMSException("Operation not supported");
    }
}
