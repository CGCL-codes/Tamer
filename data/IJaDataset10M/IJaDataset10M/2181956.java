package org.ladon.util;

import java.io.*;
import java.net.*;
import java.rmi.server.*;
import javax.net.ssl.*;
import java.security.KeyStore;
import javax.net.*;
import javax.net.ssl.*;
import javax.security.cert.X509Certificate;
import com.sun.net.ssl.*;

/** This class is used by the rmi system to create a custom socket over which the
 * rmi communication is to be handled.  This particular factory creates an
 * SSL Server Socket.
 * @author Jonathan Shriver-Blake
 * @version .01
 */
public class RMISSLServerSocketFactory implements RMIServerSocketFactory, Serializable {

    boolean debug = false;

    /** This is called by the rmi code to create the custom server socket to be used.
 * @param port The port on which to open the socket
 * @throws IOException Thrown in case of trouble
 * @return Returns an SSL Server Socket
 */
    public ServerSocket createServerSocket(int port) throws IOException {
        ServerSocketFactory serverSocketFactory = null;
        SSLContext context;
        KeyManagerFactory keyManagerFact = null;
        KeyStore keyStore = null;
        try {
            context = SSLContext.getInstance("TLS");
            keyManagerFact = KeyManagerFactory.getInstance("SunX509");
            keyStore = KeyStore.getInstance("JKS");
            char[] password = "passphrase".toCharArray();
            keyStore.load(new FileInputStream("keys"), password);
            if (debug) {
                System.out.println("KeyStore Laoded, preparing to init key" + " manager factory.");
            }
            keyManagerFact.init(keyStore, password);
            if (debug) {
                System.out.println("key Manager factory initalized" + " preparing to initalize context");
            }
            context.init(keyManagerFact.getKeyManagers(), null, null);
            if (debug) {
                System.out.println("context initalized, preparing to " + " get server socket factory.");
            }
            serverSocketFactory = context.getServerSocketFactory();
        } catch (Exception e) {
            e.printStackTrace();
        }
        SSLServerSocket socket = null;
        socket = (SSLServerSocket) serverSocketFactory.createServerSocket(port);
        String ciphers[] = socket.getEnabledCipherSuites();
        if (debug) {
            System.out.println("Enabled Cipher Suites");
            for (int i = 0; i < ciphers.length; i++) {
                System.out.println(ciphers[i]);
            }
        }
        socket.setEnabledCipherSuites(ciphers);
        return (ServerSocket) socket;
    }
}
