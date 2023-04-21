package net.sf.jimo.modules.im.api.net.ssl;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import net.sf.jimo.modules.im.api.exception.IMIllegalStateException;
import net.sf.jimo.modules.im.api.net.ProxyInfo;
import net.sf.jimo.modules.im.api.exception.IMIllegalArgumentException;
import net.sf.jimo.modules.im.api.exception.IMUnsupportedOperationException;

/**
 * A factory class that creates SSL sockets that may tunnel through a proxy.
 */
class SSLTunnelSocketFactory extends SSLSocketFactory {

    /**
	 * The default factory used by system. We first create a tunnelling socket
	 * through the proxy and delegate it to this factory for overlaying
	 * SSL on it.
	 */
    private SSLSocketFactory factory;

    /**
	 * The proxy to be used for tunnelling.
	 */
    private ProxyInfo info;

    /**
	 * Constructs a new tunnelling SSL socket factory by specifying the proxy
	 * information and a factory to overlay SSL on the tunnel.
	 *
	 * @param factory The default factory used by system. We first create a
	 *                tunnelling socket through the proxy and delegate it to
	 *                this factory for overlaying SSL on it.
	 * @param info    The proxy to be used for tunnelling.
	 */
    public SSLTunnelSocketFactory(SSLSocketFactory factory, ProxyInfo info) {
        this.info = info;
        this.factory = factory;
    }

    public Socket createSocket(String host, int port) throws IOException, UnknownHostException {
        return createSocket(null, host, port, true);
    }

    public Socket createSocket(String host, int port, InetAddress clientHost, int clientPort) throws IOException, UnknownHostException {
        return createSocket(null, host, port, true);
    }

    public Socket createSocket(InetAddress host, int port) throws IOException {
        return createSocket(null, host.getHostName(), port, true);
    }

    public Socket createSocket(InetAddress address, int port, InetAddress clientAddress, int clientPort) throws IOException {
        return createSocket(null, address.getHostName(), port, true);
    }

    public Socket createSocket(Socket s, String host, int port, boolean autoClose) throws IOException, UnknownHostException {
        Socket tunnel;
        try {
            tunnel = this.info.getConnection(host, port).getSocket();
        } catch (IMIllegalStateException e) {
            e.printStackTrace();
            return null;
        }
        SSLSocket result = (SSLSocket) this.factory.createSocket(tunnel, host, port, autoClose);
        return result;
    }

    public String[] getDefaultCipherSuites() {
        return this.factory.getDefaultCipherSuites();
    }

    public String[] getSupportedCipherSuites() {
        return this.factory.getSupportedCipherSuites();
    }
}
