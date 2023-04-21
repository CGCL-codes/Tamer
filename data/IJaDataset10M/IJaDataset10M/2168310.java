package padrmi.action;

import java.io.*;
import java.net.*;
import java.security.*;

/**
 * Resolves host address and connects socket.
 * 
 * @see java.net.InetSocketAddress
 * @see java.net.Socket#connect(java.net.SocketAddress)
 */
public class SocketConnectAction implements PrivilegedExceptionAction<Socket> {

    /**
	 * Host name.
	 */
    private final String host;

    /**
	 * Port number.
	 */
    private final int port;

    /**
	 * Creates action.
	 * 
	 * @param host
	 *            host name
	 * @param port
	 *            port number
	 */
    public SocketConnectAction(String host, int port) {
        this.host = host;
        this.port = port;
    }

    /**
	 * Resolves host address and connects socket.
	 * 
	 * @see java.security.PrivilegedExceptionAction#run()
	 */
    public Socket run() throws IOException {
        Socket socket = new Socket();
        SocketAddress address = new InetSocketAddress(host, port);
        socket.connect(address);
        return socket;
    }
}
