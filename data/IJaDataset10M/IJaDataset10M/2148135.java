package org.metastatic.jessie;

import javax.net.ssl.ManagerFactoryParameters;

/**
 * This empty class can be used to initialize {@link
 * javax.net.ssl.KeyManagerFactory} and {@link
 * javax.net.ssl.TrustManagerFactory} instances for the ``JessieX509''
 * algorithm, for cases when no keys or trusted certificates are
 * desired or needed.
 *
 * <p>This is the default manager parameters object used in {@link
 * javax.net.ssl.KeyManagerFactory} instances if no key stores are
 * specified through security properties.
 */
public final class NullManagerParameters implements ManagerFactoryParameters {
}
