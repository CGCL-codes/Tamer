package org.openbandy.net.client;

import org.openbandy.io.Message;
import org.openbandy.net.ConnectionHelper;
import org.openbandy.net.MessageBroker;
import org.openbandy.net.ServerProtocol;
import org.openbandy.service.LogService;

/**
 * TODO describe purpose and usage
 * 
 *  NOTE: does not use MMS at the moment because Nokia 6630 and 6680
 *  J2ME implementation does not support MMS sending right now.
 *
 * <br><br>
 * (c) Copyright Philipp Bolliger 2007, ALL RIGHTS RESERVED.
 *
 * @author Philipp Bolliger (philipp@bolliger.name)
 * @version 1.6
 * 
 */
public class MessageBrokerImpl implements MessageBroker {

    protected static ServerConnection networkConnection;

    protected static IncomingMessageQueue incomingMessageQueue;

    public MessageBrokerImpl() {
        networkConnection = new ServerConnection();
        incomingMessageQueue = new IncomingMessageQueue();
    }

    public synchronized Message sendMessage(String host, ServerProtocol protocol, Message message) {
        if (networkConnection.connect(host, protocol.getPort())) {
            ConnectionHelper.writeMessage(message, networkConnection.getOutputStream());
            Message response = ConnectionHelper.readMessage(networkConnection.getInputStream());
            if (response != null) {
                if (protocol.isValidMessage(response)) {
                    return response;
                } else {
                    LogService.warn(this, "Response is not valid");
                    return null;
                }
            }
        } else {
            LogService.warn(this, "No connection. Could not send message.");
        }
        return null;
    }
}
