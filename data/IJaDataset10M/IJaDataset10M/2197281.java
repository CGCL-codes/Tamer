package amqpgw.core;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicSession;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.qpid.transport.Connection;
import org.apache.qpid.transport.DeliveryProperties;
import org.apache.qpid.transport.Header;
import org.apache.qpid.transport.MessageAcceptMode;
import org.apache.qpid.transport.MessageAcquireMode;
import org.apache.qpid.transport.Session;

/**
 * Contains more of that magic.
 * 
 * @author tmfrei
 *
 */
public class J2ATopicConverter extends GatewayComponent implements MessageListener {

    private Log log;

    private Connection amqconn;

    private Session amqsess;

    private DeliveryProperties amqdp;

    private TopicConnection jmsconn;

    private TopicSession jmssess;

    private Topic jmsname;

    private String name;

    public J2ATopicConverter(String name) {
        log = LogFactory.getLog(J2ATopicConverter.class);
        this.name = name;
        amqdp = new DeliveryProperties();
        amqdp.setRoutingKey(name + ".amqpgw");
    }

    @Override
    public String amqAttrsToString() {
        return "routing key=" + name;
    }

    /**
	 * 
	 * @return	<tt>null</tt> if not added to the composite.
	 */
    @Override
    public String amqConnectionToString() {
        return getParent().amqConnectionToString();
    }

    @Override
    public long getAcknowledged() {
        return 0;
    }

    @Override
    public String getGroup() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void getNodeAttrs() {
    }

    @Override
    public NodeTag getNodeTag() {
        return NodeTag.J2ATOPICCONVERTER;
    }

    @Override
    public long getReceived() {
        return 0;
    }

    @Override
    public long getSent() {
        return 0;
    }

    @Override
    public State getState() {
        return null;
    }

    @Override
    public String getType() {
        return null;
    }

    @Override
    void setState(State state) {
    }

    @Override
    public synchronized void start() {
        amqconn = ((Connector) getParent()).getAMQConnection();
        jmsconn = (TopicConnection) ((Connector) getParent()).getJMSConnection();
        amqsess = amqconn.createSession(0);
        log.info("AMQ producer session created");
        try {
            jmssess = jmsconn.createTopicSession(false, javax.jms.Session.AUTO_ACKNOWLEDGE);
            jmsname = (Topic) new InitialContext().lookup(name);
            jmssess.createSubscriber(jmsname).setMessageListener(this);
            log.info("JMS consumer session created for topic " + name);
        } catch (NamingException e) {
            log.error("JNDI API lookup failed", e);
        } catch (JMSException e) {
            log.error("JMS problem in start method", e);
        }
    }

    @Override
    public synchronized void stop() {
        amqsess.close();
        log.info("AMQ producer session closed");
        try {
            jmssess.close();
            log.info("JMS consumer session closed for topic " + name);
        } catch (JMSException e) {
            log.error("JMS problem in start method", e);
        }
    }

    public synchronized void onMessage(Message m) {
        if (m instanceof TextMessage) {
            try {
                System.err.println("received " + ((TextMessage) m).getText());
                amqsess.messageTransfer("amq.topic", MessageAcceptMode.EXPLICIT, MessageAcquireMode.PRE_ACQUIRED, new Header(amqdp), ((TextMessage) m).getText());
                amqsess.sync();
            } catch (JMSException e) {
                log.fatal("How to bloody recover from this?", e);
            }
        } else {
            log.warn("no handler for " + m);
        }
    }
}
