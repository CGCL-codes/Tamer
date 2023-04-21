package gov.lanl.KeyTools;

import java.security.KeyPair;
import java.security.Principal;
import java.security.cert.Certificate;
import java.security.KeyStore;
import java.security.PrivateKey;
import javax.swing.JTextArea;

/**
 * Defines the facilities needed for keystore operations used by OpenEMed.
 * @author Jim George
 * @version $Revision: 2734 $ $Date: 2003-08-19 16:20:03 -0400 (Tue, 19 Aug 2003) $
 */
public interface KeyAndCertInterface {

    /**
   * Generate a new keypair
   * @return a newly created private/public key pair
   */
    public KeyPair createKeypair();

    /**
   * Set the months for which newly created certificates are valid
   * @param numOfMonths is the number of months to be valid
   */
    public void setMonthsOfCertificate(int numOfMonths);

    /**
   * Set the months for which newly created certificates are valid
   * @param numOfMonths is the number of months to be valid
   */
    public void setMonthsOfCertificate(String numOfMonths);

    /**
   * Set the minimum key length which newly created certificates use
   * @param keyLength is the key length, usually in bits
   */
    public void setMinimumKeyLength(int keyLength);

    /**
   * Set the minimum key length which newly created certificates use
   * @param keyLength is the key length, usually in bits
   */
    public void setMinimumKeyLength(String keyLength);

    /**
   * Set the verbose flag which controls output to Sysout,
   * default is false.
   * @param verbose , if true more output, otherwise less
   */
    public void setVerbose(boolean verbose);

    /**
   * Set the verbose flag which controls output to Sysout,
   * default is false.
   * @param verbose is string representation for true...
   */
    public void setVerbose(String verbose);

    /**
   * Set the text area for message output
   */
    public void setMsgArea(JTextArea inMsgArea);

    /**
   * Create a principal/name/distinguished name for the data.
   * @param ids is an array of known names; fullname_state
   * @param values are the corresponding values for the ids
   * @return a Principal containing selected data
   */
    public Principal createPrincipal(String[] ids, String[] values);

    /**
   * Create a principal/name/distinguished name for the data.
   * @param derString is a string containing a der encoded distinguised name
   * @return a Principal containing selected data
   */
    public Principal createPrincipal(String derString);

    /**
   * Create a certificate for the subject and issued by the issuer.
   * @param subject is the principal data wanting a certificate
   * @param sKP is the keypair of the subject
   * @param issuer is the principal data for the issuer
   * @param iKP is the keypair of the issuer
   * @return a newly constructed certificate
   */
    public Certificate createCertificate(Principal subject, KeyPair sKP, Principal issuer, KeyPair iKP);

    /**
    * Get the principal of the certificate
    * @param cert is the certificate from which to extract the principal
    * @return the principal from the certificate
    */
    public Principal getSubjPrincipal(Certificate cert);

    /**
    * Get the principal of the issuer of the certificate
    * @param cert is the certificate from which to extract the principal
    * @return the principal from the certificate
    */
    public Principal getIssuerPrincipal(Certificate cert);

    /**
     * Get the current algorithm used for signing this certificate
     * @return a string name of the algorithm
     */
    public String getSigningAlgorithm(Certificate cert);

    /**
    * Get the alias for the certificate
    * @param cert is the certificate from which to extract the alias
    * @return a string representing the alias of the certificate
    */
    public String getSubjAlias(Certificate cert);

    /**
    * Get the alias for the issuer of the certificate
    * @param cert is the certificate from which to extract the alias
    * @return a string representing the alias of the issuer
    */
    public String getIssuerAlias(Certificate cert);

    /**
     * Get the expirattion date for the certificate
     * @param cert is the certificate whose date is requested
     * @return a string representation of the expiration data
     */
    public String getExpireDate(Certificate cert);

    /**
     * Set the current algorithm for generating key pairs
     * @param inKeyAlgorithm string name of the algorithm
     */
    public void setKeyAlgorithm(String inKeyAlgorithm);

    /**
     * Get the current algorithm for generating key pairs
     * @return a string name of the algorithm
     */
    public String getKeyAlgorithm();

    public void showPKCS(String pkcsPath, String pkcsPassword);

    /**Set the keystore type to use when creating keystores
     * @param inKeystoreType is the keystore type
     */
    public void setKeystoreType(String inKeystoreType);

    public KeyStore importPKCS(String pkcsPath, String pkcsPassword, String alias, String aliasPass);

    /**Exports the private key and certificate chain to a PKCS12 file
     * @param pk is the private key to put in the PKCS12
     * @param chain is the certificate chain
     * @param pkcsPath is the full pathname for the PKCS file
     * @param pkcsPassword is the password for the PKCS file
     */
    public void exportPKCS(PrivateKey pk, Certificate[] chain, String pkcsPath, String pkcsPassword);

    /**Set the provider for creating keystores
     * @param inKeystoreProvider is the provider
     */
    public void setKeystoreProvider(String inKeystoreProvider);

    /**Create an empty keystore using the provider and type*/
    public KeyStore createKeystore();

    /**
     * Test the date or other validity of the certificate
     * @param cert is the certificate to test for validity
     * @return true if the certificate is valid, else false
     */
    public boolean isValid(Certificate cert);
}
