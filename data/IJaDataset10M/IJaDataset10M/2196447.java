package com.mycompany.basic;

import javax.jms.Connection;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnectionFactory;
import com.mycompany.basic.util.AdministeredObjectsAccess;

/**
 * This class demonstrate how a JMS consumer works.
 * 
 * @author Luiz Almeida
 *
 */
public class BasicConsumer {

    /**
	 * Flag used to durable subscribing
	 */
    private static boolean DURABLE_SUBSCRIBE = true;

    /**
	 * The subscribe name used to durable subscribing
	 */
    private static final String SUBSCRIBER_NAME = "MyDurableSubscribeUId";

    /**
	 * The client id value used for durable subscribing
	 */
    private static final String CLIENT_ID = "MyClientID";

    /**
	 * Flag used to session transaction mode
	 */
    private static boolean SESSION_TRANSACTION = true;

    public static void main(String[] args) throws Exception {
        String lookUpFactory = args[0];
        String lookUpTopic = args[1];
        AdministeredObjectsAccess adminObjects = AdministeredObjectsAccess.getInstance();
        TopicConnectionFactory factory = (TopicConnectionFactory) adminObjects.getConnectionFactory(lookUpFactory);
        Topic myTopic = (Topic) adminObjects.getTopic(lookUpTopic);
        Connection connection = factory.createConnection();
        if (DURABLE_SUBSCRIBE) {
            connection.setClientID(CLIENT_ID);
        }
        Session session = null;
        if (SESSION_TRANSACTION) {
            session = connection.createSession(true, Session.SESSION_TRANSACTED);
        } else {
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        }
        MessageConsumer consumer = null;
        if (DURABLE_SUBSCRIBE) {
            consumer = session.createDurableSubscriber(myTopic, SUBSCRIBER_NAME);
        } else {
            consumer = session.createConsumer(myTopic);
        }
        connection.start();
        int i = 0;
        while (i++ < 10) {
            TextMessage msg = (TextMessage) consumer.receive();
            System.out.println("Receive " + msg.getText());
            Thread.sleep(500);
        }
        if (SESSION_TRANSACTION) {
            session.rollback();
        }
        i = 0;
        while (i++ < 10) {
            TextMessage msg = (TextMessage) consumer.receive();
            System.out.println("Receive " + msg.getText());
            Thread.sleep(500);
        }
        if (SESSION_TRANSACTION) {
            session.commit();
        }
        connection.close();
    }
}
