package pl.edu.pjwstk.net.proto.TCP;

import org.apache.log4j.Logger;
import pl.edu.pjwstk.net.TransportPacket;
import pl.edu.pjwstk.net.proto.*;
import pl.edu.pjwstk.p2pp.GlobalConstants;
import pl.edu.pjwstk.p2pp.messages.Message;
import pl.edu.pjwstk.p2pp.messages.NonInterpretedMessage;
import java.io.IOException;
import java.io.OutputStream;
import java.net.*;
import java.nio.*;
import java.nio.channels.SocketChannel;

public class TCPNBWorker extends ProtocolWorker implements ProtocolControl, ProtocolReader, ProtocolWriter {

    private static Logger LOG = Logger.getLogger(TCPNBWorker.class);

    private TCPNBServer tcpServer;

    private void Initialize() throws IOException {
        if (LOG.isDebugEnabled()) LOG.debug("Initializing TCPNonBlockingWorker");
        this.isReliable = true;
        if (this.localAddress == null) {
            this.tcpServer = new TCPNBServer(this.localPort);
        } else {
            this.tcpServer = new TCPNBServer(this.localAddress, this.localPort);
        }
        this.tcpServer.start();
        if (this.localAddress == null) this.localAddress = this.tcpServer.getAddress();
        this.isReady = true;
    }

    public TCPNBWorker(Integer localPort) throws IOException {
        super(localPort);
        this.localPort = localPort;
        Initialize();
    }

    public TCPNBWorker(InetAddress localIP, Integer localPort) throws IOException {
        super(localIP, localPort);
        this.localAddress = localIP;
        this.localPort = localPort;
        Initialize();
    }

    public TCPNBWorker(Message message) throws IOException {
        super(message);
        this.localAddress = InetAddress.getByName(message.getSenderAddress());
        this.localPort = Integer.parseInt(message.getReceiverAddress());
        Initialize();
        SendMessage(message);
    }

    public TCPNBWorker(InetAddress localIP, Integer localPort, InetAddress remoteIP, Integer remotePort) throws IOException {
        super(localIP, localPort, remoteIP, remotePort);
        this.localAddress = localIP;
        this.localPort = localPort;
        this.remoteAddress = remoteIP;
        this.remotePort = remotePort;
        Initialize();
    }

    public boolean Send(byte[] packet) throws IOException {
        return false;
    }

    public boolean Send(InetAddress remoteIP, Integer remotePort, byte[] packet) throws IOException {
        return false;
    }

    public boolean SendMessage(Message message) {
        if (!this.isWorkerReady()) {
            LOG.warn("Trying to send message with worker not initialized worker. Won't happen");
            return false;
        }
        if (!message.isOverReliable()) return false;
        boolean success = true;
        SocketChannel sc = null;
        InetSocketAddress isa = null;
        try {
            message.setSenderAddress(this.localAddress.toString());
            message.setSenderPort(this.localPort);
            ByteBuffer bytebuf = ByteBuffer.allocate(message.asBytes().length);
            sc = SocketChannel.open();
            isa = new InetSocketAddress(message.getReceiverAddress(), message.getReceiverPort());
            sc.connect(isa);
            sc.configureBlocking(false);
            bytebuf = ByteBuffer.wrap(message.asBytes());
            sc.write(bytebuf);
        } catch (Throwable e) {
            LOG.error("Error while sending message " + message, e);
            success = false;
        } finally {
            if (sc != null) {
                try {
                    sc.close();
                } catch (IOException e) {
                    LOG.error("Error while closing the SocketChannel", e);
                }
            }
        }
        return success;
    }

    public boolean isReliable() {
        return this.isReliable;
    }

    public boolean isEncrypted() {
        return false;
    }

    public byte[] Receive() throws IOException {
        return this.ReceivePacket().packetBody;
    }

    public TransportPacket ReceivePacket() throws IOException {
        TransportPacket transportPacket = null;
        try {
            transportPacket = this.tcpServer.getReceivedPacket();
        } catch (InterruptedException e) {
            LOG.error("Error while receiving packet", e);
        }
        return transportPacket;
    }

    public NonInterpretedMessage ReceiveMessage() throws IOException {
        TransportPacket packet = ReceivePacket();
        NonInterpretedMessage message = null;
        if (packet != null) {
            if (this.localAddress == null) localAddress = this.tcpServer.getAddress();
            message = new NonInterpretedMessage(localAddress.getHostAddress(), localPort, packet.remoteAddress.getHostAddress(), packet.remotePort, GlobalConstants.isOverReliable, false, packet.packetBody);
        }
        return message;
    }

    public boolean isMessageStateMachine() {
        return true;
    }

    public boolean isWorkerReady() {
        return this.isReady;
    }

    public TCPNBServer getServer() {
        return this.tcpServer;
    }

    public int getPort() {
        return this.localPort;
    }

    public InetAddress getAddress() {
        return this.localAddress;
    }

    public int getRemotePort() {
        return this.remotePort;
    }

    public InetAddress getRemoteAddress() {
        return this.remoteAddress;
    }
}
