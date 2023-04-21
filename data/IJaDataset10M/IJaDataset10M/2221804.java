package com.usoog.commons.network;

import com.usoog.commons.network.message.Message;
import java.io.Closeable;
import java.net.SocketAddress;

/**
 * This represents a connection over the internet or a network.
 *
 * @author Jimmy Axenhus
 * @author Hylke van der Schaaf
 */
public interface NetworkConnection extends Closeable {

    /**
	 * This will send a Message over the network.
	 *
	 * @param line The Message to send.
	 */
    public void sendMessage(Message line);

    /**
	 * Returns the address of the other side.
	 *
	 * @return The SocketAddress we're connected to.
	 */
    public SocketAddress getRemoteAddress();

    /**
	 * Adds a MessageListener to this connection.
	 *
	 * @param messageListener The MessageListener to register.
	 */
    public void addMessageListener(MessageListener messageListener);

    /**
	 * This will remove the specified MessageListener.
	 *
	 * @param messageListener The MessageListener to remove.
	 */
    public void removeMessageListener(MessageListener messageListener);
}
