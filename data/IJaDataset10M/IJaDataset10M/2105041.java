package org.netbeams.dsp.wiretransport.client.model;

import java.util.UUID;
import org.netbeams.dsp.message.Message;

/**
 * Representation of a given DSP Message in the Messages Queue. It contains references to the DSP message, the URL
 * destination, the state and the messages container identification in which the message will be "wrapped up".
 * 
 * @author Marcello de Sales (marcello.sales@gmail.com)
 * 
 */
public class QueueMessageData {

    /**
     * The destination URL of the component that will receive the message
     */
    private String destinitionIpAddress;

    /**
     * The DSP message to be delivered.
     */
    private Message message;

    /**
     * The message state
     */
    private QueueMessageState state;

    /**
     * The container ID. This value is only set when the message on the state MessagesQueueState.TRANSMITTED and
     * ACKNOWLEDGED.
     */
    private UUID containerId;

    /**
     * The sequence number of the message.
     */
    private int sequenceNumber;

    /**
     * Creates a new Directory Data composed by the given component destination URL and the dsp Message, as well as in
     * the QUEUED state.
     * 
     * @param componentDestinition is the URL of the DSP component.
     * @param dspMessage is the DSP message to be sent.
     */
    public QueueMessageData(int sequenceNumber, Message dspMessage) {
        this.destinitionIpAddress = dspMessage.getHeader().getConsumer().getComponentLocator().getNodeAddress().getValue();
        this.message = dspMessage;
        this.state = QueueMessageState.QUEUED;
        this.sequenceNumber = sequenceNumber;
    }

    /**
     * Factory method for the DirectoryData.
     * @param sequenceNumber is the sequence number for the message.
     * @param componentDestinition is the URL of the component.
     * @param dspMessage is the dspMessage.
     * @return a new instance of the DirectoryData.
     */
    public static QueueMessageData makeNewInstance(int sequenceNumber, Message dspMessage) {
        return new QueueMessageData(sequenceNumber, dspMessage);
    }

    public QueueMessageState getState() {
        return this.state;
    }

    /**
     * @return the sequence number of the message that is related to the window that it will be sent.
     */
    public int getSequenceNumber() {
        return this.sequenceNumber;
    }

    /**
     * Whenever a message has been acknowledged from the server-side.
     */
    public void changeStateToAcknowledged() {
        this.state = QueueMessageState.ACKNOWLEDGED;
    }

    public void changeStateToTransmitted() {
        this.state = QueueMessageState.TRANSMITTED;
    }

    public UUID getContainerId() {
        return this.containerId;
    }

    public void setMessagesContainerId(UUID messagesContainerId) {
        this.containerId = messagesContainerId;
    }

    public String getDestinitionIpAddress() {
        return this.destinitionIpAddress;
    }

    public Message getMessage() {
        return this.message;
    }

    public boolean equals(Object obj) {
        if (obj instanceof QueueMessageData) {
            return this.destinitionIpAddress.equals(((QueueMessageData) obj).destinitionIpAddress) && this.message.getMessageID().equals(((QueueMessageData) obj).message.getMessageID()) && this.containerId.equals(((QueueMessageData) obj).containerId);
        }
        return super.equals(obj);
    }

    public int hashCode() {
        return this.destinitionIpAddress.hashCode() + this.containerId.hashCode() + this.message.getMessageID().hashCode();
    }
}
