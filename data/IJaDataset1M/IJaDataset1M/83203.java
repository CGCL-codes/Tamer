package ex4;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;

public class Client {

    private ConnectionFactory connectionFactory;

    private Destination destinationRequete;

    private Destination destinationReponse;

    private Session session;

    private Connection connection;

    public static void main(String[] args) {
        try {
            Client client = new Client();
            client.connect();
            client.sendMessages();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    private void waitForMessage() throws Exception {
        connection.start();
        MessageConsumer consumer = session.createConsumer(destinationReponse);
        consumer.setMessageListener(new MessageListener() {

            @Override
            public void onMessage(Message m) {
                TextMessage textMsg = (TextMessage) m;
                try {
                    traiterMessage(textMsg);
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void traiterMessage(TextMessage m) throws JMSException {
        System.out.println("r�ponse obtenue, requ�te ID: " + m.getJMSMessageID() + "---> " + m.getText());
    }

    private void sendMessages() throws Exception {
        MessageProducer producer = session.createProducer(destinationRequete);
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            System.out.println("\nTapper un ID � chercher (QUIT pour arreter)");
            String messageInsere = reader.readLine();
            if (messageInsere.equals("QUIT")) {
                break;
            }
            System.out.println("ID: " + messageInsere);
            TextMessage message = session.createTextMessage(messageInsere);
            producer.send(message);
            System.out.println("requ�te envoy�, ID =  " + message.getJMSMessageID());
            waitForMessage();
        }
        session.close();
        connection.close();
        System.out.println("\nclose");
    }

    private void connect() throws Exception {
        Context jndiContext = new InitialContext();
        connectionFactory = (ConnectionFactory) jndiContext.lookup("connectionFactory");
        destinationRequete = (Destination) jndiContext.lookup("MyRequete");
        destinationReponse = (Destination) jndiContext.lookup("MyReponse");
        connection = connectionFactory.createConnection();
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
    }
}
