package com.jawise.sb2jmsbridge;

import javax.jms.Connection;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.QueueConnectionFactory;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.sql.DataSource;
import org.apache.log4j.Logger;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

class Jms2SbExchanger extends MessageExchanger implements MessageListener, ExceptionListener {

    protected static Logger logger = Logger.getLogger(Sb2JmsExchanger.class);

    protected DataSource dSource;

    protected Connection qConnection;

    protected Session qSession;

    protected Queue queue;

    protected MessageConsumer messageconsumer;

    public Jms2SbExchanger(SbQueueConfiguration sbsconfiguration, JmsQueueConfiguration jmsconfiguration, DataSource dSource) {
        super(sbsconfiguration, jmsconfiguration);
        this.dSource = dSource;
    }

    @Override
    public void startExchange() throws MessageExchangerException {
        try {
            connect();
        } catch (JMSException ex) {
            setLastexception(ex);
            throw new MessageExchangerException(ex);
        }
    }

    @Override
    public void stopExchange() {
        setRunning(false);
        super.stopExchange();
        disconnect();
    }

    protected void connect() throws JMSException {
        logger.debug("connecting to jms provider");
        Sb2JmsBridge sbsJmsBridge = Sb2JmsBridge.getInstance();
        QueueConnectionFactory qConnectionFactory = sbsJmsBridge.getQueueConnectionFactory();
        qConnection = qConnectionFactory.createQueueConnection();
        qSession = qConnection.createSession(true, 0);
        queue = qSession.createQueue(getJmsconfiguration().getQueue());
        qConnection.start();
        messageconsumer = qSession.createConsumer(queue);
        messageconsumer.setMessageListener(this);
        qConnection.setExceptionListener(this);
        setRunning(true);
    }

    protected void disconnect() {
        logger.debug("disconnecting from jms provider");
        try {
            if (qConnection != null) {
                qConnection.stop();
            }
            if (messageconsumer != null) {
                messageconsumer.setMessageListener(null);
                messageconsumer.close();
            }
            if (qSession != null) {
                qSession.close();
            }
            if (qConnection != null) {
                qConnection.stop();
                qConnection.close();
            }
        } catch (Exception ex) {
            logger.debug(ex.getMessage());
        }
    }

    @Override
    public void run() {
    }

    @Override
    public void onMessage(Message m) {
        DefaultTransactionDefinition txdef = null;
        Sb2JmsBridge instance = Sb2JmsBridge.getInstance();
        DataSourceTransactionManager txManneger = null;
        TransactionStatus txstatus = null;
        try {
            txdef = new DefaultTransactionDefinition();
            txManneger = instance.getLocalTransactionManager();
            txstatus = txManneger.getTransaction(txdef);
            SbMessageSender sender = new SbMessageSender(dSource, getSbconfiguaration());
            SbMessage msg = createSbMessage((TextMessage) m);
            sender.send(msg);
            txManneger.commit(txstatus);
            qSession.commit();
        } catch (Exception ex) {
            logger.debug("processing failed exchange");
            setLastexception(ex);
            try {
                if (qSession != null) {
                    qSession.rollback();
                }
                if (txstatus.isNewTransaction()) txManneger.rollback(txstatus);
            } catch (Exception ex1) {
                logger.error(ex1.getMessage());
            }
            logger.error(ex.getMessage(), ex);
        }
    }

    public SbMessage createSbMessage(Message m) throws JMSException {
        SbMessage sbsmsg = new SbMessage();
        sbsmsg.setEndconversation(m.getStringProperty("endConversation"));
        sbsmsg.setMessagetypename(m.getStringProperty("messagetypename"));
        sbsmsg.setConversationhandle(m.getStringProperty("conversationhandle"));
        sbsmsg.setConversationgroup(m.getStringProperty("conversationgroup"));
        sbsmsg.setMessagebody(((TextMessage) m).getText());
        return sbsmsg;
    }

    @Override
    public void onException(JMSException ex) {
        logger.error(ex.getMessage());
        setRunning(false);
        setLastexception(ex);
    }
}
