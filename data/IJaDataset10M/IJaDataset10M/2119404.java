package org.knopflerfish.bundle.connectors.socket;

import java.net.ServerSocket;
import java.io.IOException;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.ServerSocketConnection;

/**
 * @author perg@makewave.com
 */
class ServerSocketConnectionImpl implements ServerSocketConnection {

    private ServerSocket socket;

    private SocketConnectionFactory factory;

    public ServerSocketConnectionImpl(SocketConnectionFactory factory, ServerSocket socket) {
        this.socket = socket;
        this.factory = factory;
        factory.registerConnection(this);
    }

    public StreamConnection acceptAndOpen() throws IOException {
        return new SocketConnectionImpl(factory, socket.accept());
    }

    public void close() throws IOException {
        socket.close();
        factory.unregisterConnection(this);
    }

    public String getLocalAddress() throws IOException {
        return socket.getInetAddress().getHostAddress();
    }

    public int getLocalPort() throws IOException {
        return socket.getLocalPort();
    }
}
