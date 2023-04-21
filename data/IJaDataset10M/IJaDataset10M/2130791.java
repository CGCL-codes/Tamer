package org.apache.http.conn.scheme;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import net.jcip.annotations.Immutable;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

/**
 * The default class for creating plain (unencrypted) sockets.
 * <p>
 * The following parameters can be used to customize the behavior of this 
 * class: 
 * <ul>
 *  <li>{@link org.apache.http.params.CoreConnectionPNames#CONNECTION_TIMEOUT}</li>
 * </ul>
 *
 * @since 4.0
 */
@Immutable
public final class PlainSocketFactory implements SocketFactory {

    /**
     * The default factory.
     */
    private static final PlainSocketFactory DEFAULT_FACTORY = new PlainSocketFactory();

    private final HostNameResolver nameResolver;

    /**
     * Gets the default factory. Usually there should be no reason for creating 
     * multiple instances of this class.
     * 
     * @return the default factory
     */
    public static PlainSocketFactory getSocketFactory() {
        return DEFAULT_FACTORY;
    }

    public PlainSocketFactory(final HostNameResolver nameResolver) {
        super();
        this.nameResolver = nameResolver;
    }

    public PlainSocketFactory() {
        this(null);
    }

    public Socket createSocket() {
        return new Socket();
    }

    public Socket connectSocket(Socket sock, String host, int port, InetAddress localAddress, int localPort, HttpParams params) throws IOException {
        if (host == null) {
            throw new IllegalArgumentException("Target host may not be null.");
        }
        if (params == null) {
            throw new IllegalArgumentException("Parameters may not be null.");
        }
        if (sock == null) sock = createSocket();
        if ((localAddress != null) || (localPort > 0)) {
            if (localPort < 0) localPort = 0;
            InetSocketAddress isa = new InetSocketAddress(localAddress, localPort);
            sock.bind(isa);
        }
        int timeout = HttpConnectionParams.getConnectionTimeout(params);
        InetSocketAddress remoteAddress;
        if (this.nameResolver != null) {
            remoteAddress = new InetSocketAddress(this.nameResolver.resolve(host), port);
        } else {
            remoteAddress = new InetSocketAddress(host, port);
        }
        try {
            sock.connect(remoteAddress, timeout);
        } catch (SocketTimeoutException ex) {
            throw new ConnectTimeoutException("Connect to " + remoteAddress + " timed out");
        }
        return sock;
    }

    /**
     * Checks whether a socket connection is secure.
     * This factory creates plain socket connections
     * which are not considered secure.
     *
     * @param sock      the connected socket
     *
     * @return  <code>false</code>
     *
     * @throws IllegalArgumentException if the argument is invalid
     */
    public final boolean isSecure(Socket sock) throws IllegalArgumentException {
        if (sock == null) {
            throw new IllegalArgumentException("Socket may not be null.");
        }
        if (sock.isClosed()) {
            throw new IllegalArgumentException("Socket is closed.");
        }
        return false;
    }
}
