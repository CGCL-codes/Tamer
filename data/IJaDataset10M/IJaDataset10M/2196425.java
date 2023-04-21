package org.xsocket.connection;

import java.io.IOException;

/**
 * acceptor callback <br><br>
 *   
 * @author grro@xsocket.org
 */
interface IIoAcceptorCallback {

    /**
	 * notifies that the acceptor has been bound to the socket, and 
	 * accepts incoming connections
	 *
	 */
    void onConnected();

    /**
	 * notifies that the acceptor has been unbound
	 *
	 */
    void onDisconnected();

    /**
	 * notifies a new incoming connection
	 * 
	 * @param ioHandler      the assigned io handler of the new connection
	 * @throws IOException If some other I/O error occurs
	 */
    void onConnectionAccepted(IoChainableHandler ioHandler) throws IOException;
}
