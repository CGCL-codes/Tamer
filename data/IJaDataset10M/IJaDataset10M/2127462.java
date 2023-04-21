package xades4j.providers;

import java.security.MessageDigest;
import xades4j.UnsupportedAlgorithmException;

/**
 * Interface for providers of message digest engines. A default implementation
 * is provided.
 * @see xades4j.providers.impl.DefaultMessageDigestProvider
 * @author Luís
 */
public interface MessageDigestEngineProvider {

    /**
     * Gets a {@code MessageDigest} engine for the algorithm identified by the
     * given URI. The URIs defined in the XML-DSIG specification are used.
     *
     * @param digestAlgorithmURI the URI of the digest algorithm
     * @return the message digest engine
     * @throws UnsupportedAlgorithmException if the current provider doesn't support
     *          the specified algorithm URI or there is no provider in the platform
     *          for the corresponding algorithm name
     */
    MessageDigest getEngine(String digestAlgorithmURI) throws UnsupportedAlgorithmException;
}
