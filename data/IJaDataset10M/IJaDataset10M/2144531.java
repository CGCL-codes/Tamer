package org.actioncenters.message;

/**
 * @author dougk
 *
 */
public class Message implements IMessage {

    /**
     * The correlation id of the message.  Used to track a message through the system.
     */
    private String correlationId;

    /**
     * The content of the message.
     */
    private IMessageData messageData;

    /**
     * The priority of the message.
     */
    private MessagePriority priority;

    /**
     * Default constructor.
     */
    public Message() {
    }

    /**
     * Creates a new message from an old message copying the CorrelationId and Priority from the old message
     * to the new message.
     * @param oldMessage Message to pair this one with.  Used to pair a response message to a request message.
     */
    public Message(IMessage oldMessage) {
        setCorrelationId(oldMessage.getCorrelationId());
        setPriority(oldMessage.getPriority());
    }

    /**
     * Constructor that sets the correlationId and priority.
     * @param correlationId An id used to track the message through the system.
     * @param priority The priority of the message.
     */
    public Message(String correlationId, MessagePriority priority) {
        setCorrelationId(correlationId);
        setPriority(priority);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCorrelationId() {
        return correlationId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IMessageData getMessageData() {
        return messageData;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MessagePriority getPriority() {
        return priority;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setMessageData(IMessageData messageData) {
        this.messageData = messageData;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPriority(MessagePriority priority) {
        this.priority = priority;
    }
}
