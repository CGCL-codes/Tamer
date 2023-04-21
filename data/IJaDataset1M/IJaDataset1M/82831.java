package pl.edu.pjwstk.net.proto.TCP;

import org.apache.log4j.Logger;
import pl.edu.pjwstk.net.TransportPacket;
import pl.edu.pjwstk.net.proto.*;
import pl.edu.pjwstk.p2pp.GlobalConstants;
import pl.edu.pjwstk.p2pp.messages.Message;
import pl.edu.pjwstk.p2pp.messages.NonInterpretedMessage;
import javax.net.SocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.io.OutputStream;
import java.net.*;

/**
 * TCP protocol worker implementation.
 *
 * @author Konrad Adamczyk conrad.adamczyk@gmail.com
 */
public class TCPWorker extends ProtocolWorker implements ProtocolControl, ProtocolReader, ProtocolWriter {

    private static Logger LOG = Logger.getLogger(TCPWorker.class);

    private SupportedEncryption encryption;

    private String keysPath;

    private String passphrase;

    private SocketFactory socketFactory;

    private TCPServer tcpServer;

    private void Initialize() throws IOException {
        if (LOG.isDebugEnabled()) LOG.debug("Initializing TCPWorker");
        this.isReliable = true;
        if (this.encryption == null) {
            this.socketFactory = SocketFactory.getDefault();
            if (this.localAddress == null) {
                this.tcpServer = new TCPServer(this.localPort);
            } else {
                this.tcpServer = new TCPServer(this.localAddress, this.localPort);
            }
        } else {
            try {
                this.socketFactory = SSLSocketFactory.getDefault();
                if (this.localAddress == null) {
                    this.tcpServer = new TCPServer(this.localPort, this.encryption, this.keysPath, this.passphrase);
                } else {
                    this.tcpServer = new TCPServer(this.localAddress, this.localPort, this.encryption, this.keysPath, this.passphrase);
                }
            } catch (Throwable e) {
                throw new IOException("Error while starting TCPServer with encryption", e);
            }
        }
        this.tcpServer.start();
        if (this.localAddress == null) this.localAddress = this.tcpServer.getAddress();
        this.isReady = true;
    }

    public TCPWorker(Integer localPort) throws IOException {
        super(localPort);
        this.localPort = localPort;
        Initialize();
    }

    public TCPWorker(InetAddress localIP, Integer localPort) throws IOException {
        super(localIP, localPort);
        this.localAddress = localIP;
        this.localPort = localPort;
        Initialize();
    }

    public TCPWorker(Message message) throws IOException {
        super(message);
        this.localAddress = InetAddress.getByName(message.getSenderAddress());
        this.localPort = Integer.parseInt(message.getReceiverAddress());
        Initialize();
        SendMessage(message);
    }

    public TCPWorker(InetAddress localIP, Integer localPort, InetAddress remoteIP, Integer remotePort) throws IOException {
        super(localIP, localPort, remoteIP, remotePort);
        this.localAddress = localIP;
        this.localPort = localPort;
        Initialize();
    }

    public TCPWorker(Integer localPort, SupportedEncryption encryption, String keysPath, String passphrase) throws IOException {
        super(localPort);
        this.localPort = localPort;
        this.encryption = encryption;
        this.keysPath = keysPath;
        this.passphrase = passphrase;
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
        Socket socket = null;
        try {
            message.setSenderAddress(this.localAddress.toString());
            message.setSenderPort(this.localPort);
            socket = this.socketFactory.createSocket(message.getReceiverAddress(), message.getReceiverPort());
            socket.setSoTimeout(30000);
            if (this.encryption != null) ((SSLSocket) socket).startHandshake();
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write(message.asBytes());
            outputStream.flush();
        } catch (Throwable e) {
            LOG.error("Error while sending message " + message, e);
            success = false;
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    LOG.error("Error while closing the socket", e);
                }
            }
        }
        return success;
    }

    public boolean isReliable() {
        return this.isReliable;
    }

    public boolean isEncrypted() {
        return this.encryption != null;
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
}
