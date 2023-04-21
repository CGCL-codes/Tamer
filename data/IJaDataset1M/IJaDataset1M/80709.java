package gnu.javax.net.ssl.provider;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.Provider;

/**
 * This is the security provider for Jessie. It implements the following
 * algorithms:
 *
 * <pre>
 * {@link javax.net.ssl.SSLContext}.SSLv3
 * {@link javax.net.ssl.SSLContext}.SSL
 * {@link javax.net.ssl.SSLContext}.TLSv1
 * {@link javax.net.ssl.SSLContext}.TLS
 * {@link javax.net.ssl.KeyManagerFactory}.JessieX509
 * {@link javax.net.ssl.TrustManagerFactory}.JessieX509
 * {@link javax.net.ssl.TrustManagerFactory}.SRP
 * </pre>
 *
 */
public class Jessie extends Provider {

    public static final String VERSION = "1.0.0";

    public static final double VERSION_DOUBLE = 1.0;

    public Jessie() {
        super("Jessie", VERSION_DOUBLE, "Implementing SSLv3, TLSv1 SSL Contexts; X.509 Key Manager Factories;" + System.getProperty("line.separator") + "X.509 and SRP Trust Manager Factories, continuously-seeded secure random.");
        AccessController.doPrivileged(new PrivilegedAction() {

            public Object run() {
                put("SSLContext.SSLv3", Context.class.getName());
                put("Alg.Alias.SSLContext.SSL", "SSLv3");
                put("Alg.Alias.SSLContext.TLSv1", "SSLv3");
                put("Alg.Alias.SSLContext.TLS", "SSLv3");
                put("KeyManagerFactory.JessieX509", X509KeyManagerFactory.class.getName());
                put("TrustManagerFactory.JessieX509", X509TrustManagerFactory.class.getName());
                put("TrustManagerFactory.SRP", SRPTrustManagerFactory.class.getName());
                return null;
            }
        });
    }
}
