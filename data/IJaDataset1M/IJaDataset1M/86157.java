package org.localstorm.activemq.test;

import java.util.Properties;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import org.apache.activemq.broker.BrokerService;

public class Main {

    private static final String JMS_DESTINATION_NAME = "dynamicQueues/FOO.BAR";

    /**
   * @param args
   */
    public static void main(String[] args) throws Exception {
        BrokerService broker = new BrokerService();
        broker.addConnector("tcp://localhost:61616");
        broker.setUseShutdownHook(false);
        broker.start();
        Properties props = new Properties();
        props.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
        props.setProperty(Context.PROVIDER_URL, "tcp://localhost:61616");
        InitialContext ic = new InitialContext(props);
        ConnectionFactory cf = (ConnectionFactory) ic.lookup("ConnectionFactory");
        Thread listener = new Thread(new JmsRunnable(ic, JMS_DESTINATION_NAME));
        listener.start();
        Thread.sleep(1000);
        Destination dest = (Destination) ic.lookup(JMS_DESTINATION_NAME);
        Connection connection = null;
        Session session = null;
        MessageProducer producer = null;
        try {
            connection = cf.createConnection();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            producer = session.createProducer(dest);
            for (int i = 0; i < 10; i++) {
                TextMessage message = session.createTextMessage();
                message.setText("Test-" + i);
                producer.send(message);
                System.out.println("Sending message: " + message.getText() + " [" + i + "]");
            }
        } catch (JMSException e) {
            System.out.println("Exception occurred: " + e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        }
        Thread.sleep(1000);
        listener.interrupt();
        listener.join();
        broker.stop();
    }
}
