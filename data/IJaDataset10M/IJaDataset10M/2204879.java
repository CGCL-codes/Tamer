package issrg.security;

import java.security.cert.X509Certificate;

/**
 * This is the interface for verification of signatures on various objects. It 
 * implies that
 * the underlying implementation can operate on a certificate storage to 
 * retrieve certificates.
 * However, the implementations must assume that during the verification 
 * process the whole
 * certification path can be passed to the verifier. Such separation makes the 
 * push model
 * possible.
 *
 * <p>Some implementations may support the push model only (don't return 
 * anything on
 * getVerificationCertificates), but the callers need to be aware of that.
 *
 * @author A.Otenko
 * @version 1.0
 */
public interface Verifier {

    /**
  * This method lets to verify the given signature of the data byte array.
  *
  * <p>The caller must supply all certificates that are relevant to the signer
  * and issuers of that certificate, putting the certificate of the signer
  * the first in the array of certificates. The implementations may retrieve
  * insufficient data (roots of trust, certificate repositories, CRLs) by
  * whatever means available to them; these means are not specified by this
  * interface.
  *
  * <p>It is up to the Verifier to ensure the certificates are not revoked.
  *
  * @param data is the byte array of the signed data
  * @param signature is the byte array of the signature
  * @param certs is the array of certificates available to the caller; the first
  *      certificate is the one used for verifying the signature, the rest will
  *      be used to verify the certification path
  *
  * @return true, if the signature verifies and the certification path can be
  *      established
  *
  * @throws SecurityException if a definite decision cannot be derived
  */
    public boolean verify(byte[] data, byte[] signature, String algorithmID, java.security.cert.X509Certificate[] certs) throws SecurityException;

    /**
   * This method allows the caller to obtain all verification certificates that
   * the Verifier can obtain using its own means. The returned array can be
   * empty.
   *
   * @param signerName is the name of the signer of the data - the name of the
   *      holder of the verification certificates to return, and the locator
   *      of holder's PKCs
   *
   * @return the array of X509 PKCs; if no certificates could be obtained, an
   *      empty array should be returned (invalid name, or the Verifier is too
   *      simple to access any certificate repositories)
   *
   * @throws SecurityException if the result cannot be delivered
   */
    public java.security.cert.X509Certificate[] getVerificationCertificates(issrg.utils.repository.TokenLocator signerName) throws SecurityException;

    /**
   * This method returns the array of PKCs of multiple roots of trust.
   *
   * @return array of X509Certificate, which can be empty or null, if no roots
   *   of trust have been specified
   */
    public X509Certificate[] getRootCAs();

    /**
   * gets the repository that will be used to retrieve user's signature 
   * verification Public Key Certificates. 
   */
    public PKCRepository getPKCRepository();
}
