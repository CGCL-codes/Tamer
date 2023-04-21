package org.opennms.core.utils;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

/**
 * @author <a href="mailto:seth@opennms.org">Seth</a>
 * @author <a href="http://www.opennms.org">OpenNMS </a>
 */
public abstract class SocketUtils {

    public static Socket wrapSocketInSslContext(Socket socket) throws IOException {
        return wrapSocketInSslContext(socket, null);
    }

    public static Socket wrapSocketInSslContext(Socket socket, String[] cipherSuites) throws IOException {
        TrustManager[] tm = { new RelaxedX509TrustManager() };
        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, tm, new java.security.SecureRandom());
        } catch (NoSuchAlgorithmException e) {
            LogUtils.errorf(SocketUtils.class, e, "wrapSocket: Error wrapping socket, throwing runtime exception: %s", e.getMessage());
            throw new IllegalStateException("No such algorithm in SSLSocketFactory: " + e);
        } catch (KeyManagementException e) {
            LogUtils.errorf(SocketUtils.class, e, "wrapSocket: Error wrapping socket, throwing runtime exception: %s", e.getMessage());
            throw new IllegalStateException("Key management exception in SSLSocketFactory: " + e);
        }
        SSLSocketFactory socketFactory = sslContext.getSocketFactory();
        InetAddress inetAddress = socket.getInetAddress();
        String hostAddress = InetAddressUtils.str(inetAddress);
        Socket wrappedSocket = socketFactory.createSocket(socket, hostAddress, socket.getPort(), true);
        if (cipherSuites != null && cipherSuites.length > 0) {
            final SSLSocket sslSocket = (SSLSocket) wrappedSocket;
            sslSocket.setEnabledCipherSuites(cipherSuites);
        }
        return wrappedSocket;
    }
}
