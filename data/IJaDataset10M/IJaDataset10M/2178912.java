package net.sf.opengroove.client.messaging;

import java.io.File;
import net.sf.opengroove.client.storage.OutboundMessage;

/**
 * An interface that classes can implement to indicate that they know how to
 * send messages. The topmost MessageHierarchy instance in any given hierarchy
 * must have a MessageSender attached in order for messages to be sent.
 * 
 * @author Alexander Boyd
 * 
 */
public interface MessageDeliverer {

    /**
     * Actually sends the specified message.
     */
    public void sendMessage(OutboundMessage message);

    public OutboundMessage createMessage();

    public File getInboundMessageFile(String messageId);

    public File getOutboundMessageFile(String messageId);
}
