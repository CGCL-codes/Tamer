package hermes;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;

/**
 * @author colincrist@hermesjms.com
 * @version $Id: NullConnectionFactory.java,v 1.2 2004/05/08 15:15:48 colincrist
 *          Exp $
 */
public class NullConnectionFactory implements TopicConnectionFactory, QueueConnectionFactory {

    public TopicConnection createTopicConnection() throws JMSException {
        throw new JMSException("You must select a real ConnectionFactory for this provider");
    }

    public TopicConnection createTopicConnection(String arg0, String arg1) throws JMSException {
        throw new JMSException("You must select a real ConnectionFactory for this provider");
    }

    public QueueConnection createQueueConnection() throws JMSException {
        throw new JMSException("You must select a real ConnectionFactory for this provider");
    }

    public QueueConnection createQueueConnection(String arg0, String arg1) throws JMSException {
        throw new JMSException("You must select a real ConnectionFactory for this provider");
    }

    public Connection createConnection() throws JMSException {
        throw new JMSException("You must select a real ConnectionFactory for this provider");
    }

    public Connection createConnection(String arg0, String arg1) throws JMSException {
        throw new JMSException("You must select a real ConnectionFactory for this provider");
    }
}
