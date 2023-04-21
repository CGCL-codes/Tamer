package org.lnicholls.galleon.togo;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import org.apache.commons.httpclient.ConnectTimeoutException;
import org.apache.commons.httpclient.params.HttpConnectionParams;
import org.apache.commons.httpclient.protocol.SecureProtocolSocketFactory;
import org.apache.log4j.Logger;
import org.lnicholls.galleon.util.Tools;

/**
 * Accept self-signed certificates.
 */
public class TiVoSSLProtocolSocketFactory implements SecureProtocolSocketFactory {

    private static Logger log = Logger.getLogger(TiVoX509TrustManager.class.getName());

    private SSLContext getSSLContext() {
        if (this.mSSLContext == null) {
            this.mSSLContext = createSSLContext();
        }
        return this.mSSLContext;
    }

    public Socket createSocket(String host, int port, InetAddress clientHost, int clientPort) throws IOException, UnknownHostException {
        return getSSLContext().getSocketFactory().createSocket(host, port, clientHost, clientPort);
    }

    public Socket createSocket(String host, int port) throws IOException, UnknownHostException {
        return getSSLContext().getSocketFactory().createSocket(host, port);
    }

    public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException, UnknownHostException {
        return getSSLContext().getSocketFactory().createSocket(socket, host, port, autoClose);
    }

    public Socket createSocket(String host, int port, InetAddress localAddress, int localPort, HttpConnectionParams params) throws IOException, UnknownHostException, ConnectTimeoutException {
        return getSSLContext().getSocketFactory().createSocket(host, port);
    }

    private SSLContext createSSLContext() {
        try {
            SSLContext context = SSLContext.getInstance("SSL");
            context.init(null, new TrustManager[] { new TiVoX509TrustManager() }, null);
            return context;
        } catch (Exception ex) {
            Tools.logException(TiVoSSLProtocolSocketFactory.class, ex);
            throw new RuntimeException(ex.toString());
        }
    }

    private SSLContext mSSLContext;
}
