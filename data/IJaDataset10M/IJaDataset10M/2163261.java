package org.ldaptive.ssl;

import java.security.GeneralSecurityException;
import java.security.KeyStore;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import org.ldaptive.LdapUtils;

/**
 * Provides an SSL context initializer which can use java KeyStores to create
 * key and trust managers.
 *
 * @author  Middleware Services
 * @version  $Revision: 2352 $ $Date: 2012-04-11 10:00:33 -0400 (Wed, 11 Apr 2012) $
 */
public class KeyStoreSSLContextInitializer extends AbstractSSLContextInitializer {

    /** KeyStore used to create trust managers. */
    private KeyStore trustKeystore;

    /** KeyStore used to create key managers. */
    private KeyStore authenticationKeystore;

    /** Password used to access the authentication keystore. */
    private char[] authenticationPassword;

    /**
   * Sets the keystore to use for creating the trust managers.
   *
   * @param  keystore  to set
   */
    public void setTrustKeystore(final KeyStore keystore) {
        trustKeystore = keystore;
    }

    /**
   * Sets the keystore to use for creating the key managers.
   *
   * @param  keystore  to set
   */
    public void setAuthenticationKeystore(final KeyStore keystore) {
        authenticationKeystore = keystore;
    }

    /**
   * Sets the password used for accessing the authentication keystore.
   *
   * @param  password  to use for authentication
   */
    public void setAuthenticationPassword(final char[] password) {
        authenticationPassword = password;
    }

    /** {@inheritDoc} */
    @Override
    public TrustManager[] getTrustManagers() throws GeneralSecurityException {
        TrustManager[] tm = null;
        if (trustKeystore != null) {
            final TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(trustKeystore);
            tm = tmf.getTrustManagers();
        }
        TrustManager[] aggregate;
        if (tm == null) {
            aggregate = super.getTrustManagers() != null ? aggregateTrustManagers(super.getTrustManagers()) : null;
        } else {
            aggregate = aggregateTrustManagers(LdapUtils.concatArrays(tm, super.getTrustManagers()));
        }
        return aggregate;
    }

    /** {@inheritDoc} */
    @Override
    public KeyManager[] getKeyManagers() throws GeneralSecurityException {
        KeyManager[] km = null;
        if (authenticationKeystore != null && authenticationPassword != null) {
            final KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(authenticationKeystore, authenticationPassword);
            km = kmf.getKeyManagers();
        }
        return km;
    }
}
