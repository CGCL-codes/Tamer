package uk.azdev.openfire.friendlist.messageprocessors;

import uk.azdev.openfire.ConnectionEventListener;
import uk.azdev.openfire.conversations.IConversation;
import uk.azdev.openfire.conversations.IConversationStore;
import uk.azdev.openfire.net.IMessageSender;
import uk.azdev.openfire.net.messages.IMessage;
import uk.azdev.openfire.net.messages.bidirectional.ChatMessage;

public class ChatMessageProcessor implements IMessageProcessor {

    private IConversationStore conversationStore;

    private ConnectionEventListener listener;

    private IMessageSender messageSender;

    public ChatMessageProcessor(IConversationStore conversationStore, ConnectionEventListener listener, IMessageSender messageSender) {
        this.conversationStore = conversationStore;
        this.listener = listener;
        this.messageSender = messageSender;
    }

    public void processMessage(IMessage msg) {
        ChatMessage message = (ChatMessage) msg;
        IConversation conversation = conversationStore.getConversation(message.getSessionId());
        conversation.receiveMessage(message);
        if (message.isContentMessage()) {
            sendAck(message);
            listener.conversationUpdate(message.getSessionId());
        }
    }

    private void sendAck(ChatMessage message) {
        ChatMessage ack = new ChatMessage();
        ack.setSessionId(message.getSessionId());
        ack.setAcknowledgementPayload(message.getMessageIndex());
        messageSender.sendMessage(ack);
    }
}
