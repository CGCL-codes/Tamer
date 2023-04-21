package com.liferay.portal.kernel.messaging;

import com.liferay.portal.kernel.messaging.sender.MessageSender;
import com.liferay.portal.kernel.messaging.sender.SynchronousMessageSender;

/**
 * <a href="MessageBusUtil.java.html"><b><i>View Source</i></b></a>
 *
 * @author Michael C. Han
 *
 */
public class MessageBusUtil {

    public static void addDestination(Destination destination) {
        _instance._addDestination(destination);
    }

    public static MessageBus getMessageBus() {
        return _instance._messageBus;
    }

    public static MessageSender getMessageSender() {
        return _instance._messageSender;
    }

    public static boolean hasMessageListener(String destination) {
        return _instance._hasMessageListener(destination);
    }

    public static void init(MessageBus messageBus, MessageSender messageSender, SynchronousMessageSender synchronousMessageSender) {
        _instance._init(messageBus, messageSender, synchronousMessageSender);
    }

    public static void registerMessageListener(String destination, MessageListener listener) {
        _instance._registerMessageListener(destination, listener);
    }

    public static void removeDestination(String destination) {
        _instance._removeDestination(destination);
    }

    public static void sendMessage(String destination, Message message) {
        _instance._sendMessage(destination, message);
    }

    public static void sendMessage(String destination, Object payload) {
        _instance._sendMessage(destination, payload);
    }

    public static Object sendSynchronousMessage(String destination, Message message) throws MessageBusException {
        return _instance._sendSynchronousMessage(destination, message);
    }

    public static Object sendSynchronousMessage(String destination, Object payload) throws MessageBusException {
        return _instance._sendSynchronousMessage(destination, payload);
    }

    public static Object sendSynchronousMessage(String destination, Message message, long timeout) throws MessageBusException {
        return _instance._sendSynchronousMessage(destination, message, timeout);
    }

    public static Object sendSynchronousMessage(String destination, Object payload, long timeout) throws MessageBusException {
        return _instance._sendSynchronousMessage(destination, payload, timeout);
    }

    public static boolean unregisterMessageListener(String destination, MessageListener listener) {
        return _instance._unregisterMessageListener(destination, listener);
    }

    private MessageBusUtil() {
    }

    private void _addDestination(Destination destination) {
        _messageBus.addDestination(destination);
    }

    private boolean _hasMessageListener(String destination) {
        return _messageBus.hasMessageListener(destination);
    }

    private void _init(MessageBus messageBus, MessageSender messageSender, SynchronousMessageSender synchronousMessageSender) {
        _messageBus = messageBus;
        _messageSender = messageSender;
        _synchronousMessageSender = synchronousMessageSender;
    }

    private void _registerMessageListener(String destination, MessageListener listener) {
        _messageBus.registerMessageListener(destination, listener);
    }

    private void _removeDestination(String destination) {
        _messageBus.removeDestination(destination);
    }

    private void _sendMessage(String destination, Message message) {
        _messageBus.sendMessage(destination, message);
    }

    private void _sendMessage(String destination, Object payload) {
        Message message = new Message();
        message.setPayload(payload);
        _sendMessage(destination, message);
    }

    private Object _sendSynchronousMessage(String destination, Message message) throws MessageBusException {
        return _synchronousMessageSender.sendMessage(destination, message);
    }

    private Object _sendSynchronousMessage(String destination, Object payload) throws MessageBusException {
        Message message = new Message();
        message.setPayload(payload);
        return _sendSynchronousMessage(destination, message);
    }

    private Object _sendSynchronousMessage(String destination, Message message, long timeout) throws MessageBusException {
        return _synchronousMessageSender.sendMessage(destination, message, timeout);
    }

    private Object _sendSynchronousMessage(String destination, Object payload, long timeout) throws MessageBusException {
        Message message = new Message();
        message.setPayload(payload);
        return _sendSynchronousMessage(destination, message, timeout);
    }

    private boolean _unregisterMessageListener(String destination, MessageListener listener) {
        return _messageBus.unregisterMessageListener(destination, listener);
    }

    private static MessageBusUtil _instance = new MessageBusUtil();

    private MessageBus _messageBus;

    private MessageSender _messageSender;

    private SynchronousMessageSender _synchronousMessageSender;
}
