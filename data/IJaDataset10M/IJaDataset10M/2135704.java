package org.rip.ssl;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.security.GeneralSecurityException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.SecureRandom;
import java.security.Security;
import java.security.cert.X509Certificate;
import javax.net.ssl.HandshakeCompletedEvent;
import javax.net.ssl.HandshakeCompletedListener;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import org.rip.keystore.KeyStoreUtils;
import org.rip.keystore.RipTrustManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SSLUtils {

    private static Logger LOG = LoggerFactory.getLogger(SSLUtils.class);

    static class CustomHandshakeCompletedListener implements HandshakeCompletedListener {

        @Override
        public void handshakeCompleted(HandshakeCompletedEvent event) {
            LOG.info("handshakeCompleted");
            LOG.info("cipher suite:" + event.getCipherSuite());
        }
    }

    public static SSLSocket wrapClientSocket(SSLContext sslContext, Socket socket, String algorithm) {
        RipTrustManager tm = (RipTrustManager) TrustManagerUtils.getDefaultTrustManager();
        SSLSocket sslSocket = null;
        try {
            SSLSocketFactory factory = sslContext.getSocketFactory();
            sslSocket = (SSLSocket) factory.createSocket(socket, socket.getInetAddress().getHostAddress(), socket.getPort(), false);
            sslSocket.setEnableSessionCreation(true);
            sslSocket.setUseClientMode(true);
            sslSocket.addHandshakeCompletedListener(new CustomHandshakeCompletedListener());
            sslSocket.startHandshake();
        } catch (Exception she) {
            LOG.error("SSL Exception:", she);
        }
        X509Certificate[] chain = tm.getChain();
        KeyStoreUtils.updateKeyStore(socket.getInetAddress().getHostAddress(), chain);
        return sslSocket;
    }

    public static SSLSocket createClientSocket(SSLContext sslContext, Socket socket, String server, int port) {
        SSLSocket sslSocket = null;
        try {
            SSLSocketFactory factory = sslContext.getSocketFactory();
            sslSocket = (SSLSocket) factory.createSocket(socket, server, socket.getPort(), false);
        } catch (Exception she) {
            LOG.error("SSL Exception:", she);
        }
        return sslSocket;
    }

    public static SSLContext createSSLContext(String protocol, KeyManager[] keyManagers, TrustManager[] trustManagers) {
        Provider[] ps = Security.getProviders();
        for (Provider p : ps) {
            LOG.info(p.getName() + " " + p.getVersion() + " " + p.getInfo());
        }
        SSLContext ctx = null;
        try {
            String secureRandomAlgorithm = "SHA1PRNG";
            String secureRandomProvider = "SUN";
            SecureRandom secureRandom = SecureRandom.getInstance(secureRandomAlgorithm, secureRandomProvider);
            ctx = SSLContext.getInstance(protocol);
            ctx.init(keyManagers, trustManagers, secureRandom);
        } catch (Exception e) {
            LOG.error("Exception creating SSL Context", e);
        }
        return ctx;
    }
}
