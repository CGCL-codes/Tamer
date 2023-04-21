package com.teletalk.jserver.tcp.messaging;

import com.teletalk.jserver.net.acl.AccessControlListHandler;
import com.teletalk.jserver.tcp.TcpEndPointIdentifier;
import com.teletalk.jserver.tcp.messaging.admin.ServerAdministrationHandler;

/**
 * Default message processor implementation. 
 * 
 * @author Tobias L�fstrand
 * 
 * @since 2.1 (20050517)
 */
public class DefaultMessageProcessor implements MessageProcessor {

    private MessagingManager messagingManager;

    /**
    * Creates a new DefaultMessageProcessor.
    */
    public DefaultMessageProcessor() {
    }

    /**
    * Gets the MessagingManager associated with this handler.
    */
    public MessagingManager getMessagingManager() {
        return messagingManager;
    }

    /**
    * Sets the MessagingManager associated with this handler.
    */
    public void setMessagingManager(MessagingManager messagingManager) {
        this.messagingManager = messagingManager;
    }

    /**
    * Called by the associated MessagingManager to initialize this handler. This method is called when the MessagingManager 
    * is starting up, after {@link #setMessagingManager(MessagingManager)} has been called.
    */
    public void initialize() {
    }

    /**
    * Called by the associated MessagingManager to shut down this handler. This method is called when the MessagingManager 
    * is shutting down. 
    */
    public void shutDown() {
    }

    /**
    * Handles a response message, with the use of the specified message dispatch handler.<br>
    * <br>
    * Note: This method will by default be called synchronously, by the thread reading from the endpoint.
    * 
    * @return <code>true</code> if the specified message was a response to a message send through this object, otherwise <code>false</code>.
    */
    public boolean handleResponseMessage(final Message message, final MessageDispatchHandler messageDispatchHandler) throws Exception {
        return messageDispatchHandler.responseReceived(message);
    }

    /**
    * Called to notify a {@link MessageReceiver} of an incomming message.<br>
    * <br>
    * The implementation of this method calls the method {@link MessageReceiver#messageReceived(Message)} on the specified 
    * message receiver. If access control list checks are enabled and the specified message receiver is an {@link AccessControlMessageReceiver} 
    * an access control list check will be performed before the message receiver may handle the message. If the check fails, a response message 
    * will be generated and dispatched, using the header type {@link MessageHeader#ACCESS_DENIED_HEADER}.<br>
    * <br>
    * <i>Note</i>: If the message cannot be identified (i.e. if {@link AccessControlMessageReceiver#identify(Message)} returns null), this method 
    * will do noting and return <code>false</code>. <br>
    * <br>
    * Note: This method will by default be called from a different thread than the thread reading from the endpoint.
    * 
    * @param messageReceiver the {@link MessageReceiver}that is to be notified of an incomming message.
    * @param message the received message.
    * @param accessControlListHandler the {@link AccessControlListHandler} used by the associated MessagingManager. May be null.
    * 
    * @return <code>true</code> if the specified message receiver handled the message, otherwise <code>false</code>.
    */
    public boolean handleInMessageReceiver(final Message message, final MessageReceiver messageReceiver, final AccessControlListHandler accessControlListHandler) throws Exception {
        boolean messageHandled = false;
        if (messageReceiver != null) {
            if ((accessControlListHandler != null) && accessControlListHandler.isAccessControlCheckEnabled() && (messageReceiver instanceof AccessControlMessageReceiver)) {
                messageHandled = notifyMessageReceiverWithAccessCheck(messageReceiver, message, accessControlListHandler);
            }
            if (!messageHandled) {
                if (this.messagingManager.isDebugMode()) this.messagingManager.logDebug("Notifying message receiver of message " + message + ".");
                messageReceiver.messageReceived(message);
                messageHandled = true;
            }
        }
        return messageHandled;
    }

    /**
    * Method for handling messages (calls) to the {@link ServerAdministrationHandler} of this MessagingManager. This method is 
    * invoked by the method {@link MessagingManager#messageReceivedImpl(Message)} if the header type is {@link MessageHeader#SERVER_ADMINISTRATION_HEADER}.<br>
    * <br>
    * Note: This method will by default be called from a different thread than the thread reading from the endpoint.
    * 
    * @param message the received message.
    */
    public boolean handleServerAdministrationMessage(final Message message, final AccessControlListHandler accessControlListHandler) throws Exception {
        String interfaceName = this.messagingManager.getInterfaceName(message);
        if ((accessControlListHandler != null) && accessControlListHandler.isAccessControlCheckEnabled() && !accessControlListHandler.hasServerAdministrationAccess(interfaceName)) {
            MessageHeader header = message.getHeader();
            header.setHeaderType(MessageHeader.ACCESS_DENIED_HEADER);
            header.setDescription("No access to server administration from address " + message.getDestinationAddress() + "(" + interfaceName + ")" + "!");
            this.messagingManager.dispatchMessageAsync(header);
            return true;
        }
        ServerAdministrationHandler serverAdministrationHandler = this.messagingManager.getServerAdministrationHandler();
        if (serverAdministrationHandler != null) {
            serverAdministrationHandler.messageReceived(message);
            return true;
        }
        return false;
    }

    /**
    * Handles a proxied message.<br>
    * <br>
    * Note: This method will by default be called from a different thread than the thread reading from the endpoint.
    * 
    * @param message the message to relay to another messaging system.
    * @param namedReceiver the receiver name to which the message should be relayed to. May be null if the message is a response. 
    * 
    * @return <code>true</code> if the specified message could be proxied, otherwise <code>false</code>.
    */
    public boolean handleProxyMessage(final Message message, final String namedReceiver) throws Exception {
        return this.messagingManager.dispatchProxiedMessage(message, null, namedReceiver);
    }

    /**
    * Called to handle an unhandle message.<br>
    * <br>
    * This implementation sends a reponse message, using the header type {@link MessageHeader#MESSAGE_PROCESSING_ERROR_HEADER}.<br>
    * <br>
    * Note: This method will by default be called from a different thread than the thread reading from the endpoint.
    * 
    * @param message the unhandled message.
    */
    public void handleUnhandledMessage(Message message) throws Exception {
        final MessageHeader header = message.getHeader();
        if (this.messagingManager.isDebugMode()) this.messagingManager.logDebug("Unhandled message: " + message + ".");
        message.signalReadCompletion();
        if (!header.isAsynch() && (header.getProtocolVersion() > 4)) {
            header.setHeaderType(MessageHeader.MESSAGE_PROCESSING_ERROR_HEADER);
            header.setDescription("No handler available to process message!");
            this.messagingManager.dispatchMessageAsync(header);
        }
    }

    /**
    * Method for handling errors that occur while {@link MessageWorker} threads executes {@link MessagingManager#messageReceivedImpl(Message)}, 
    * i.e. when a message receiver handles a message.<br>
    * <br>
    * Note: This method will by default be called from a different thread than the thread reading from the endpoint.
    * 
    * @param message the received message.
    */
    public void handleMessageReceiverError(final Message message, final Throwable error) {
        MessageHeader header = message.getHeader();
        this.messagingManager.logError("Error occurred in message receiver while processing message " + message + "!", error);
        if (!header.isAsynch() && (header.getProtocolVersion() > 4)) {
            header.setHeaderType(MessageHeader.MESSAGE_PROCESSING_ERROR_HEADER);
            header.setDescription(error.toString());
            this.messagingManager.dispatchMessageAsync(header);
        }
    }

    /**
    * Performs an access control list check for the specified message before invoking {@link MessageReceiver#messageReceived(Message)} 
    * to handle the message, if the check is successful.
    */
    private boolean notifyMessageReceiverWithAccessCheck(final MessageReceiver messageReceiver, final Message message, final AccessControlListHandler accessControlListHandler) {
        MessageHeader header = message.getHeader();
        String operationId = null;
        boolean callIdentify = true;
        if (messageReceiver instanceof MessageReceiverComponent) {
            callIdentify = ((MessageReceiverComponent) messageReceiver).isAccessControlCheckEnabled();
        }
        if (callIdentify) {
            try {
                operationId = ((AccessControlMessageReceiver) messageReceiver).identify(message);
            } catch (Throwable t) {
                this.messagingManager.logError("Error while identifying message " + message + "!", t);
                throw (RuntimeException) new RuntimeException(t.getMessage()).fillInStackTrace();
            }
            if (operationId != null) {
                TcpEndPointIdentifier address = message.getDestinationAddress();
                String interfaceName = this.messagingManager.getInterfaceName(message);
                boolean messageHandled = false;
                if (accessControlListHandler.checkAccess(interfaceName, operationId)) {
                    if (this.messagingManager.isDebugMode()) {
                        this.messagingManager.logDebug("Access control check success for operation with id: " + operationId + ", sent from address: " + address + ((interfaceName != null) ? ("(" + interfaceName + ")") : "") + " - notifying message receiver. Message header: " + header + ".");
                    }
                    messageReceiver.messageReceived(message);
                    messageHandled = true;
                } else {
                    if (this.messagingManager.isDebugMode()) {
                        this.messagingManager.logDebug("Access control check FAILED for operation with id: " + operationId + ", sent from address: " + address + ((interfaceName != null) ? ("(" + interfaceName + ")") : "") + ". Message header: " + header + ".");
                    }
                    try {
                        messageHandled = true;
                        message.signalReadCompletion();
                        header.setHeaderType(MessageHeader.ACCESS_DENIED_HEADER);
                        header.setDescription("No access to execute operation " + operationId + " from address " + address + "(" + interfaceName + ")" + "!");
                        this.messagingManager.dispatchMessageAsync(header);
                    } catch (Exception e) {
                        this.messagingManager.logWarning("Failed to dispatch access denied response to message " + message + "!", e);
                    }
                }
                return messageHandled;
            }
        }
        return false;
    }
}
