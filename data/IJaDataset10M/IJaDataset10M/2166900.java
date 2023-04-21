package ow.messaging;

import ow.stat.MessagingReporter;

public interface MessageReceiver {

    /**
	 * Returns a MessageSender.
	 */
    MessageSender getSender();

    /**
	 * Registers a message handler.
	 */
    void addHandler(MessageHandler handler);

    /**
	 * Removes a message handler.
	 */
    void removeHandler(MessageHandler handler);

    /**
	 * Returns SocketAddress of the node itself.
	 */
    MessagingAddress getSelfAddress();

    /**
	 * Sets SocketAddress of the node itself.
	 */
    void setSelfAddress(String hostnameOrIPAddress);

    /**
	 * Sets MessagingAddress directly.
	 */
    MessagingAddress setSelfAddress(MessagingAddress msgAddress);

    /**
	 * Returns port number bound to this receiver.
	 */
    int getPort();

    MessagingReporter getMessagingReporter();

    /**
	 * Stops this receiver.
	 */
    void stop();

    /**
	 * Restarts this receiver after stopped.
	 */
    void start();
}
