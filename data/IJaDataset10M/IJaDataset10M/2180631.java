package openr66.protocol.networkhandler.ssl;

import goldengate.common.logging.GgInternalLogger;
import goldengate.common.logging.GgInternalLoggerFactory;
import java.security.Security;
import javax.net.ssl.SSLContext;

/**
 * SecureSslContextFactory for SSL
 *
 * @author The Netty Project (netty@googlegroups.com)
 * @author Trustin Lee (trustin@gmail.com)
 *
 * @version $Rev: 288 $, $Date: 2009-08-10 22:49:26 +0200 (lun., 10 août 2009)
 *          $
 *
 */
public class SecureSslContextFactory {

    /**
     * Internal Logger
     */
    private static final GgInternalLogger logger = GgInternalLoggerFactory.getLogger(SecureSslContextFactory.class);

    /**
	 *
	 */
    private static final String PROTOCOL = "TLS";

    /**
	 *
	 */
    private static final SSLContext SERVER_CONTEXT;

    /**
	 *
	 */
    private static final SSLContext CLIENT_CONTEXT;

    static {
        String algorithm = Security.getProperty("ssl.KeyManagerFactory.algorithm");
        if (algorithm == null) {
            algorithm = "SunX509";
        }
        SSLContext serverContext = null;
        SSLContext clientContext = null;
        try {
            serverContext = SSLContext.getInstance(PROTOCOL);
            serverContext.init(R66SecureKeyStore.keyManagerFactory.getKeyManagers(), SecureTrustManagerFactory.getTrustManagers(), null);
        } catch (Exception e) {
            logger.error("Failed to initialize the server-side SSLContext", e);
            throw new Error("Failed to initialize the server-side SSLContext", e);
        }
        try {
            clientContext = SSLContext.getInstance(PROTOCOL);
            clientContext.init(R66SecureKeyStore.keyManagerFactory.getKeyManagers(), SecureTrustManagerFactory.getTrustManagers(), null);
        } catch (Exception e) {
            logger.error("Failed to initialize the client-side SSLContext", e);
            throw new Error("Failed to initialize the client-side SSLContext", e);
        }
        SERVER_CONTEXT = serverContext;
        CLIENT_CONTEXT = clientContext;
    }

    /**
     * @return the Server Context
     */
    public static SSLContext getServerContext() {
        return SERVER_CONTEXT;
    }

    /**
     * @return the Client Context
     */
    public static SSLContext getClientContext() {
        return CLIENT_CONTEXT;
    }
}
