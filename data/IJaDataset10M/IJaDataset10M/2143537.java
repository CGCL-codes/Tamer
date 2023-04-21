package org.commsuite.devices;

import java.util.List;
import javolution.util.FastTable;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.log4j.Logger;
import org.commsuite.enums.FormatType;
import org.commsuite.managers.ContentsManager;
import org.commsuite.managers.SentContentManager;
import org.commsuite.model.Contents;
import org.commsuite.model.Message;
import org.commsuite.model.SentContent;
import org.commsuite.util.RandomGUID;

/**
 * Device for sending/receiving messages.
 * 
 * To send message use createOutboundMessage and fill fields of created object, then use send
 * method. To receive messages add ReceiveListener to object.
 * 
 * @since 1.0
 * @author Rafał Malinowski
 * @author Marcin Zduniak
 */
public abstract class Device {

    private static final Logger logger = Logger.getLogger(Device.class);

    private SentContentManager sentContentManager;

    private ContentsManager contentsManager;

    /**
     * States of device.
     */
    public enum State {

        OFF, INITIALIZING, ON
    }

    /**
     * {@link State#OFF} by default.
     */
    private State state = State.OFF;

    private long messagesSent;

    private long messagesReceived;

    private List<ReceiveListener> receiveListeners;

    private List<SendStateChangeListener> sendStateChangeListeners;

    /**
     * unique name of device: fax001, fax002...
     */
    private final String name;

    /**
     * Creates new device.
     * 
     * @param name unique name of device.
     */
    protected Device(String name) {
        this.name = name;
        receiveListeners = new FastTable<ReceiveListener>();
        sendStateChangeListeners = new FastTable<SendStateChangeListener>();
    }

    public SentContentManager getSentContentManager() {
        return sentContentManager;
    }

    public void setSentContentManager(SentContentManager sentContentManager) {
        this.sentContentManager = sentContentManager;
    }

    public ContentsManager getContentsManager() {
        return contentsManager;
    }

    public void setContentsManager(ContentsManager contentsManager) {
        this.contentsManager = contentsManager;
    }

    /**
     * Initialize device. Returns true if operation succesed.
     * 
     * @throws DeviceInitializationFailedException if device initialization failed
     */
    public abstract void init() throws DeviceInitializationFailedException;

    /**
     * Shutdowns device. Returns true if operation succesed.
     * 
     * @throws DeviceShutdownFailedException if device shutdown failed
     */
    public abstract void shutdown() throws DeviceShutdownFailedException;

    /**
     * Creates OutboundMessage for this device.
     */
    public abstract OutboundMessage createOutboundMessage();

    /**
     * Sends message to receiver.
     * 
     * @throws DeviceInvalidOutboundMessageException if message was created by another device
     * @throws OutboundMessageInvalidContentMimeTypeException if device cannot send messages with gived mime type
     * @throws OutboundMessageInvalidContentException if device cannot send messages with given content
     * @throws OutboundMessageInvalidDestinationAddressException if message cannot send messages to given destination
     * @throws OutboundMessageSendException if any error occured during sending mesages
     * @throws OutboundMessageConversionFailedException if any error occured dugins conversion of message content to acceptable format
     */
    public abstract void send(OutboundMessage message) throws DeviceInvalidOutboundMessageException, OutboundMessageInvalidContentMimeTypeException, OutboundMessageInvalidContentException, OutboundMessageInvalidDestinationAddressException, OutboundMessageSendException, OutboundMessageConversionFailedException;

    /**
     * Sends message to receiver
     * 
     * REFACTOR: wydaje mi sie, ze zarowno ta metoda jak i nastepna powinny znaleźć się w jakiejś
     * klasie proxy między klasą Device a interfejsem SAP-CS. Po prostu nie wydaje mi się, aby to
     * była dobra enkapsulacja.
     * 
     * @param message message to send
     */
    public void send(Message message) {
        final List<SentContent> sentContents = message.getSentContents();
        logger.debug("message sap id is: " + message.getSapInstanceDefOwner());
        for (final SentContent sentContent : sentContents) {
            sendSimpleMessage(sentContent, message.getReceiver());
        }
    }

    /**
     * Sends simple message to receiver. Saves send status into database.
     * 
     * @param sentContent sentContent to send
     * @param receiver destination address
     */
    private void sendSimpleMessage(SentContent sentContent, String receiver) {
        final Contents content = sentContent.getContent();
        OutboundMessage outboundMessage = null;
        try {
            outboundMessage = createOutboundMessage();
            outboundMessage.setContentMimeType(content.getMimeType());
            outboundMessage.setContent(content.getData());
            outboundMessage.setDestinationAddress(receiver);
            send(outboundMessage);
            messagesSent++;
        } catch (DeviceInvalidOutboundMessageException e) {
            logger.fatal("", e);
        } catch (OutboundMessageInvalidContentMimeTypeException e) {
            content.setDescription("Invalid mime type: " + e.getMessage());
            logger.error("", e);
        } catch (OutboundMessageInvalidContentException e) {
            content.setDescription("Invalid content: " + e.getMessage());
            logger.error("", e);
        } catch (OutboundMessageInvalidDestinationAddressException e) {
            content.setDescription(("Invalid destination address: " + e.getMessage()));
            logger.error("", e);
        } catch (OutboundMessageSendException e) {
            content.setDescription("Could not send: " + e.getMessage());
            logger.error("", e);
        } catch (OutboundMessageConversionFailedException e) {
            content.setDescription("Conversion failed: " + e.getMessage());
            logger.error("", e);
        }
        contentsManager.saveContents(content);
        if (null != outboundMessage && null != outboundMessage.getMessageId()) {
            final String internalId = getInternallId(outboundMessage);
            final SentContent prevSentContents = sentContentManager.getSentContentByInternalId(internalId);
            if (null != prevSentContents) {
                prevSentContents.setInternalId(RandomGUID.getGUID());
                sentContentManager.saveSentContent(prevSentContents);
            }
            sentContent.setTryNumber(sentContent.getTryNumber() + 1);
            sentContent.setInternalId(internalId);
            sentContent.setState(org.commsuite.enums.State.SENT);
            sentContentManager.saveSentContent(sentContent);
            notifySendStateChange(outboundMessage.getMessageId(), OutboundMessage.State.SUBMITED);
        } else {
            final String messageId = RandomGUID.getGUID();
            final String internalId = getInternallId(false, messageId);
            sentContent.setTryNumber(sentContent.getTryNumber() + 1);
            sentContent.setInternalId(internalId);
            sentContent.setState(org.commsuite.enums.State.FAILED);
            sentContentManager.saveSentContent(sentContent);
            notifySendStateChange(messageId, OutboundMessage.State.FAILED);
        }
    }

    /**
     * Returns type of data supported by device (sms data, fax data...)
     * 
     * @return type of data supported by device
     */
    public abstract FormatType getFormatType();

    /**
     * Adds new listener of receiving messages.
     * 
     * @param receiveListener new receive listener
     */
    public void addReceiveListener(ReceiveListener receiveListener) {
        synchronized (receiveListeners) {
            receiveListeners.add(receiveListener);
        }
    }

    /**
     * Removes listener of receiving messages.
     * 
     * @param receiveListener receive listener to remove
     */
    public void removeReceiveListener(ReceiveListener receiveListener) {
        synchronized (receiveListeners) {
            receiveListeners.remove(receiveListener);
        }
    }

    /**
     * Adds new listener of chaning outbound message state.
     * 
     * @param sendStateChangeListener new sent state change listener
     */
    public void addSendStateChangeListener(SendStateChangeListener sendStateChangeListener) {
        synchronized (sendStateChangeListeners) {
            sendStateChangeListeners.add(sendStateChangeListener);
        }
    }

    /**
     * Removes listener ofchaning outbound message state.
     * 
     * @param sent state change listener to remove
     */
    public void removeSendStateChangeListener(SendStateChangeListener sendStateChangeListener) {
        synchronized (sendStateChangeListeners) {
            sendStateChangeListeners.remove(sendStateChangeListener);
        }
    }

    /**
     * Sets state of device.
     * 
     * @param state new state of device
     */
    protected void setState(State state) {
        this.state = state;
    }

    /**
     * Returns state of device.
     * 
     * @return state of device
     */
    public State getState() {
        return state;
    }

    /**
     * Stub. Return unique name of device. It will be something like "Fax0001" "Fax0002".
     * 
     * @return unique name of device
     */
    public String getName() {
        return name;
    }

    /**
     * Returns system-unique id of message with messageId. It consists of name of device, type of
     * message and message id.
     * 
     * @param inbound true if message is inbound, false if it is outbound
     * @param messageId messageId to convert
     * @return System unique message id.
     */
    public String getInternallId(boolean inbound, String messageId) {
        if (inbound) {
            return name + ":i:" + messageId;
        } else {
            return name + ":o:" + messageId;
        }
    }

    /**
     * Returns system-unique id of message. It consists of name of device, type of message and
     * message id.
     * 
     * @param message
     * @return System unique message id.
     */
    public String getInternallId(OutboundMessage message) {
        return getInternallId(false, message.getMessageId());
    }

    /**
     * Returns system-unique id of message. It consists of name of device, type of message and
     * message id.
     * 
     * @param message
     * @return System unique message id.
     */
    public String getMessageId(InboundMessage message) {
        return getInternallId(true, message.getMessageId());
    }

    /**
     * If notifications says that message failed and it was send less that 3 times try to sent it one more time.
     * Else, notifies all listeners about SendStateChange event.
     * 
     * @param messageId messageId of message whose send state changes
     * @param state current state of message
     */
    public void notifySendStateChange(String messageId, OutboundMessage.State state) {
        logger.debug("send state changed");
        final String internalId = getInternallId(false, messageId);
        logger.info(internalId);
        if (state == OutboundMessage.State.FAILED) {
            SentContent sentContent = sentContentManager.getSentContentByInternalId(internalId);
            if (null != sentContent) {
                if (null != sentContent.getMessage()) {
                    if (sentContent.getTryNumber() < 3) {
                        sendSimpleMessage(sentContent, sentContent.getMessage().getReceiver());
                        return;
                    }
                }
            }
        }
        synchronized (sendStateChangeListeners) {
            for (SendStateChangeListener listener : sendStateChangeListeners) {
                listener.onSendStateChange(internalId, state);
            }
        }
    }

    /**
     * Notifies all listeners about Receive event.
     * 
     * @param message received message
     */
    public void notifyReceive(InboundMessage message) {
        logger.debug("Message received from " + message.getSourceAddress() + " to " + message.getDestinationAddress());
        messagesReceived++;
        synchronized (receiveListeners) {
            for (ReceiveListener rl : receiveListeners) {
                rl.onReceive(message);
            }
        }
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this).append("name", this.name).append("state", this.state).append("messagesReceived", this.messagesReceived).append("messagesSent", this.messagesSent).append("formatType", this.getFormatType()).toString();
    }
}
