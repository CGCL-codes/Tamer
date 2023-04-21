package net.grinder.common;

import javax.net.ssl.SSLContext;

/**
 * Factory for {@link SSLContext}s.
 *
 * @author Philip Aston
 * @version $Revision: 3762 $
 */
public interface SSLContextFactory {

    /**
   * Returns an appropriate JSSE {@link SSLContext}. This can be used
   * to obtain an {@link javax.net.ssl.SSLSocketFactory}.
   *
   * <p>The Grinder optimises client SSL processing to increase the
   * number of simultaneous client threads it is reasonable to run. It
   * uses an insecure source of random information, and does not
   * perform checks on the certificates presented by a server. <b>Do
   * not use The Grinder to implement any SSL communication that you
   * want to be secure.</b></p>
   *
   * @return The SSL context.
   * @exception SSLContextFactoryException If the SSLContext could not
   * be found/created.
   * @see net.grinder.script.SSLControl
   */
    SSLContext getSSLContext() throws SSLContextFactoryException;

    /**
   * Exception that indicates problem creating an SSLContext.
   */
    public static final class SSLContextFactoryException extends GrinderException {

        /**
     * Constructor.
     *
     * @param message Helpful message.
     */
        public SSLContextFactoryException(String message) {
            super(message);
        }

        /**
     * Constructor.
     *
     * @param message Helpful message.
     * @param t A nested <code>Throwable</code>
     */
        public SSLContextFactoryException(String message, Throwable t) {
            super(message, t);
        }
    }
}
