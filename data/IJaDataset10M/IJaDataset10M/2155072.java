package lt.baltic_amadeus.jqbridge.providers.jms;

import java.io.ByteArrayOutputStream;
import javax.jms.BytesMessage;
import javax.jms.DeliveryMode;
import javax.jms.InvalidDestinationException;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueReceiver;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import lt.baltic_amadeus.jqbridge.msg.BridgeDestination;
import lt.baltic_amadeus.jqbridge.msg.BridgePersistence;
import lt.baltic_amadeus.jqbridge.msg.BridgeMsg;
import lt.baltic_amadeus.jqbridge.msg.MsgHdr;
import lt.baltic_amadeus.jqbridge.providers.ChannelHandler;
import lt.baltic_amadeus.jqbridge.server.BridgeException;
import lt.baltic_amadeus.jqbridge.server.CommunicationException;
import lt.baltic_amadeus.jqbridge.server.Endpoint;
import lt.baltic_amadeus.jqbridge.server.MessageLogger;
import lt.baltic_amadeus.jqbridge.server.PortConfigurationException;

/**
 * 
 * @author Baltic Amadeus, JSC
 * @author Antanas Kompanas
 *
 */
public class JMSChannelHandler implements ChannelHandler {

    private JMSPort port;

    private Endpoint endpoint;

    private QueueSession session;

    private Queue queue;

    private Queue replyQueue;

    private QueueSender sender;

    private QueueReceiver receiver;

    private Message lastMessage;

    private MessageLogger msgLog;

    public JMSChannelHandler(JMSPort port, Endpoint ep) throws BridgeException {
        try {
            this.port = port;
            this.endpoint = ep;
            QueueConnection connection = port.getConnection();
            switch(ep.getSide()) {
                case Endpoint.SOURCE:
                    session = connection.createQueueSession(false, Session.CLIENT_ACKNOWLEDGE);
                    queue = session.createQueue(ep.getDestination().getName());
                    receiver = session.createReceiver(queue);
                    break;
                case Endpoint.DESTINATION:
                    session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
                    queue = session.createQueue(ep.getDestination().getName());
                    BridgeDestination replyTo = ep.getReplyTo();
                    if (replyTo != null) replyQueue = session.createQueue(replyTo.getName());
                    sender = session.createSender(queue);
                    break;
                default:
                    throw new IllegalStateException("Unexpected error: endpoint for port " + port.getName() + " is neighter SOURCE nor DESTINATION");
            }
            msgLog = port.getProvider().getMessageLogger();
        } catch (InvalidDestinationException ex) {
            throw new PortConfigurationException(port.getName(), "invalid port destination " + ep.getDestination());
        } catch (javax.jms.IllegalStateException ex) {
            throw new ConnectionDownException(ex);
        } catch (JMSException ex) {
            throw new CommunicationException(port, ex);
        }
    }

    public Endpoint getEndpoint() {
        return endpoint;
    }

    public void sendMessage(BridgeMsg brMsg) throws BridgeException {
        try {
            Message jMsg = createJMSMessage(brMsg);
            if (replyQueue != null) jMsg.setJMSReplyTo(replyQueue);
            sender.send(jMsg);
            brMsg.setMessageId(jMsg.getJMSMessageID());
        } catch (JMSException ex) {
            throw new CommunicationException(this, ex);
        }
    }

    public BridgeMsg receiveMessage() throws BridgeException {
        try {
            port.runFaultyConnection();
            lastMessage = receiver.receive();
            if (lastMessage == null) return null;
            msgLog.logEarlyReceive(lastMessage.getJMSMessageID());
            return createBridgeMsg(lastMessage);
        } catch (JMSException ex) {
            throw new CommunicationException(this, ex);
        }
    }

    public void commit() throws BridgeException {
        try {
            lastMessage.acknowledge();
        } catch (JMSException ex) {
            throw new CommunicationException(this, ex);
        }
    }

    public void rollback() throws BridgeException {
        try {
            session.recover();
        } catch (JMSException ex) {
            throw new CommunicationException(this, ex);
        }
    }

    public void stop() throws BridgeException {
        close();
    }

    public void close() throws BridgeException {
        try {
            session.close();
        } catch (JMSException ex) {
            throw new CommunicationException(this, ex);
        }
    }

    private BridgeMsg createBridgeMsg(Message jMsg) throws BridgeException, JMSException {
        BridgeMsg brMsg = new BridgeMsg();
        brMsg.setMessageId(jMsg.getJMSMessageID());
        brMsg.setPersistence(createPersistence(jMsg.getJMSDeliveryMode()));
        if (jMsg instanceof TextMessage) {
            brMsg.setMsgType(BridgeMsg.TYPE_TEXT);
            brMsg.setTextData(((TextMessage) jMsg).getText());
        } else if (jMsg instanceof BytesMessage) {
            brMsg.setMsgType(BridgeMsg.TYPE_BYTES);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buf = new byte[8192];
            BytesMessage bMsg = (BytesMessage) jMsg;
            for (; ; ) {
                int amount = bMsg.readBytes(buf);
                if (amount <= 0) break;
                out.write(buf, 0, amount);
                if (amount < buf.length) break;
            }
            brMsg.setByteData(out.toByteArray());
        } else {
            brMsg.setMsgType(BridgeMsg.TYPE_NONE);
        }
        return brMsg;
    }

    private Message createJMSMessage(BridgeMsg brMsg) throws JMSException {
        Message jMsg = null;
        switch(brMsg.getMsgType()) {
            case BridgeMsg.TYPE_NONE:
                jMsg = session.createMessage();
                break;
            case BridgeMsg.TYPE_TEXT:
                jMsg = session.createTextMessage(brMsg.getTextData());
                break;
            case BridgeMsg.TYPE_BYTES:
                BytesMessage btsMsg = session.createBytesMessage();
                btsMsg.writeBytes(brMsg.getByteData());
                jMsg = btsMsg;
                break;
            default:
                throw new IllegalArgumentException("Unsupported message type");
        }
        sender.setDeliveryMode(createDeliveryMode(brMsg.getPersistence()));
        jMsg.setStringProperty(MsgHdr.ORIG_MESSAGE_ID, brMsg.getOriginalMessageId());
        return jMsg;
    }

    private BridgePersistence createPersistence(int jmsDeliveryMode) {
        switch(jmsDeliveryMode) {
            case DeliveryMode.PERSISTENT:
                return BridgePersistence.PERSISTENT;
            case DeliveryMode.NON_PERSISTENT:
                return BridgePersistence.NON_PERSISTENT;
            default:
                throw new IllegalArgumentException("DeliveryMode must be eighter PERSISTENT or NON_PERSISTENT");
        }
    }

    private int createDeliveryMode(BridgePersistence persistence) {
        if (persistence == BridgePersistence.PERSISTENT) return DeliveryMode.PERSISTENT; else if (persistence == BridgePersistence.NON_PERSISTENT) return DeliveryMode.NON_PERSISTENT; else throw new IllegalArgumentException("BridgePersistence must be eighter PERSISTENT or NON_PERSISTENT");
    }
}
