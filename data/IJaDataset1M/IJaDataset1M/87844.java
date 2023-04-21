package fiswidgets.fisremote;

import java.io.*;
import java.net.*;
import java.rmi.server.*;
import javax.net.*;
import javax.net.ssl.*;
import java.security.*;

public class FisServerSocketFactory implements RMIServerSocketFactory, Serializable {

    private final String keystore = "/fiswidgets.key";

    private final char[] password = "snoopy".toCharArray();

    private static RMISocketFactory defaultFactory = RMISocketFactory.getDefaultSocketFactory();

    private static String ssl = "SSL";

    private String protocol;

    /**
	 * Select the type of socket you wish RMI to use
	 * @param String (enter "SSL" for secure sockets)
	 */
    public FisServerSocketFactory(String protocol) {
        this.protocol = protocol;
    }

    private SSLServerSocketFactory getFactory() {
        SSLServerSocketFactory sslFactory = null;
        try {
            SSLContext ctx = SSLContext.getInstance("TLS");
            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
            KeyStore ks = KeyStore.getInstance("JKS");
            ks.load(getClass().getResourceAsStream(keystore), password);
            kmf.init(ks, password);
            ctx.init(kmf.getKeyManagers(), null, new SecureRandom());
            sslFactory = ctx.getServerSocketFactory();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return sslFactory;
    }

    public ServerSocket createServerSocket(int port) throws IOException {
        if (protocol.equals(ssl)) return getFactory().createServerSocket(port); else return defaultFactory.createServerSocket(port);
    }
}
