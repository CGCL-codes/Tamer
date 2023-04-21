package com.ohua.eai.resources;

import java.io.Serializable;
import javax.jms.BytesMessage;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueBrowser;
import javax.jms.Session;
import javax.jms.StreamMessage;
import javax.jms.TemporaryQueue;
import javax.jms.TemporaryTopic;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicSubscriber;
import com.ohua.engine.resource.management.ConnectionID;
import com.ohua.engine.resource.management.ResourceConnection;

public class JMSQueueConnection extends ResourceConnection implements Session {

    private Session _session = null;

    protected JMSQueueConnection(ConnectionID connectionID, Session session) {
        super(connectionID);
        _session = session;
    }

    public void close() throws JMSException {
        throw new UnsupportedOperationException("ResourceManager buisness");
    }

    public void commit() throws JMSException {
        throw new UnsupportedOperationException("ResourceManager buisness");
    }

    public QueueBrowser createBrowser(Queue arg0) throws JMSException {
        return _session.createBrowser(arg0);
    }

    public QueueBrowser createBrowser(Queue arg0, String arg1) throws JMSException {
        return _session.createBrowser(arg0, arg1);
    }

    public BytesMessage createBytesMessage() throws JMSException {
        return _session.createBytesMessage();
    }

    public MessageConsumer createConsumer(Destination arg0) throws JMSException {
        return _session.createConsumer(arg0);
    }

    public MessageConsumer createConsumer(Destination arg0, String arg1) throws JMSException {
        return _session.createConsumer(arg0, arg1);
    }

    public MessageConsumer createConsumer(Destination arg0, String arg1, boolean arg2) throws JMSException {
        return _session.createConsumer(arg0, arg1, arg2);
    }

    public TopicSubscriber createDurableSubscriber(Topic arg0, String arg1) throws JMSException {
        return _session.createDurableSubscriber(arg0, arg1);
    }

    public TopicSubscriber createDurableSubscriber(Topic arg0, String arg1, String arg2, boolean arg3) throws JMSException {
        return _session.createDurableSubscriber(arg0, arg1, arg2, arg3);
    }

    public MapMessage createMapMessage() throws JMSException {
        return _session.createMapMessage();
    }

    public Message createMessage() throws JMSException {
        return _session.createMessage();
    }

    public ObjectMessage createObjectMessage() throws JMSException {
        return _session.createObjectMessage();
    }

    public ObjectMessage createObjectMessage(Serializable arg0) throws JMSException {
        return _session.createObjectMessage(arg0);
    }

    public MessageProducer createProducer(Destination arg0) throws JMSException {
        return _session.createProducer(arg0);
    }

    public Queue createQueue(String arg0) throws JMSException {
        return _session.createQueue(arg0);
    }

    public StreamMessage createStreamMessage() throws JMSException {
        return _session.createStreamMessage();
    }

    public TemporaryQueue createTemporaryQueue() throws JMSException {
        return _session.createTemporaryQueue();
    }

    public TemporaryTopic createTemporaryTopic() throws JMSException {
        return _session.createTemporaryTopic();
    }

    public TextMessage createTextMessage() throws JMSException {
        return _session.createTextMessage();
    }

    public TextMessage createTextMessage(String arg0) throws JMSException {
        return _session.createTextMessage(arg0);
    }

    public Topic createTopic(String arg0) throws JMSException {
        return _session.createTopic(arg0);
    }

    public int getAcknowledgeMode() throws JMSException {
        return _session.getAcknowledgeMode();
    }

    public MessageListener getMessageListener() throws JMSException {
        return _session.getMessageListener();
    }

    public boolean getTransacted() throws JMSException {
        return _session.getTransacted();
    }

    public void recover() throws JMSException {
        throw new UnsupportedOperationException("ResourceManager buisness");
    }

    public void rollback() throws JMSException {
        throw new UnsupportedOperationException("ResourceManager buisness");
    }

    public void run() {
        _session.run();
    }

    public void setMessageListener(MessageListener arg0) throws JMSException {
        _session.setMessageListener(arg0);
    }

    public void unsubscribe(String arg0) throws JMSException {
        _session.unsubscribe(arg0);
    }
}
