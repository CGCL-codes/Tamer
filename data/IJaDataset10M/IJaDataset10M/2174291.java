package common;

import java.io.*;
import java.net.*;
import java.rmi.server.*;
import javax.net.ssl.*;
import java.security.KeyStore;
import javax.net.ssl.*;

/**
 * RMI SSL socket factory for server side.
 * 
 * @deprecated Not used.
 * @author Miguel Svensson
 * @version 0.1 2006-05-18
 */
public class RMISSLServerSocketFactory implements RMIServerSocketFactory {

    private SSLServerSocketFactory ssf = null;

    public RMISSLServerSocketFactory() throws Exception {
        try {
            SSLContext ctx;
            KeyManagerFactory kmf;
            KeyStore ks;
            File curdir = new File(".");
            System.out.println(curdir.getCanonicalPath());
            char[] passphrase = "passphrase".toCharArray();
            ks = KeyStore.getInstance("JKS");
            ks.load(new FileInputStream("testkeys"), passphrase);
            kmf = KeyManagerFactory.getInstance("SunX509");
            kmf.init(ks, passphrase);
            ctx = SSLContext.getInstance("TLS");
            ctx.init(kmf.getKeyManagers(), null, null);
            ssf = ctx.getServerSocketFactory();
        } catch (Exception e) {
            System.out.println("SocketFactory error: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public ServerSocket createServerSocket(int port) throws IOException {
        return ssf.createServerSocket(port);
    }

    public int hashCode() {
        return getClass().hashCode();
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        return true;
    }
}
