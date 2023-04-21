package com.openjmsadapter.transport;

import javax.jms.BytesMessage;
import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TextMessage;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author hari
 */
public class PublisherEndpointTest {

    @Test
    public void testInitializeWithJMSMessagePublisher() throws Exception {
        Connection connection = TestJMSUtil.getActiveMQConnection(TestJMSUtil.loadConfig("C:/NetBeansProjects/openjmsadapter/test/test_jms_config.yml"));
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        MessageProducer producer = session.createProducer(session.createQueue("test.queue"));
        assertNotNull(new PublisherEndpoint(producer));
        producer.close();
        session.close();
        connection.close();
    }

    @Test
    public void testPublishBytesMessage() throws Exception {
        Connection connection = TestJMSUtil.getActiveMQConnection(TestJMSUtil.loadConfig("C:/NetBeansProjects/openjmsadapter/test/test_jms_config.yml"));
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        MessageProducer producer = session.createProducer(session.createQueue("test.queue"));
        PublisherEndpoint pub = new PublisherEndpoint(producer);
        BytesMessage message = session.createBytesMessage();
        message.writeBytes("HELLO".getBytes());
        pub.sendBytesMessage(message);
        MessageConsumer consumer = session.createConsumer(session.createQueue("test.queue"));
        consumer.receive(5000);
        consumer.close();
        producer.close();
        session.close();
        connection.close();
    }

    @Test
    public void testPublishTextMessage() throws Exception {
        Connection connection = TestJMSUtil.getActiveMQConnection(TestJMSUtil.loadConfig("C:/NetBeansProjects/openjmsadapter/test/test_jms_config.yml"));
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        MessageProducer producer = session.createProducer(session.createQueue("test.queue"));
        PublisherEndpoint pub = new PublisherEndpoint(producer);
        TextMessage message = session.createTextMessage();
        message.setText("HELLO");
        pub.sendTextMessage(message);
        MessageConsumer consumer = session.createConsumer(session.createQueue("test.queue"));
        consumer.receive(5000);
        consumer.close();
        producer.close();
        session.close();
        connection.close();
    }

    @Test
    public void testPublishObjectMessage() throws Exception {
        Connection connection = TestJMSUtil.getActiveMQConnection(TestJMSUtil.loadConfig("C:/NetBeansProjects/openjmsadapter/test/test_jms_config.yml"));
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        MessageProducer producer = session.createProducer(session.createQueue("test.queue"));
        PublisherEndpoint pub = new PublisherEndpoint(producer);
        ObjectMessage message = session.createObjectMessage();
        message.setObject(new Integer(1));
        pub.sendObjectMessage(message);
        MessageConsumer consumer = session.createConsumer(session.createQueue("test.queue"));
        consumer.receive(5000);
        consumer.close();
        producer.close();
        session.close();
        connection.close();
    }

    @Test(expected = JMSException.class)
    public void testEndpointDisconnect() throws JMSException {
        PublisherEndpoint pub = null;
        ObjectMessage message = null;
        try {
            Connection connection = TestJMSUtil.getActiveMQConnection(TestJMSUtil.loadConfig("C:/NetBeansProjects/openjmsadapter/test/test_jms_config.yml"));
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            message = session.createObjectMessage();
            message.setObject(new Integer(1));
            MessageProducer producer = session.createProducer(session.createQueue("test.queue"));
            pub = new PublisherEndpoint(producer);
        } catch (Exception e) {
            fail("Test setup failed due to " + e.getMessage());
        }
        if (pub != null && message != null) {
            pub.disconnect();
            pub.sendObjectMessage(message);
        }
    }
}
