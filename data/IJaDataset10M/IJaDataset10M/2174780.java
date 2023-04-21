package org.apache.mina.transport.socket.nio;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.transport.AbstractBindTest;

/**
 * Tests {@link NioDatagramAcceptor} resource leakage.
 *
 * @author <a href="http://mina.apache.org">Apache MINA Project</a>
 */
public class DatagramBindTest extends AbstractBindTest {

    public DatagramBindTest() {
        super(new NioDatagramAcceptor());
    }

    @Override
    protected SocketAddress createSocketAddress(int port) {
        return new InetSocketAddress("localhost", port);
    }

    @Override
    protected int getPort(SocketAddress address) {
        return ((InetSocketAddress) address).getPort();
    }

    @Override
    protected IoConnector newConnector() {
        return new NioDatagramConnector();
    }
}
