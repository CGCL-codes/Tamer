package java.security.cert;

import java.util.Collection;

/**
 * The <i>service provider interface</i> (<b>SPI</b>) for the {@link
 * CertStore} class.
 *
 * <p>Providers wishing to implement a CertStore must subclass this
 * class, implementing all the abstract methods. Providers may also
 * implement the {@link CertStoreParameters} interface, if they require
 * parameters.
 *
 * @since JDK 1.4
 * @see CertStore
 * @see CollectionCertStoreParameters
 * @see LDAPCertStoreParameters
 */
public abstract class CertStoreSpi {

    /**
   * Creates a new CertStoreSpi.
   *
   * @param params The parameters to initialize this instance with, or
   *        null if no parameters are required.
   * @throws InvalidAlgorithmParameterException If the specified
   *         parameters are inappropriate for this class.
   */
    public CertStoreSpi(CertStoreParameters params) throws java.security.InvalidAlgorithmParameterException {
        super();
    }

    /**
   * Get the certificates from this store, filtering them through the
   * specified CertSelector.
   *
   * @param selector The CertSelector to filter certificates.
   * @return A (non-null) collection of certificates.
   * @throws CertStoreException If the certificates cannot be retrieved.
   */
    public abstract Collection engineGetCertificates(CertSelector selector) throws CertStoreException;

    /**
   * Get the certificate revocation list from this store, filtering them
   * through the specified CRLSelector.
   *
   * @param selector The CRLSelector to filter certificate revocation
   *        lists.
   * @return A (non-null) collection of certificate revocation list.
   * @throws CertStoreException If the CRLs cannot be retrieved.
   */
    public abstract Collection engineGetCRLs(CRLSelector selector) throws CertStoreException;
}
