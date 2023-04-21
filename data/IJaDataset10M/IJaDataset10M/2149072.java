package ca.uhn.hl7v2.protocol.impl;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueReceiver;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import ca.uhn.hl7v2.protocol.TransportException;

/**
 * A <code>TransportLayer</code> that uses a JMS Queue.
 *   
 * @author <a href="mailto:bryan.tripp@uhn.on.ca">Bryan Tripp</a>
 * @version $Revision: 1.1 $ updated on $Date: 2007-02-19 02:24:26 $ by $Author: jamesagnew $
 */
public class JMSQueueTransport extends AbstractJMSTransport {

    private QueueSession mySendingSession;

    private QueueSession myReceivingSession;

    private QueueSender mySender;

    private QueueReceiver myReceiver;

    private QueueConnection myConnection;

    private Queue myQueue;

    /**
     * @param theConnection
     * @param theDestination
     */
    public JMSQueueTransport(QueueConnection theConnection, Queue theDestination) {
        myConnection = theConnection;
        myQueue = theDestination;
    }

    /**
     * @see ca.uhn.hl7v2.protocol.impl.AbstractJMSTransport#getDestinationName()
     */
    protected String getDestinationName() throws JMSException {
        return myQueue.getQueueName();
    }

    /**
     * @see ca.uhn.hl7v2.protocol.impl.AbstractJMSTransport#getConnection()
     */
    public Connection getConnection() {
        return myConnection;
    }

    /**
     * @see ca.uhn.hl7v2.protocol.impl.AbstractJMSTransport#getMessage()
     */
    protected Message getMessage() throws JMSException {
        return mySendingSession.createTextMessage();
    }

    /** 
     * @see ca.uhn.hl7v2.protocol.impl.AbstractJMSTransport#send(javax.jms.Message)
     */
    protected void sendJMS(Message theMessage) throws JMSException {
        mySender.send(theMessage);
    }

    /**
     * @see ca.uhn.hl7v2.protocol.impl.AbstractJMSTransport#receive()
     */
    protected Message receiveJMS() throws JMSException {
        return myReceiver.receive();
    }

    /** 
     * @see ca.uhn.hl7v2.protocol.impl.AbstractJMSTransport#connect()
     */
    public void doConnect() throws TransportException {
        boolean transacted = false;
        int ackMode = Session.AUTO_ACKNOWLEDGE;
        doDisconnect();
        try {
            mySendingSession = myConnection.createQueueSession(transacted, ackMode);
            mySender = mySendingSession.createSender(myQueue);
            myReceivingSession = myConnection.createQueueSession(transacted, ackMode);
            myReceiver = myReceivingSession.createReceiver(myQueue);
        } catch (JMSException e) {
            throw new TransportException(e);
        }
    }

    /**
     * @see ca.uhn.hl7v2.protocol.impl.AbstractTransport#doDisconnect()
     */
    public void doDisconnect() throws TransportException {
        try {
            if (mySendingSession != null) {
                mySendingSession.close();
            }
            if (myReceivingSession != null) {
                myReceivingSession.close();
            }
        } catch (JMSException e) {
            throw new TransportException(e);
        }
    }
}
