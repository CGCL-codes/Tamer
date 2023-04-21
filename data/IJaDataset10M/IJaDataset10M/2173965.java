package net.sourceforge.javautil.network.socket.udp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import net.sourceforge.javautil.common.NetworkUtil;
import net.sourceforge.javautil.network.protocol.IProtocol;
import net.sourceforge.javautil.network.protocol.bmp.BasicMessageProtocol1_1;
import net.sourceforge.javautil.network.socket.ISocketTransport;
import net.sourceforge.javautil.network.socket.ISocketTransportListener;

/**
 * 224.0.0.0 to 239.255.255.255, inclusive. The address 224.0.0.0 is reserved and should not be used.
 * 
 * @author elponderador
 * @author $Author$
 * @version $Id$
 */
public class UDPMulticastIPV4 implements ISocketTransport {

    /**
	 * @param address The address in question
	 * @return True if the address is a valid address in the multicast range
	 */
    public static boolean isValidMulticastIPV4Address(InetAddress address) {
        int[] digits = NetworkUtil.getRealAddress(address);
        if (digits[0] < 224 || digits[0] > 239) return false;
        if (digits[0] == 224) {
            if (digits[1] == 0 && digits[2] == 0 && digits[3] == 1) return false;
        }
        return true;
    }

    public static void main(String[] args) throws Exception {
    }

    protected final MulticastSocket socket;

    protected final IProtocol protocol;

    protected MulticastUDPSocketListener listener;

    public UDPMulticastIPV4(InetAddress multicastGroup, IProtocol protocol) throws IOException {
        this(multicastGroup, protocol, 0);
    }

    public UDPMulticastIPV4(InetAddress multicastGroup, IProtocol protocol, int port) throws IOException {
        this(multicastGroup, protocol, port, 2000);
    }

    public UDPMulticastIPV4(InetAddress multicastGroup, IProtocol protocol, int port, long socketTimeout) throws IOException {
        if (!isValidMulticastIPV4Address(multicastGroup)) throw new UDPException("Invalid multicast group (must be in range [224.0.0.1 to 239.255.255.255]): " + multicastGroup.getHostAddress());
        this.socket = new MulticastSocket(port);
        this.socket.setReuseAddress(true);
        this.socket.setBroadcast(true);
        this.socket.setSoTimeout(2000);
        this.socket.joinGroup(multicastGroup);
        this.protocol = protocol;
    }

    public IProtocol getProtocol() {
        return this.protocol;
    }

    @Override
    public OutputStream getMessageOutputStream() {
        return null;
    }

    @Override
    public InetAddress getFrom() {
        return null;
    }

    @Override
    public int getPort() {
        return 0;
    }

    @Override
    public InetSocketAddress getLocalAddress() {
        return null;
    }

    @Override
    public InetSocketAddress getRemoteAddress() {
        return null;
    }

    @Override
    public void startListening(ISocketTransportListener listener) {
    }

    @Override
    public void stopListening() {
    }

    @Override
    public boolean isListening() {
        return false;
    }

    @Override
    public boolean isConnected() {
        return false;
    }

    @Override
    public void setKeepAlive(boolean flag, long interval) {
    }

    @Override
    public void close() {
    }

    @Override
    public void sendMessage(byte[] message, int offset, int length) {
    }

    public void sendMessage(byte[] packet) {
    }

    protected class MulticaseUDPSocketDispatcher implements Runnable {

        @Override
        public void run() {
        }

        ;
    }

    /**
	 * 
	 * 
	 * @author elponderador
	 * @author $Author$
	 * @version $Id$
	 */
    protected class MulticastUDPSocketListener implements Runnable {

        @Override
        public void run() {
        }
    }
}
