package myrojectWithLoadBalancing;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicSubscriber;
import org.apache.activemq.ActiveMQConnectionFactory;

public class ReceiveNewsletterAgent extends Thread {

    private Connection _connection = null;

    private Session _session = null;

    private TextListener _listener = null;

    private TopicSubscriber _subscriber = null;

    private String _idClient;

    private String _topicName;

    private Topic _topic;

    private int _numMessReceived;

    private String _brokerURL;

    /**
      * Costruttore. Crea gli elementi necessari per la comunicazione
      * @param id � l'id del cliente
      * @param topicName � il nome del topic che lo mette in contatto con l'edicola
      * @param brokerUrl � l'indirizzo del broker
      */
    public ReceiveNewsletterAgent(String id, String topicName, String brokerUrl) {
        try {
            _brokerURL = brokerUrl;
            _numMessReceived = 0;
            _idClient = id;
            _topicName = topicName;
            ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(_brokerURL);
            _connection = factory.createConnection();
            _connection.setClientID(_idClient);
            _session = _connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            _topic = _session.createTopic(_topicName);
        } catch (Exception e) {
            System.err.println("Connection problem: " + e.toString());
            if (_connection != null) {
                try {
                    _connection.close();
                } catch (JMSException ee) {
                }
            }
            System.exit(1);
        }
    }

    /**
      * Stops connection, then creates durable subscriber,
      * registers message listener (TextListener), and starts
      * message delivery; listener displays the messages
      * obtained.
      */
    public void startSubscriber() {
        try {
            println("Starting..");
            _connection.stop();
            _subscriber = _session.createDurableSubscriber(_topic, _idClient);
            _listener = new TextListener();
            _subscriber.setMessageListener(_listener);
            _connection.start();
        } catch (JMSException e) {
            System.err.println("startSubscriber: Exception occurred: " + e.toString());
        }
    }

    /**
      * Blocks until publisher issues a control message
      * indicating end of publish stream, then closes
      * subscriber.
      */
    public void closeSubscriber() {
        try {
            _listener.monitor.waitTillDone();
            println("Closing..");
            _subscriber.close();
        } catch (JMSException e) {
            println("Exception occurred: " + e.toString());
        }
    }

    /**
      * Closes the connection.
      */
    public void finish() {
        if (_connection != null) {
            try {
                println("Unsubscribing from durable subscription");
                _session.unsubscribe(_idClient);
                _connection.close();
            } catch (JMSException e) {
            }
        }
    }

    @Override
    public void run() {
        startSubscriber();
        while (_numMessReceived == 1) ;
        closeSubscriber();
        finish();
    }

    /**
 	 * Scrive a video il messaggio con un determinato formato
 	 * @param message � il messaggio da scrivere a video
 	 */
    private void println(String message) {
        System.out.println("ReceiveNewsletterAgent: " + message);
    }

    /**
      * The TextListener class implements the MessageListener
      * interface by defining an onMessage method for the
      * DurableSubscriber class.
      */
    private class TextListener implements MessageListener {

        final SampleUtilities.DoneLatch monitor = new SampleUtilities.DoneLatch();

        /**
          * Casts the message to a TextMessage and displays
          * its text. A non-text message is interpreted as the
          * end of the message stream, and the message
          * listener sets its monitor state to all done
          * processing messages.
          *
          * @param message    the incoming message
          */
        public void onMessage(Message message) {
            if (message instanceof TextMessage) {
                TextMessage msg = (TextMessage) message;
                try {
                    println("Reading message: " + msg.getText());
                    _numMessReceived++;
                } catch (JMSException e) {
                    System.err.println("Exception in " + "onMessage(): " + e.toString());
                }
            } else {
                monitor.allDone();
            }
        }
    }
}
