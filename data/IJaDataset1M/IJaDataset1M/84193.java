package org.apache.mina.transport.socket.nio;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.transport.AbstractTrafficControlTest;

/**
 * Tests suspending and resuming reads and writes for the datagram
 * transport type.
 *
 * @author <a href="http://mina.apache.org">Apache MINA Project</a>
 */
public class DatagramTrafficControlTest extends AbstractTrafficControlTest {

    public DatagramTrafficControlTest() {
        super(new NioDatagramAcceptor());
    }

    @Override
    protected ConnectFuture connect(int port, IoHandler handler) throws Exception {
        IoConnector connector = new NioDatagramConnector();
        connector.setHandler(handler);
        return connector.connect(new InetSocketAddress("localhost", port));
    }

    @Override
    protected SocketAddress createServerSocketAddress(int port) {
        return new InetSocketAddress(port);
    }

    @Override
    protected int getPort(SocketAddress address) {
        return ((InetSocketAddress) address).getPort();
    }
}
