package fr.xebia.sample.springframework.jms.requestreply;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import org.apache.log4j.Logger;
import org.springframework.core.style.ToStringCreator;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.JmsUtils;
import org.springframework.util.Assert;

/**
 * <p>
 * SpringFramework {@link JmsTemplate} based Request/Reply client invoker.
 * </p>
 * <p>
 * Note that this request-reply does not support temporary queues because temporary queues lifetime does not fit well with
 * {@link JmsTemplate}'s JCA based approach . Indeed, temporary queues have the lifetime of a connections when JMSTemplate eagerly closes
 * resources (Connection, Session, MessageProducer and MessageConsumer ) and is aimed to be used with an underlying JCA connector to pool
 * all these resources.
 * </p>
 * <p>
 * Extract from JMS 1.1 Specification :
 * </p>
 * <blockquote><strong> 4.4.3 Creating Temporary Destinations </strong> <br/> Although sessions are used to create temporary destinations,
 * this is only for convenience. Their scope is actually the entire connection. Their lifetime is that of their connection, and any of the
 * connection's sessions is allowed to create a MessageConsumer for them. </blockquote>
 * 
 * @author <a href="mailto:cyrille.leclerc@pobox.com">Cyrille Le Clerc</a>
 */
public class RequestReplyClientInvoker {

    private static final Logger logger = Logger.getLogger(RequestReplyClientInvoker.class);

    protected Destination replyToDestination;

    protected ConnectionFactory connectionFactory;

    protected Destination requestDestination;

    protected long timeoutInMillis = 1000;

    /**
     * Request/Reply SpringFramework sample.
     * 
     * @param request
     *            sent to the remote service
     * @return reply returned by the remote service
     * @throws JMSException
     */
    public String requestReply(String request) throws JMSException {
        Connection connection = connectionFactory.createConnection();
        try {
            connection.start();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            try {
                MessageProducer messageProducer = session.createProducer(this.requestDestination);
                try {
                    Message requestMessage = session.createTextMessage(request);
                    requestMessage.setJMSReplyTo(this.replyToDestination);
                    messageProducer.send(requestMessage);
                    String messageSelector = "JMSCorrelationID  LIKE '" + requestMessage.getJMSMessageID() + "'";
                    MessageConsumer messageConsumer = session.createConsumer(this.replyToDestination, messageSelector);
                    TextMessage replyMessage = (TextMessage) messageConsumer.receive(timeoutInMillis);
                    Assert.notNull(replyMessage, "Timeout waiting for jms response");
                    logger.debug("requestReply " + "\r\nrequest : " + requestMessage + "\r\nreply : " + replyMessage);
                    String result = replyMessage.getText();
                    logger.debug("requestReply('" + request + "'): '" + result + "'");
                    return result;
                } finally {
                    JmsUtils.closeMessageProducer(messageProducer);
                }
            } finally {
                JmsUtils.closeSession(session);
            }
        } finally {
            JmsUtils.closeConnection(connection);
        }
    }

    public void setConnectionFactory(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public void setReplyToDestination(Destination replyToDestination) {
        this.replyToDestination = replyToDestination;
    }

    public void setRequestDestination(Destination requestDestination) {
        this.requestDestination = requestDestination;
    }

    public void setTimeoutInMillis(long timeoutInMillis) {
        this.timeoutInMillis = timeoutInMillis;
    }

    @Override
    public String toString() {
        return new ToStringCreator(this).append("connectionFactory", this.connectionFactory).append("requestDestination", this.requestDestination).append("replyToDestination", this.replyToDestination).toString();
    }
}
