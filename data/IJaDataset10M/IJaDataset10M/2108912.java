package com.globant.google.mendoza.malbec.transport;

import java.io.InputStream;
import java.security.KeyStore;
import javax.net.ServerSocketFactory;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

/** Helper methods to generate the client and server socket factories from the
 * truststore and keystore files.
 */
public final class SSLUtility {

    /** Private constructor, there must be no instances of this class.
   */
    private SSLUtility() {
    }

    /** Builds a ssl server socket factory.
   *
   * @param keyStoreStream An input stream that contains the key store. This
   * key store must have the certificate to present to the client. The alias is
   * supported at this time. It cannot be null.
   *
   * @param keyStorePassword The password used to decrypt the key store stream.
   * It cannot be null.
   *
   * @param keyPassword The password used to decrypt the certificate stored in
   * the key store stream. It cannot be null.
   *
   * @return Returns a socket factory that can be used to create ssl sockets to
   * use from the server side.
   */
    public static ServerSocketFactory createServerSocketFactory(final InputStream keyStoreStream, final String keyStorePassword, final String keyPassword) {
        if (keyStoreStream == null) {
            throw new IllegalArgumentException("the key store stream cannot be" + " null");
        }
        if (keyStorePassword == null) {
            throw new IllegalArgumentException("the key store password cannot be" + " null");
        }
        if (keyPassword == null) {
            throw new IllegalArgumentException("the key password cannot be null");
        }
        ServerSocketFactory factory = null;
        try {
            KeyStore keyStore = KeyStore.getInstance("JKS");
            keyStore.load(keyStoreStream, keyStorePassword.toCharArray());
            KeyManagerFactory keyManagerFactory;
            keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
            keyManagerFactory.init(keyStore, keyPassword.toCharArray());
            SSLContext context = SSLContext.getInstance("SSLv3");
            context.init(keyManagerFactory.getKeyManagers(), null, null);
            factory = context.getServerSocketFactory();
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Unable to create socket factory", e);
        }
        return factory;
    }

    /** Builds a ssl client socket factory.
   *
   * @param trustStoreStream An input stream that contains the trust store.
   * This trust store must have all the certificates that the client trusts.
   * It cannot be null.
   *
   * @param trustStorePassword The password used to decrypt the trust store
   * stream. It cannot be null.
   *
   * @return Returns a socket factory that can be used to create ssl sockets to
   * use from the client side.
   */
    public static SSLSocketFactory createClientSocketFactory(final InputStream trustStoreStream, final String trustStorePassword) {
        if (trustStoreStream == null) {
            throw new IllegalArgumentException("the trust store stream cannot be" + " null");
        }
        if (trustStorePassword == null) {
            throw new IllegalArgumentException("the trust store password cannot be" + " null");
        }
        SSLSocketFactory factory = null;
        try {
            KeyStore keyStore = KeyStore.getInstance("JKS");
            keyStore.load(trustStoreStream, trustStorePassword.toCharArray());
            TrustManagerFactory trustFactory = TrustManagerFactory.getInstance("SunX509");
            trustFactory.init(keyStore);
            SSLContext sslc = SSLContext.getInstance("SSLv3");
            sslc.init(null, trustFactory.getTrustManagers(), null);
            factory = (SSLSocketFactory) sslc.getSocketFactory();
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Unable to create client socket factory", e);
        }
        return factory;
    }
}
