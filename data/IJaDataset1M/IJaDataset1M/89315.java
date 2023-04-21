package org.xsocket.connection.spi;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Map;
import org.xsocket.connection.IConnection;

/**
 * Client IO Provider, which is responsible to create and manage client-side {@link IIoHandler}  <br><br>
 *
 *
 * @author grro@xsocket.org
 */
public interface IClientIoProvider extends IHandlerIoProvider {

    public static final String PROVIDER_CLASSNAME_KEY = "org.xsocket.stream.io.ClientIoProviderClass";

    public static final String SO_SNDBUF = IConnection.SO_SNDBUF;

    public static final String SO_RCVBUF = IConnection.SO_RCVBUF;

    public static final String SO_REUSEADDR = IConnection.SO_REUSEADDR;

    public static final String SO_TIMEOUT = "SOL_SOCKET.SO_TIMEOUT";

    public static final String SO_KEEPALIVE = IConnection.SO_KEEPALIVE;

    public static final String SO_LINGER = IConnection.SO_LINGER;

    public static final String TCP_NODELAY = IConnection.TCP_NODELAY;

    public static final int DEFAULT_CONNECTION_TIMEOUT_SEC = IConnection.DEFAULT_CONNECTION_TIMEOUT_SEC;

    public static final int DEFAULT_IDLE_TIMEOUT_SEC = IConnection.DEFAULT_IDLE_TIMEOUT_SEC;

    /**
	 * Return the version of this implementation. It consists of any string assigned
	 * by the vendor of this implementation and does not have any particular syntax
	 * specified or expected by the Java runtime. It may be compared for equality
	 * with other package version strings used for this implementation
	 * by this vendor for this package.
	 *
	 * @return the version of the implementation
	 */
    public String getImplementationVersion();

    /**
	 * creates a client-site {@link IIoHandler}
	 *
	 * @param remoteAddress   the remote address
	 * @param options         the socket options
	 * @return the new IoHandler-instance
	 * @throws IOException If some other I/O error occurs
	 *
	 */
    public IIoHandler createClientIoHandler(InetSocketAddress remoteAddress, int connectTimeoutMillis, Map<String, Object> options) throws IOException;
}
