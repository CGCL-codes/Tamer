package org.opennms.netmgt.provision.detector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import org.opennms.netmgt.provision.support.Client;
import org.opennms.netmgt.provision.support.trustmanager.RelaxedX509TrustManager;

/**
 * @author thedesloge
 *
 */
public class SSLClient extends MultilineOrientedClient implements Client<LineOrientedRequest, MultilineOrientedResponse> {

    public void connect(InetAddress address, int port, int timeout) throws IOException {
        m_socket = getWrappedSocket(address, port, timeout);
        setOutput(m_socket.getOutputStream());
        setInput(new BufferedReader(new InputStreamReader(m_socket.getInputStream())));
    }

    protected Socket getWrappedSocket(InetAddress address, int port, int timeout) throws IOException {
        Socket socket = new Socket();
        socket.connect(new InetSocketAddress(address, port), timeout);
        socket.setSoTimeout(timeout);
        try {
            return wrapSocket(socket, address.getHostAddress(), port);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    protected Socket wrapSocket(Socket socket, String hostAddress, int port) throws Exception {
        Socket sslSocket;
        SSLSocketFactory sslSF = null;
        TrustManager[] tm = { new RelaxedX509TrustManager() };
        SSLContext sslContext = SSLContext.getInstance("SSL");
        sslContext.init(null, tm, new java.security.SecureRandom());
        sslSF = sslContext.getSocketFactory();
        System.out.println("port: " + port);
        sslSocket = sslSF.createSocket(socket, hostAddress, port, true);
        return sslSocket;
    }
}
