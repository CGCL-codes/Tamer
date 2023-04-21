package com.sonic.jms.processor;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJBException;
import javax.ejb.MessageDriven;
import javax.ejb.MessageDrivenContext;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.jms.BytesMessage;
import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import com.sonic.jms.base.MQuotesMsgProcessorBase;
import com.sonic.jms.exceptions.MQuotesMapperException;
import com.sonic.jms.listeners.MPapExceptionListener;
import com.sonic.log.ejb.impl.MQLogger;
import com.sonic.mquotes.dto.MqPapMessage;

/** 
 * MPap processor message consumer class.<br>
 * There is P2P communication with ESB and 1-many with topic consumers.
 * 
 * @author <b>Adam Dec</b> for <b><I>iBroker</i></b><br> Copyright &#169; 2008/2009
 * @since 25-09-2008
 * @category BROKER PROCESSOR MDB
 * @version 1.0
 */
@MessageDriven(activationConfig = { @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"), @ActivationConfigProperty(propertyName = "acknoweledgeMode", propertyValue = "Auto-acknoweledge") }, mappedName = "Jms.queue.mquotes.MobilePAPQueue", name = "MPapMsgProcessor", description = "P2P communication with ESB")
public class MPapMsgProcessor extends MQuotesMsgProcessorBase implements MessageListener {

    public static MQLogger logger = new MQLogger(MPapMsgProcessor.class, "MQ-PAP-QUEUE");

    private static final String TOPIC_CONNECTION_FACTORY_NAME = "TopicConnectionFactory";

    private static final String TOPIC_PAP_NAME = "Jms.topic.mquotes.MobilePAPTopic";

    private TopicConnectionFactory topicConnectionFactory = null;

    private Topic wlsPapTopic = null;

    @SuppressWarnings("unused")
    @Resource
    private MessageDrivenContext messageDrivenContext;

    private TopicConnection topicConnection = null;

    private TopicSession topicQuoteSession = null;

    private TopicPublisher topicQuotePublisher = null;

    private BytesMessage bytesMessage = null;

    private static final String CLIENT_ID = "queuePapClientId.";

    private boolean logSending = true;

    private ByteArrayOutputStream bout = new ByteArrayOutputStream();

    ;

    private DataOutputStream dout = new DataOutputStream(bout);

    private Object object = null;

    private static int counter = 0;

    @PostConstruct
    public void init() {
        final String methodName = "init()";
        logger.debug(methodName, " --- [INFO] Creating MPapMsgProcessor MDB...");
        try {
            final InitialContext ctx = super.getContext();
            this.topicConnectionFactory = (TopicConnectionFactory) ctx.lookup(TOPIC_CONNECTION_FACTORY_NAME);
            if (this.topicConnection != null) this.topicConnection.close();
            this.topicConnection = super.createTopicConnection(this.topicConnectionFactory);
            this.topicConnection.setClientID(CLIENT_ID + System.currentTimeMillis());
            this.topicConnection.setExceptionListener(new MPapExceptionListener());
            this.topicQuoteSession = super.createTopicSession(this.topicConnection);
            this.wlsPapTopic = (Topic) ctx.lookup(TOPIC_PAP_NAME);
            this.topicQuotePublisher = super.createTopicPublisher(this.topicQuoteSession, this.wlsPapTopic);
            this.topicQuotePublisher.setPriority(6);
            this.topicQuotePublisher.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
            logger.debug(methodName, " --- [INFO] MPapMsgProcessor MDB initialization successfull!");
        } catch (JMSException e) {
            logger.debug(methodName, " --- [ERROR] MPapMsgProcessor MDB initialization failure!");
            logger.error(methodName, Integer.parseInt(e.getErrorCode()), e.getMessage());
        } catch (NamingException e) {
            logger.debug(methodName, " --- [ERROR] MQuotesMsgProcessor MDB initialization failure!");
            logger.debug(methodName, e.getMessage());
        }
        try {
            if (this.topicConnection != null) {
                this.topicConnection.start();
                logger.debug(methodName, " --- [INFO] MPapMsgProcessor MDB topic conncetion started.");
            }
        } catch (JMSException e) {
            logger.debug(methodName, " --- [ERROR] MPapMsgProcessor MDB topic conncetion has not been started!");
            logger.error(methodName, Integer.parseInt(e.getErrorCode()), e.getMessage());
            throw new EJBException(e);
        }
        try {
            if (this.topicQuoteSession != null) {
                bytesMessage = topicQuoteSession.createBytesMessage();
                logger.debug(methodName, " --- [INFO] MPapMsgProcessor MDB bytes message has been created!");
            }
        } catch (JMSException e) {
            logger.debug(methodName, " --- [ERROR] MPapMsgProcessor MDB bytes message has not been created!");
            logger.error(methodName, Integer.parseInt(e.getErrorCode()), e.getMessage());
            throw new EJBException(e);
        }
        logger.debug(methodName, " --- [INFO] MPapMsgProcessor MDB initialization complete.");
    }

    @PreDestroy
    public void destroy() {
        final String methodName = "destroy()";
        logger.debug(methodName, " --- [INFO] Destroying MPapMsgProcessor MDB...");
        try {
            if (this.topicConnection != null) {
                this.topicConnection.stop();
                logger.debug(methodName, " --- [INFO] TopicConnection has been succesfully stopped.");
            }
        } catch (JMSException e) {
            logger.debug(methodName, " --- [ERROR] TopicConnection has not been succesfully stopped!");
            logger.error(methodName, Integer.parseInt(e.getErrorCode()), e.getMessage());
        }
        try {
            if (this.topicQuoteSession != null) {
                this.topicQuoteSession.close();
                logger.debug(methodName, " --- [INFO] TopicSession [topicPapSession] has been succesfully closed.");
            }
        } catch (JMSException e) {
            logger.debug(methodName, " --- [ERROR] TopicSession [topicPapSession]has not been succesfully closed!");
            logger.error(methodName, Integer.parseInt(e.getErrorCode()), e.getMessage());
        }
        logger.debug(methodName, " --- [INFO] MPapMsgProcessor MDB has been destroyed.");
    }

    public MPapMsgProcessor() {
        super();
        final String methodName = "MPapMsgProcessor()";
        logger.debug(methodName, " --- [INFO] Initializing MPapMsgProcessor class.");
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public void onMessage(Message message) {
        final String methodName = "onMessage()";
        try {
            try {
                this.object = super.readObjectAsXmlFrom(((TextMessage) message).getText().getBytes("UTF-8"));
            } catch (UnsupportedEncodingException e) {
                logger.debug(methodName, " --- [ERROR] Error converting TextMessage to byte array using UTF-8 encoding, maybe there is wrong XML encoding!");
                logger.debug(methodName, e.getMessage());
                throw new EJBException(e);
            } catch (MQuotesMapperException e) {
                logger.debug(methodName, " --- [ERROR] Error converting TextMessage to byte array using UTF-8 encoding, maybe there are problems with unmarshall!");
                logger.debug(methodName, e.getMessage());
                throw new EJBException(e);
            }
            try {
                ((MqPapMessage) object).write(dout);
            } catch (IOException e) {
                logger.debug(methodName, " --- [ERROR] Data serialization exception: " + e.getMessage());
            } finally {
                try {
                    if (dout != null) {
                        dout.flush();
                        dout.close();
                    }
                    if (bout != null) {
                        bout.flush();
                        bout.close();
                    }
                } catch (IOException e) {
                    logger.debug(methodName, " --- [ERROR] Data stream close error: " + e.getMessage());
                }
            }
            this.bytesMessage.writeBytes(bout.toByteArray());
            this.bytesMessage.setIntProperty("MSG_LENGTH", bout.toByteArray().length);
            this.topicQuotePublisher.publish(bytesMessage);
            this.bytesMessage.clearBody();
            this.bytesMessage.clearProperties();
            if (bout != null) bout.reset();
            System.out.println("Pap message: " + (counter++));
        } catch (JMSException e) {
            try {
                logger.debug(methodName, " --- Error getting data from JMS message, type: " + message.getJMSCorrelationID() + " id: " + ((TextMessage) message).getJMSMessageID());
            } catch (JMSException e1) {
            }
            logger.error(methodName, Integer.parseInt(e.getErrorCode() != null ? e.getErrorCode() : "-1"), e.getMessage());
            throw new EJBException(e);
        }
    }
}
