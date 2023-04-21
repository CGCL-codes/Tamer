package org.apache.http.impl;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import org.apache.http.HttpInetConnection;
import org.apache.http.impl.io.SocketInputBuffer;
import org.apache.http.impl.io.SocketOutputBuffer;
import org.apache.http.io.SessionInputBuffer;
import org.apache.http.io.SessionOutputBuffer;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

/**
 * Implementation of a server-side HTTP connection that can be bound to a 
 * network Socket in order to receive and transmit data.
 *
 *
 * @version $Revision: 744524 $
 * 
 * @since 4.0
 */
public class SocketHttpServerConnection extends AbstractHttpServerConnection implements HttpInetConnection {

    private volatile boolean open;

    private volatile Socket socket = null;

    public SocketHttpServerConnection() {
        super();
    }

    protected void assertNotOpen() {
        if (this.open) {
            throw new IllegalStateException("Connection is already open");
        }
    }

    protected void assertOpen() {
        if (!this.open) {
            throw new IllegalStateException("Connection is not open");
        }
    }

    /**
     * @deprecated Use {@link #createSessionInputBuffer(Socket, int, HttpParams)}
     */
    protected SessionInputBuffer createHttpDataReceiver(final Socket socket, int buffersize, final HttpParams params) throws IOException {
        return createSessionInputBuffer(socket, buffersize, params);
    }

    /**
     * @deprecated Use {@link #createSessionOutputBuffer(Socket, int, HttpParams)}
     */
    protected SessionOutputBuffer createHttpDataTransmitter(final Socket socket, int buffersize, final HttpParams params) throws IOException {
        return createSessionOutputBuffer(socket, buffersize, params);
    }

    /**
     * Creates an instance of {@link SocketInputBuffer} to be used for 
     * receiving data from the given {@link Socket}.
     * <p>
     * This method can be overridden in a super class in order to provide 
     * a custom implementation of {@link SessionInputBuffer} interface.
     * 
     * @see SocketInputBuffer#SocketInputBuffer(Socket, int, HttpParams)
     * 
     * @param socket the socket.
     * @param buffersize the buffer size.
     * @param params HTTP parameters.
     * @return session input buffer.
     * @throws IOException in case of an I/O error.
     */
    protected SessionInputBuffer createSessionInputBuffer(final Socket socket, int buffersize, final HttpParams params) throws IOException {
        return new SocketInputBuffer(socket, buffersize, params);
    }

    /**
     * Creates an instance of {@link SessionOutputBuffer} to be used for 
     * sending data to the given {@link Socket}.
     * <p>
     * This method can be overridden in a super class in order to provide 
     * a custom implementation of {@link SocketOutputBuffer} interface.
     * 
     * @see SocketOutputBuffer#SocketOutputBuffer(Socket, int, HttpParams)
     * 
     * @param socket the socket.
     * @param buffersize the buffer size.
     * @param params HTTP parameters.
     * @return session output buffer.
     * @throws IOException in case of an I/O error.
     */
    protected SessionOutputBuffer createSessionOutputBuffer(final Socket socket, int buffersize, final HttpParams params) throws IOException {
        return new SocketOutputBuffer(socket, buffersize, params);
    }

    /**
     * Binds this connection to the given {@link Socket}. This socket will be 
     * used by the connection to send and receive data.
     * <p>
     * This method will invoke {@link #createSessionInputBuffer(Socket, int, HttpParams)}
     * and {@link #createSessionOutputBuffer(Socket, int, HttpParams)} methods 
     * to create session input / output buffers bound to this socket and then 
     * will invoke {@link #init(SessionInputBuffer, SessionOutputBuffer, HttpParams)} 
     * method to pass references to those buffers to the underlying HTTP message
     * parser and formatter. 
     * <p>
     * After this method's execution the connection status will be reported
     * as open and the {@link #isOpen()} will return <code>true</code>.
     * <p>
     * The following HTTP parameters affect configuration this connection:
     * <p>
     * The {@link CoreConnectionPNames#SOCKET_BUFFER_SIZE}
     * parameter determines the size of the internal socket buffer. If not 
     * defined or set to <code>-1</code> the default value will be chosen
     * automatically.
     * 
     * @param socket the socket.
     * @param params HTTP parameters.
     * @throws IOException in case of an I/O error.
     */
    protected void bind(final Socket socket, final HttpParams params) throws IOException {
        if (socket == null) {
            throw new IllegalArgumentException("Socket may not be null");
        }
        if (params == null) {
            throw new IllegalArgumentException("HTTP parameters may not be null");
        }
        this.socket = socket;
        int buffersize = HttpConnectionParams.getSocketBufferSize(params);
        init(createHttpDataReceiver(socket, buffersize, params), createHttpDataTransmitter(socket, buffersize, params), params);
        this.open = true;
    }

    protected Socket getSocket() {
        return this.socket;
    }

    public boolean isOpen() {
        return this.open;
    }

    public InetAddress getLocalAddress() {
        if (this.socket != null) {
            return this.socket.getLocalAddress();
        } else {
            return null;
        }
    }

    public int getLocalPort() {
        if (this.socket != null) {
            return this.socket.getLocalPort();
        } else {
            return -1;
        }
    }

    public InetAddress getRemoteAddress() {
        if (this.socket != null) {
            return this.socket.getInetAddress();
        } else {
            return null;
        }
    }

    public int getRemotePort() {
        if (this.socket != null) {
            return this.socket.getPort();
        } else {
            return -1;
        }
    }

    public void setSocketTimeout(int timeout) {
        assertOpen();
        if (this.socket != null) {
            try {
                this.socket.setSoTimeout(timeout);
            } catch (SocketException ignore) {
            }
        }
    }

    public int getSocketTimeout() {
        if (this.socket != null) {
            try {
                return this.socket.getSoTimeout();
            } catch (SocketException ignore) {
                return -1;
            }
        } else {
            return -1;
        }
    }

    public void shutdown() throws IOException {
        this.open = false;
        Socket tmpsocket = this.socket;
        if (tmpsocket != null) {
            tmpsocket.close();
        }
    }

    public void close() throws IOException {
        if (!this.open) {
            return;
        }
        this.open = false;
        doFlush();
        try {
            try {
                this.socket.shutdownOutput();
            } catch (IOException ignore) {
            }
            try {
                this.socket.shutdownInput();
            } catch (IOException ignore) {
            }
        } catch (UnsupportedOperationException ignore) {
        }
        this.socket.close();
    }
}
