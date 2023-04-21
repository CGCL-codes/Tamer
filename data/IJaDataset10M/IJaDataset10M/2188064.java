package javax.net.ssl;

import gnu.java.security.Engine;
import java.lang.reflect.InvocationTargetException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Provider;
import java.security.SecureRandom;
import java.security.Security;

/**
 * A "meta-factory" for protocol-specific socket and server socket
 * factories. This class serves as a clearinghouse for socket
 * factories and cached session contexts for a particular protocol,
 * such as SSLv3.
 *
 * @author Casey Marshall (rsdio@metastatic.org)
 */
public class SSLContext {

    /** Service name for SSL contexts. */
    private static final String SSL_CONTEXT = "SSLContext";

    /** The underlying engine. */
    private final SSLContextSpi ctxSpi;

    /** The provider of the engine class. */
    private final Provider provider;

    /** The protocal name. */
    private final String protocol;

    /**
   * Create a new SSL context.
   *
   * @param ctxSpi The context engine.
   * @param provider The provider of the implementation.
   * @param protocol The name of the SSL protocol.
   */
    protected SSLContext(SSLContextSpi ctxSpi, Provider provider, String protocol) {
        this.ctxSpi = ctxSpi;
        this.provider = provider;
        this.protocol = protocol;
    }

    /**
   * Get an instance of a context for the specified protocol from the
   * first provider that implements it.
   *
   * @param protocol The name of the protocol to get a context for.
   * @return The new context.
   * @throws NoSuchAlgorithm If no provider implements the given
   *   protocol.
   */
    public static final SSLContext getInstance(String protocol) throws NoSuchAlgorithmException {
        Provider[] provs = Security.getProviders();
        for (int i = 0; i < provs.length; i++) {
            try {
                return getInstance(protocol, provs[i]);
            } catch (NoSuchAlgorithmException ignore) {
            }
        }
        throw new NoSuchAlgorithmException(protocol);
    }

    /**
   * Get an instance of a context for the specified protocol from the
   * named provider.
   *
   * @param protocol The name of the protocol to get a context for.
   * @param provider The name of the provider to get the
   *   implementation from.
   * @return The new context.
   * @throws NoSuchAlgorithmException If the provider does not
   *   implement the given protocol.
   * @throws NoSuchProviderException If the named provider does not
   *   exist.
   * @throws IllegalArgumentException If <i>provider</i> is null.
   */
    public static final SSLContext getInstance(String protocol, String provider) throws NoSuchAlgorithmException, NoSuchProviderException {
        if (provider == null) {
            throw new IllegalArgumentException("null provider");
        }
        Provider p = Security.getProvider(provider);
        if (p == null) {
            throw new NoSuchProviderException(provider);
        }
        return getInstance(protocol, p);
    }

    /**
   * Get an instance of a context for the specified protocol from the
   * specified provider.
   *
   * @param protocol The name of the protocol to get a context for.
   * @param provider The name of the provider to get the
   *   implementation from.
   * @return The new context.
   * @throws NoSuchAlgorithmException If the provider does not
   *   implement the given protocol.
   * @throws IllegalArgumentException If <i>provider</i> is null.
   */
    public static final SSLContext getInstance(String protocol, Provider provider) throws NoSuchAlgorithmException {
        try {
            return new SSLContext((SSLContextSpi) Engine.getInstance(SSL_CONTEXT, protocol, provider), provider, protocol);
        } catch (InvocationTargetException ite) {
            NoSuchAlgorithmException nsae = new NoSuchAlgorithmException(protocol);
            throw (NoSuchAlgorithmException) nsae.initCause(ite);
        } catch (ClassCastException cce) {
            NoSuchAlgorithmException nsae = new NoSuchAlgorithmException(protocol);
            throw (NoSuchAlgorithmException) nsae.initCause(cce);
        }
    }

    /**
   * Returns the set of SSL contexts available for client connections.
   *
   * @return The set of SSL contexts available for client connections.
   */
    public final SSLSessionContext getClientSessionContext() {
        return ctxSpi.engineGetClientSessionContext();
    }

    /**
   * Returns the protocol name of this context.
   *
   * @return The protocol name of this context.
   */
    public final String getProtocol() {
        return protocol;
    }

    /**
   * Returns the provider of this implementation.
   *
   * @return The provider of this implementation.
   */
    public final Provider getProvider() {
        return provider;
    }

    /**
   * Returns the set of SSL contexts available for server connections.
   *
   * @return The set of SSL contexts available for server connections.
   */
    public final SSLSessionContext getServerSessionContext() {
        return ctxSpi.engineGetServerSessionContext();
    }

    /**
   * Returns the factory for server SSL sockets.
   *
   * @return The factory for server SSL sockets.
   */
    public final SSLServerSocketFactory getServerSocketFactory() {
        return ctxSpi.engineGetServerSocketFactory();
    }

    /**
   * Returns the factory for client SSL sockets.
   *
   * @return The factory for client SSL sockets.
   */
    public final SSLSocketFactory getSocketFactory() {
        return ctxSpi.engineGetSocketFactory();
    }

    /**
   * Initializes this context and prepares it for producing socket
   * factories. All of the parameters are optional; default values are
   * used if left unspecified.
   *
   * @param keyManagers The set of key managers to use.
   * @param trustManagers The set of trust managers to use.
   * @param random A source of random bits to use.
   * @throws KeyManagementException If initialization fails.
   */
    public final void init(KeyManager[] keyManagers, TrustManager[] trustManagers, SecureRandom random) throws KeyManagementException {
        ctxSpi.engineInit(keyManagers, trustManagers, random);
    }
}
