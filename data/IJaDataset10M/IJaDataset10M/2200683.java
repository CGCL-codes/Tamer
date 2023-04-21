package javapns.communication;

import java.io.*;
import java.net.*;
import java.security.*;
import javapns.communication.exceptions.*;
import javax.net.ssl.*;
import org.apache.log4j.*;
import org.bouncycastle.jce.provider.*;

/**
 * <h1>Class representing an abstract connection to an Apple server</h1>
 * 
 * Communication protocol differences between Notification and Feedback servers are
 * implemented in {@link javapns.notification.ConnectionToNotificationServer} and {@link javapns.feedback.ConnectionToFeedbackServer}.
 * 
 * @author Sylvain Pedneault
 */
public abstract class ConnectionToAppleServer {

    protected static final Logger logger = Logger.getLogger(ConnectionToAppleServer.class);

    private static final String ALGORITHM = ((Security.getProperty("ssl.KeyManagerFactory.algorithm") == null) ? "sunx509" : Security.getProperty("ssl.KeyManagerFactory.algorithm"));

    private static final String PROTOCOL = "TLS";

    public static final String KEYSTORE_TYPE_PKCS12 = "PKCS12";

    public static final String KEYSTORE_TYPE_JKS = "JKS";

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    private KeyStore keyStore;

    private SSLSocketFactory socketFactory;

    private AppleServer server;

    /**
	 * Builds a connection to an Apple server.
	 * 
	 * @param server connection details
	 * @throws KeystoreException thrown if an error occurs when loading the keystore
	 */
    public ConnectionToAppleServer(AppleServer server) throws KeystoreException {
        this.server = server;
        this.keyStore = KeystoreManager.loadKeystore(server);
    }

    /**
	 * Builds a connection to an Apple server.
	 * 
	 * @param server connection details
	 * @param keystore
	 */
    public ConnectionToAppleServer(AppleServer server, KeyStore keystore) {
        this.server = server;
        this.keyStore = keystore;
    }

    public AppleServer getServer() {
        return server;
    }

    public KeyStore getKeystore() {
        return keyStore;
    }

    public void setKeystore(KeyStore ks) {
        this.keyStore = ks;
    }

    /**
	 * Generic SSLSocketFactory builder
	 * 
	 * @param trustManagers
	 * @return SSLSocketFactory
	 * @throws KeystoreException 
	 */
    protected SSLSocketFactory createSSLSocketFactoryWithTrustManagers(TrustManager[] trustManagers) throws KeystoreException {
        logger.debug("Creating SSLSocketFactory");
        try {
            KeyStore keystore = getKeystore();
            KeyManagerFactory kmf = KeyManagerFactory.getInstance(ALGORITHM);
            try {
                char[] password = KeystoreManager.getKeystorePasswordForSSL(server);
                kmf.init(keystore, password);
            } catch (Exception e) {
                e = KeystoreManager.wrapKeystoreException(e);
                throw e;
            }
            SSLContext sslc = SSLContext.getInstance(PROTOCOL);
            sslc.init(kmf.getKeyManagers(), trustManagers, null);
            return sslc.getSocketFactory();
        } catch (Exception e) {
            throw new KeystoreException("Keystore exception: " + e.getMessage(), e);
        }
    }

    public abstract String getServerHost();

    public abstract int getServerPort();

    /**
	 * Return a SSLSocketFactory for creating sockets to communicate with Apple.
	 * 
	 * @return SSLSocketFactory
	 * @throws KeystoreException
	 */
    public SSLSocketFactory createSSLSocketFactory() throws KeystoreException {
        return createSSLSocketFactoryWithTrustManagers(new TrustManager[] { new ServerTrustingTrustManager() });
    }

    public SSLSocketFactory getSSLSocketFactory() throws KeystoreException {
        if (socketFactory == null) socketFactory = createSSLSocketFactory();
        return socketFactory;
    }

    /**
	 * Create a SSLSocket which will be used to send data to Apple
	 * @return the SSLSocket
	 * @throws KeystoreException 
	 * @throws CommunicationException 
	 */
    public SSLSocket getSSLSocket() throws KeystoreException, CommunicationException {
        SSLSocketFactory socketFactory = getSSLSocketFactory();
        logger.debug("Creating SSLSocket to " + getServerHost() + ":" + getServerPort());
        try {
            if (ProxyManager.isUsingProxy(server)) {
                return tunnelThroughProxy(socketFactory);
            } else {
                return (SSLSocket) socketFactory.createSocket(getServerHost(), getServerPort());
            }
        } catch (Exception e) {
            throw new CommunicationException("Communication exception: " + e, e);
        }
    }

    private SSLSocket tunnelThroughProxy(SSLSocketFactory socketFactory) throws UnknownHostException, IOException {
        SSLSocket socket;
        String tunnelHost = ProxyManager.getProxyHost(server);
        Integer tunnelPort = ProxyManager.getProxyPort(server);
        Socket tunnel = new Socket(tunnelHost, tunnelPort);
        doTunnelHandshake(tunnel, getServerHost(), getServerPort());
        socket = (SSLSocket) socketFactory.createSocket(tunnel, getServerHost(), getServerPort(), true);
        socket.addHandshakeCompletedListener(new HandshakeCompletedListener() {

            public void handshakeCompleted(HandshakeCompletedEvent event) {
                logger.debug("Handshake finished!");
                logger.debug("\t CipherSuite:" + event.getCipherSuite());
                logger.debug("\t SessionId " + event.getSession());
                logger.debug("\t PeerHost " + event.getSession().getPeerHost());
            }
        });
        return socket;
    }

    private void doTunnelHandshake(Socket tunnel, String host, int port) throws IOException {
        OutputStream out = tunnel.getOutputStream();
        StringBuilder header = new StringBuilder();
        header.append("CONNECT " + host + ":" + port + " HTTP/1.0\n");
        header.append("User-Agent: BoardPad Server\n");
        String authorization = ProxyManager.getProxyAuthorization(server);
        if (authorization != null && authorization.length() > 0) {
            header.append("Proxy-Authorization: " + authorization + "\n");
        }
        header.deleteCharAt(header.lastIndexOf("\n"));
        header.append("\r\n\r\n");
        String msg = header.toString();
        byte b[] = null;
        try {
            b = msg.getBytes("ASCII7");
        } catch (UnsupportedEncodingException ignored) {
            b = msg.getBytes();
        }
        out.write(b);
        out.flush();
        byte reply[] = new byte[200];
        int replyLen = 0;
        int newlinesSeen = 0;
        boolean headerDone = false;
        InputStream in = tunnel.getInputStream();
        while (newlinesSeen < 2) {
            int i = in.read();
            if (i < 0) {
                throw new IOException("Unexpected EOF from proxy");
            }
            if (i == '\n') {
                headerDone = true;
                ++newlinesSeen;
            } else if (i != '\r') {
                newlinesSeen = 0;
                if (!headerDone && replyLen < reply.length) {
                    reply[replyLen++] = (byte) i;
                }
            }
        }
        String replyStr;
        try {
            replyStr = new String(reply, 0, replyLen, "ASCII7");
        } catch (UnsupportedEncodingException ignored) {
            replyStr = new String(reply, 0, replyLen);
        }
        if (replyStr.toLowerCase().indexOf("200 connection established") == -1) {
            throw new IOException("Unable to tunnel through. Proxy returns \"" + replyStr + "\"");
        }
    }
}
