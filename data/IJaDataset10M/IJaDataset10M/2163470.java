package com.amethyst.core;

/**
 * The listener interface for a client connected event 
 * @author David
 */
public interface ClientConnectedListener extends ConnectionListener {

    /**
	 * The connection method that is called when a client connection is made
	 * @param evt The event object that is associated with an event
	 */
    public void OnClientConnected(ClientConnectedEvent evt);
}
