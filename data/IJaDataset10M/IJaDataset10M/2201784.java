package org.jazzteam.example.jms.simple;

import java.util.logging.Logger;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;

/**
 * This is a simple example to show some basics of JMS for a publish - subscribe
 * scenario.
 */
public class ExamplePublishAndSubscribe {

    public static final String TOPIC1 = "Simple.Test.Topic1";

    private static Logger jdkLogger = Logger.getLogger("com.zcage.jms.ExamplePublishAndSubscribe");

    /**
     * Create a JMS Publisher and Subscriber. Of course in the real world these
     * would be in separate applications. Start these processes and let them run
     * a while before shutting down. Execution comments will be logged.
     */
    public static void main(String[] args) throws Exception {
        startBroker();
        Publisher publisher = new Publisher();
        publisher.start();
        Thread.sleep(3000);
        Subscriber subscriber = new Subscriber();
        subscriber.startListening();
        Thread.sleep(10000);
        publisher.stopPublishing();
        subscriber.stopListening();
        jdkLogger.info("Exiting");
        System.exit(0);
    }

    /**
     * Create an Embedded JMS Broker for this example. Requires JDK1.5.
     */
    private static void startBroker() throws Exception {
        jdkLogger.info("Starting Broker");
        BrokerService broker = new BrokerService();
        broker.setUseJmx(true);
        broker.addConnector("tcp://localhost:61616");
        broker.start();
        jdkLogger.info("Broker started");
    }

    /**
     * Use the ActiveMQConnectionFactory to get a JMS ConnectionFactory. In an
     * enterprise application this would normally be accessed through JNDI.
     */
    public static ConnectionFactory getJmsConnectionFactory() throws JMSException {
        String user = ActiveMQConnection.DEFAULT_USER;
        String password = ActiveMQConnection.DEFAULT_PASSWORD;
        String url = ActiveMQConnection.DEFAULT_BROKER_URL;
        return new ActiveMQConnectionFactory(user, password, url);
    }
}
