package pl.edu.pjwstk.net.proto.TCP;

import org.apache.log4j.Logger;
import pl.edu.pjwstk.net.TransportPacket;
import pl.edu.pjwstk.net.proto.ProtocolWorker;
import pl.edu.pjwstk.net.proto.SupportedEncryption;
import javax.net.ServerSocketFactory;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.concurrent.*;

public class TCPServer extends Thread {

    private static final Logger LOG = Logger.getLogger(TCPServer.class);

    private boolean isEncrypted = false;

    private ServerSocketFactory socketFactory;

    private ServerSocket serverSocket;

    private final BlockingQueue<TransportPacket> receivedPackets = new LinkedBlockingQueue<TransportPacket>(100);

    private final ThreadPoolExecutor readExecutor = new ThreadPoolExecutor(10, 10, 5, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(1000));

    private final InetAddress address;

    private final int port;

    public TCPServer(InetAddress address, int port) {
        if (LOG.isDebugEnabled()) LOG.debug("Initializing TCPServer");
        this.address = address;
        this.port = port;
        this.socketFactory = ServerSocketFactory.getDefault();
        this.readExecutor.prestartAllCoreThreads();
    }

    public TCPServer(int port) throws UnknownHostException {
        this(InetAddress.getByAddress("0.0.0.0", new byte[] { 0, 0, 0, 0 }), port);
    }

    public TCPServer(InetAddress address, int port, SupportedEncryption encryption, String keysPath, String passphrase) throws CertificateException, IOException, KeyManagementException, KeyStoreException, NoSuchAlgorithmException, UnrecoverableKeyException {
        if (LOG.isDebugEnabled()) LOG.debug("Initializing TCPServer with " + encryption + " encryption");
        this.address = address;
        this.port = port;
        this.isEncrypted = true;
        char[] pass = passphrase.toCharArray();
        SSLContext ctx = SSLContext.getInstance(encryption.toString());
        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        KeyStore ks = KeyStore.getInstance("JKS");
        ks.load(new FileInputStream(keysPath), pass);
        kmf.init(ks, pass);
        ctx.init(kmf.getKeyManagers(), null, null);
        this.socketFactory = ctx.getServerSocketFactory();
        this.readExecutor.prestartAllCoreThreads();
    }

    public TCPServer(int port, SupportedEncryption encryption, String keysPath, String passphrase) throws CertificateException, IOException, KeyManagementException, KeyStoreException, NoSuchAlgorithmException, UnrecoverableKeyException {
        this(InetAddress.getByAddress("0.0.0.0", new byte[] { 0, 0, 0, 0 }), port, encryption, keysPath, passphrase);
    }

    public InetAddress getAddress() {
        if (this.serverSocket == null) {
            return this.address;
        } else {
            return this.serverSocket.getInetAddress();
        }
    }

    public void receivePacket(TransportPacket transportPacket) throws InterruptedException {
        this.receivedPackets.offer(transportPacket, 1, TimeUnit.SECONDS);
    }

    public int getBufferCapacity() {
        return this.receivedPackets.remainingCapacity();
    }

    public TransportPacket getReceivedPacket() throws InterruptedException {
        return this.receivedPackets.poll(100, TimeUnit.MILLISECONDS);
    }

    @Override
    public void run() {
        try {
            this.serverSocket = this.socketFactory.createServerSocket(this.port, 128, this.address);
            if (LOG.isDebugEnabled()) LOG.debug("Starting TCPServer on: " + this.address + ":" + this.port);
            if (this.isEncrypted) {
                ((SSLServerSocket) this.serverSocket).setNeedClientAuth(false);
            }
            while (true) {
                if (isInterrupted()) break;
                Socket socket = null;
                try {
                    socket = serverSocket.accept();
                    socket.setSoTimeout(30000);
                    if (LOG.isDebugEnabled()) LOG.debug("New connection from " + socket.getInetAddress().toString() + ":" + socket.getPort());
                    this.readExecutor.submit(new TCPServerThread(this, socket, socket.getInputStream()));
                } catch (Throwable e) {
                    LOG.error("Error while running TCPServer", e);
                    if (socket != null) {
                        try {
                            socket.close();
                        } catch (IOException ioe) {
                            LOG.error("Error while closing the socket", ioe);
                        }
                    }
                }
            }
        } catch (RejectedExecutionException e) {
            LOG.warn("Could not create new TCPServerThread - queue is full", e);
        } catch (Throwable e) {
            LOG.error("Error while running TCPServer", e);
        }
    }
}

class TCPServerThread extends Thread {

    private static final Logger LOG = Logger.getLogger(TCPServerThread.class);

    private final TCPServer tcpServer;

    private final Socket socket;

    private final InputStream inputStream;

    public TCPServerThread(TCPServer tcpServer, Socket socket, InputStream in) {
        this.tcpServer = tcpServer;
        this.socket = socket;
        this.inputStream = in;
    }

    public void run() {
        if (LOG.isTraceEnabled()) LOG.trace("Starting TCPServerThread");
        ByteArrayOutputStream dataBuffer = new ByteArrayOutputStream();
        try {
            boolean goodPacket = true;
            while (true) {
                if (isInterrupted()) break;
                byte[] readBuffer = new byte[1024];
                byte[] readBufferIntermediate;
                int read = this.inputStream.read(readBuffer);
                if (read == -1) {
                    this.inputStream.close();
                    this.socket.close();
                    break;
                }
                readBufferIntermediate = new byte[read];
                System.arraycopy(readBuffer, 0, readBufferIntermediate, 0, read);
                dataBuffer.write(readBufferIntermediate);
                if (dataBuffer.size() > ProtocolWorker.PACKET_SIZE_MAX) {
                    goodPacket = false;
                    break;
                }
            }
            dataBuffer.close();
            if (goodPacket) {
                int secondsPassed = 0;
                while (this.tcpServer.getBufferCapacity() == 0) {
                    sleep(1000);
                    if (++secondsPassed == 10) {
                        LOG.warn("After 10 seconds buffer is still full. Dropping packet from " + this.socket.getInetAddress());
                        break;
                    }
                }
                this.tcpServer.receivePacket(new TransportPacket(this.socket.getInetAddress(), this.socket.getPort(), dataBuffer.toByteArray()));
            } else {
                LOG.warn("Received packet of size excessing 64kB");
            }
            this.inputStream.close();
        } catch (Throwable e) {
            LOG.error("Error while reading packet", e);
        } finally {
            if (this.socket != null) {
                try {
                    this.socket.close();
                } catch (IOException e) {
                    LOG.error("Error while closing the socket", e);
                }
            }
        }
    }
}
