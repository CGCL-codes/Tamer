package ch.squix.nataware.messaging;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.protobuf.Message;

public class MessageHandlerNotifier implements IMessageObservable {

    private List<IMessageHandler> messageHandlers;

    private Map<IMessageHandler, Class<?>> messageTypeRegistry;

    public MessageHandlerNotifier() {
        messageHandlers = new ArrayList<IMessageHandler>();
        messageTypeRegistry = new HashMap<IMessageHandler, Class<?>>();
    }

    public void addMessageHandler(IMessageHandler handler) {
        messageHandlers.add(handler);
        messageTypeRegistry.put(handler, null);
    }

    public void addMessageHandler(IMessageHandler handler, Class<?> messageType) {
        messageHandlers.add(handler);
        messageTypeRegistry.put(handler, messageType);
    }

    public void removeMessageHandler(IMessageHandler handler) {
        messageHandlers.remove(handler);
        messageTypeRegistry.remove(handler);
    }

    public void notifyMessageHandlers(Message message) {
        for (IMessageHandler messageHandler : messageHandlers) {
            Class<?> registeredType = messageTypeRegistry.get(messageHandler);
            if (registeredType == null || message.getClass().isInstance(registeredType)) {
                messageHandler.messageReceived(message);
            }
        }
    }
}
