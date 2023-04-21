package org.apache.http.mockup;

import java.net.Socket;
import org.apache.http.conn.scheme.LayeredSocketFactory;

/**
 * {@link LayeredSocketFactory} mockup implementation.
 */
public class SecureSocketFactoryMockup extends SocketFactoryMockup implements LayeredSocketFactory {

    public static final LayeredSocketFactory INSTANCE = new SecureSocketFactoryMockup("INSTANCE");

    public SecureSocketFactoryMockup(String name) {
        super(name);
    }

    @Override
    public String toString() {
        return "SecureSocketFactoryMockup." + mockup_name;
    }

    public Socket createSocket(Socket socket, String host, int port, boolean autoClose) {
        throw new UnsupportedOperationException("I'm a mockup!");
    }
}
