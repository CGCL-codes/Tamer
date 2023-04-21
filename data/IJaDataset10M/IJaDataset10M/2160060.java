package edu.ucdavis.genomics.metabolomics.binbase.cluster.status;

import java.util.HashMap;
import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.naming.InitialContext;
import org.apache.log4j.Logger;

public class JMSTopicProvider implements JMSProvider {

    private Logger logger = Logger.getLogger(getClass());

    boolean invalid = false;

    private static JMSProvider instance;

    public static JMSProvider getInstance() {
        if (instance == null) {
            instance = new JMSTopicProvider();
        }
        return instance;
    }

    private JMSTopicProvider() {
    }

    public void sendMessage(HashMap<?, ?> msg) throws JMSException {
        try {
            logger.info("open session...");
            TopicConnection con = null;
            TopicSession sess = null;
            TopicPublisher pub = null;
            InitialContext ctx = new InitialContext();
            TopicConnectionFactory fact = (TopicConnectionFactory) ctx.lookup("TopicConnectionFactory");
            con = fact.createTopicConnection();
            sess = con.createTopicSession(false, TopicSession.AUTO_ACKNOWLEDGE);
            Topic topic = (Topic) ctx.lookup("topic/monitor");
            pub = sess.createPublisher(topic);
            con.start();
            try {
                ObjectMessage message = sess.createObjectMessage();
                message.setObject(msg);
                pub.publish(message);
            } finally {
                pub.close();
                sess.close();
                con.close();
            }
        } catch (Exception e) {
            if (e instanceof JMSException) {
                throw (JMSException) e;
            }
            throw new JMSException(e.getMessage());
        }
    }
}
