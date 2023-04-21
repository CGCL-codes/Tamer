package myProjectWithDiscoveryBroker;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * Invia un messaggio ai subscriber interessati utilizzando JMS 
 */
public class SendNewsletterAgent extends Thread {

    private Connection _connection = null;

    private MessageProducer _producer = null;

    private Session _session = null;

    private Topic _topic;

    static int _startindex = 0;

    private String _topicName;

    private String _message;

    private String _brokerUrl;

    /**
     * Costruttore
     * @param topicName � il nome del topic
     * @param brokerUrl � l'indirizzo del broker
     */
    public SendNewsletterAgent(String topicName, String brokerUrl) {
        try {
            _topicName = topicName;
            _brokerUrl = brokerUrl;
            ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(_brokerUrl);
            _connection = factory.createConnection();
            _session = _connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            _topic = _session.createTopic(_topicName);
            _producer = _session.createProducer(_topic);
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
	 * Imposta il messaggio da inviare via JMS
	 * @param message � il mess da inviare
	 */
    public void setMessage(String message) {
        _message = message;
    }

    /**
     * Creates text message.
     * Sends some messages, varying text slightly.
     * Messages must be persistent.
     */
    public void publishMessages() {
        TextMessage message = null;
        try {
            message = _session.createTextMessage();
            message.setText(_message);
            _producer.send(message);
            println("Sended message");
            _producer.send(_session.createMessage());
        } catch (JMSException e) {
            System.err.println("publishMessages: Exception occurred: " + e.toString());
        }
    }

    /**
     * Closes the connection.
     */
    public void finish() {
        if (_connection != null) {
            try {
                println("Closing..");
                _connection.close();
            } catch (JMSException e) {
            }
        }
    }

    @Override
    public void run() {
        println("Started");
        publishMessages();
        finish();
    }

    /**
 	 * Scrive a video il messaggio con un determinato formato
 	 * @param message � il messaggio da scrivere a video
 	 */
    private void println(String message) {
        System.out.println("SendNewsletterAgent: " + message);
    }
}
