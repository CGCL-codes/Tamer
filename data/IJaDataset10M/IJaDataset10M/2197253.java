package com.googlecode.lawu.net.event;

import com.googlecode.lawu.net.SocketClient;

/**
 * Raised when a connection is successfully completed.
 * 
 * @author Miorel-Lucian Palii
 */
public class ConnectedEvent extends AbstractNetworkEvent {

    /**
	 * Constructs a connection event associated with the given client.
	 * 
	 * @param client
	 *            the client to associate with
	 */
    public ConnectedEvent(SocketClient client) {
        super(client);
    }

    @Override
    protected void doTrigger(NetworkEventListener listener) {
        listener.connected(this);
    }
}
