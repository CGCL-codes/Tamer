package org.mule.providers.sms.backends.smslib;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mule.providers.sms.SmsTextMessage;
import org.mule.providers.sms.backends.SmsReceiver;
import org.smslib.AGateway;
import org.smslib.IInboundMessageNotification;
import org.smslib.InboundMessage;
import org.smslib.MessageClasses;
import org.smslib.MessageTypes;
import org.smslib.Service;

/**
 * @author Jeroen Benckhuijsen (jeroen.benckhuijsen@gmail.com)
 * @author Dennis Vredeveld
 *
 */
public class SmslibReceiver implements IInboundMessageNotification, Runnable {

    public static final long NO_POLLING = -1;

    private static Log logger = LogFactory.getLog(SmslibReceiver.class);

    private Service service;

    private AGateway gateway;

    private SmsReceiver receiver;

    private boolean connected = false;

    private long timeout;

    /**
	 * @param service
	 * @param gateway
	 * @param receiver
	 * @param timeout
	 */
    public SmslibReceiver(Service service, AGateway gateway, SmsReceiver receiver, long timeout) {
        this.service = service;
        this.gateway = gateway;
        this.receiver = receiver;
        this.timeout = timeout;
    }

    public void connect() {
        if (connected) {
            return;
        }
        connected = true;
        if (timeout != NO_POLLING) {
            new Thread(this).start();
        } else {
            if (gateway.getInboundNotification() != null) {
                throw new IllegalStateException("A receiver is already registered for this gateway");
            }
            gateway.setInboundNotification(this);
        }
    }

    public void disconnect() {
        if (connected) {
            if (timeout == NO_POLLING) {
                gateway.setInboundNotification(null);
            }
            connected = false;
        }
    }

    /**
	 * @param message
	 */
    private void handleMessage(InboundMessage message) {
        SmsTextMessage result = new SmsTextMessage();
        result.setMessage(message.getText());
        result.setReceiveDate(message.getDate());
        result.setSender(message.getOriginator());
        if (logger.isDebugEnabled()) {
            logger.debug("Received an SMS Message:" + result.toString());
        }
        this.receiver.receiveMessage(result);
    }

    @Override
    public void process(String gatewayId, MessageTypes msgType, String memLoc, int memIndex) {
        try {
            InboundMessage message = service.readMessage(gatewayId, memLoc, memIndex);
            if (MessageTypes.INBOUND.equals(message.getType())) {
                handleMessage(message);
            }
        } catch (InterruptedException e) {
            logger.info("SMS message receiver was interrupted");
        } catch (Exception e) {
            logger.error("Error while receiving an SMS message", e);
        }
    }

    @Override
    public void run() {
        while (connected) {
            List<InboundMessage> msgList = new ArrayList<InboundMessage>();
            try {
                gateway.readMessages(msgList, MessageClasses.ALL);
                for (InboundMessage message : msgList) {
                    handleMessage(message);
                    gateway.deleteMessage(message);
                }
                Thread.sleep(timeout);
            } catch (InterruptedException e) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Ignoring InterruptedException", e);
                }
            } catch (Exception e) {
                logger.error("Exception while polling for new SMS messages", e);
            }
        }
    }
}
