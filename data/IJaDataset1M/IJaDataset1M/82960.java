package org.tolven.core.util;

import java.io.Serializable;
import java.util.Map;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import org.tolven.doc.bean.TolvenMessage;
import org.tolven.doc.bean.TolvenMessageInfo;

public class Queuer {

    private Connection connection = null;

    private javax.jms.Session session = null;

    private MessageProducer messageProducer = null;

    private Queue queue = null;

    /**
    	 * A human-readable display of the queue
    	 */
    @Override
    public String toString() {
        try {
            return "ClientId: " + connection.getClientID() + " Queue: " + queue.getQueueName();
        } catch (JMSException e) {
            e.printStackTrace();
            return "Queing Error";
        }
    }

    public void init(ConnectionFactory connectionFactory, Queue queue) {
        try {
            connection = connectionFactory.createConnection();
            session = connection.createSession(false, javax.jms.Session.AUTO_ACKNOWLEDGE);
            this.queue = queue;
            messageProducer = session.createProducer(queue);
        } catch (Exception e) {
            String qName = null;
            try {
                qName = queue.getQueueName();
            } catch (JMSException ex) {
                qName = "queue name unknown";
            }
            throw new RuntimeException("Error creating connection to " + qName, e);
        }
    }

    public void close() {
        try {
            messageProducer.close();
            connection.close();
        } catch (Exception e) {
            String qName = null;
            try {
                qName = queue.getQueueName();
            } catch (JMSException ex) {
                qName = "queue name unknown";
            }
            throw new RuntimeException("Error closing connection to " + qName, e);
        }
    }

    public void send(Serializable payload) {
        send(payload, null);
    }

    public void send(Serializable payload, Map<String, Object> properties) {
        try {
            boolean isTolvenMessage = payload instanceof TolvenMessage;
            ObjectMessage message = null;
            if (isTolvenMessage) {
                TolvenMessage tm = (TolvenMessage) payload;
                TolvenMessageInfo info = new TolvenMessageInfo(tm.getId());
                message = session.createObjectMessage(info);
            } else {
                message = session.createObjectMessage(payload);
            }
            if (properties != null) {
                for (String key : properties.keySet()) {
                    message.setObjectProperty(key, properties.get(key));
                }
            }
            messageProducer.send(message);
        } catch (JMSException ex) {
            throw new RuntimeException("Could not send payload", ex);
        }
    }
}
