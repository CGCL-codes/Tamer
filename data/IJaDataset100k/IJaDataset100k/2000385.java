package avoware.intchat.server.security.ssl;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import javax.net.SocketFactory;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

/**
 *
 * @author Andrew Orlov
 */
public class NaiveSSLSocketFactory extends SSLSocketFactory {

    private static SSLSocketFactory sslSocketFactory = null;

    @Override
    public String[] getDefaultCipherSuites() {
        return getSocketFactory().getDefaultCipherSuites();
    }

    @Override
    public String[] getSupportedCipherSuites() {
        return getSocketFactory().getSupportedCipherSuites();
    }

    @Override
    public Socket createSocket(Socket arg0, String arg1, int arg2, boolean arg3) throws IOException {
        return getSocketFactory().createSocket(arg0, arg1, arg2, arg3);
    }

    @Override
    public Socket createSocket(String arg0, int arg1) throws IOException, UnknownHostException {
        return getSocketFactory().createSocket(arg0, arg1);
    }

    @Override
    public Socket createSocket(String arg0, int arg1, InetAddress arg2, int arg3) throws IOException, UnknownHostException {
        return getSocketFactory().createSocket(arg0, arg1, arg2, arg3);
    }

    @Override
    public Socket createSocket(InetAddress arg0, int arg1) throws IOException {
        return getSocketFactory().createSocket(arg0, arg1);
    }

    @Override
    public Socket createSocket(InetAddress arg0, int arg1, InetAddress arg2, int arg3) throws IOException {
        return getSocketFactory().createSocket(arg0, arg1, arg2, arg3);
    }

    public static SocketFactory getDefault() {
        return new NaiveSSLSocketFactory();
    }

    private static final SSLSocketFactory getSocketFactory() {
        if (sslSocketFactory == null) {
            try {
                TrustManager[] tm = new TrustManager[] { new NaiveTrustManager() };
                SSLContext context = SSLContext.getInstance("SSL");
                context.init(new KeyManager[0], tm, new SecureRandom());
                sslSocketFactory = (SSLSocketFactory) context.getSocketFactory();
            } catch (KeyManagementException e) {
                System.err.println("No SSL algorithm support: " + e.getMessage());
            } catch (NoSuchAlgorithmException e) {
                System.err.println("Exception when setting up the Naive key management.");
            }
        }
        return sslSocketFactory;
    }
}
