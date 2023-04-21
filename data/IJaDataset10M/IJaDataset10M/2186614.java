package org.eclipse.osgi.internal.provisional.verifier;

import java.security.cert.Certificate;

/**
 * This class represents a chain of certificates.
 * <p>
 * <strong>EXPERIMENTAL</strong>. This class or interface has been added as
 * part of a work in progress. There is no guarantee that this API will
 * work or that it will remain the same. Please do not use this API without
 * consulting with the equinox team.
 * </p>
 */
public interface CertificateChain {

    /**
	 * Returns the list of X500 distinguished names that make up the certificate chain. Each
	 * distinguished name is separated by a ';'. The first distinguished name is the signer 
	 * and the last is the root Certificate Authority.
	 * @return the list of X500 distinguished names that make up the certificate chain
	 */
    public String getChain();

    /**
	 * Retruns all certificates in this certificate chain
	 * @return all certificates in this certificate chain
	 */
    public Certificate[] getCertificates();

    /**
	 * Returns the first certificate of the certificate chain
	 * @return the first certificate of the certificate chain
	 */
    public Certificate getSigner();

    /**
	 * Returns the root certificate of the certificate chain
	 * @return the foot certificate of the certificate chain
	 */
    public Certificate getRoot();

    /**
	 * Returns true if this certificate chain is trusted
	 * @return true if this certificate chain is trusted
	 */
    boolean isTrusted();
}
