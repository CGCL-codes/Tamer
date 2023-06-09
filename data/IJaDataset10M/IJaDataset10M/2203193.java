package org.vtlabs.rtpproxy.mock.udp;

import java.io.IOException;
import java.net.InetSocketAddress;
import org.apache.mina.transport.socket.nio.DatagramAcceptor;
import org.vtlabs.rtpproxy.udp.DatagramHandler;
import org.vtlabs.rtpproxy.udp.DatagramListener;
import org.vtlabs.rtpproxy.udp.DatagramService;

/**
 *
 * @author mhack
 */
public class DatagramServiceMOCK extends DatagramService {

    public String sentMessage;

    public InetSocketAddress sentDstAddr;

    public DatagramServiceMOCK(int bindPort, DatagramListener listener) throws IOException {
        super(bindPort, listener);
    }

    @Override
    public void start() throws IOException {
    }

    @Override
    public void send(String message, InetSocketAddress dstAddr) {
        sentMessage = message;
        sentDstAddr = dstAddr;
    }

    @Override
    protected DatagramAcceptor createDatagramAcceptor() {
        return null;
    }

    @Override
    protected DatagramHandler createDatagramHandler(DatagramListener listener) {
        return null;
    }
}
