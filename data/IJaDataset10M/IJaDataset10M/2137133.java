package org.dbe.identity.utilities;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertPath;
import java.security.cert.CertPathValidator;
import java.security.cert.CertPathValidatorException;
import java.security.cert.CertPathValidatorResult;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.PKIXParameters;
import java.security.cert.X509Certificate;
import java.util.*;
import org.dbe.identity.utilities.interfaces.ICertPathValidator;

/**
 * Wrapper for java.security.cert.CertPathValidator<br />
 * Class is used to validate Certificate Path<br />
 * 
 * @author <a href="mailto:chatark@cs.tcd.ie">Khalid Chatar</a>
 * @author <a href="mailto:Dominik.Dahlem@cs.tcd.ie">Dominik Dahlem</a>
 * @version 1.0
 */
public class DBECertPathValidator implements ICertPathValidator {

    public static String m_strValidatorType = "PKIX";

    public static String m_strCertPathType = "PKCS7";

    public static String m_strCertificateType = "X.509";

    private PKIXParameters m_objPKIXParams;

    private CertPath m_objCertPath;

    private CertPathValidatorResult m_objValidatorResult = null;

    private String m_strFailureCause = null;

    private int m_iCertIndex = -1;

    /**
     * Method used to check whether the given object isn't null. If the object
     * is an instance of String it checks weather the string is !empty as well.<br />
     * 
     * @param p_Obj object, which has to be proved
     * @param p_Num parameter-order of p_Obj
     * 
     * @throws
     * @see <b>IllegalArgumentException </b> if object is null or if object is
     *      an instance of string and this string is empty
     */
    private void checkObject(Object p_Obj, int p_Num) {
        if (p_Obj == null) throw new IllegalArgumentException(p_Num + ". Parameter is null!");
        if ((p_Obj instanceof String) && !Validator.isStringOK((String) p_Obj)) throw new IllegalArgumentException(p_Num + ". Parameter is empty!");
    }

    /**
     * Method will be called by constructors to load keystore with trusted CAs
     * 
     * @throws NoSuchProviderException
     * 
     * @throws <b>KeyStoreException </b> if Keystoretype is not available from
     *             provider
     * @throws <b>NoSuchAlgorithmException </b> if provider has not been
     *             configured
     * @throws <b>CertificateException </b> if any of the certificates in the
     *             keystore could not be loaded
     * @throws <b>FileNotFoundException </b> if the file does not exist, is a
     *             directory rather than a regular file, or for some other
     *             reason cannot be opened for reading
     * @throws <b>IOException </b> if there is an I/O or format problem with the
     *             keystore data, if a password is required but not given, or if
     *             the given password was incorrect
     */
    private KeyStore getKeystore(String p_keystorePATH, String p_keystoreTYPE, String p_keystorePROVIDER, String p_keystorePWD) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, FileNotFoundException, IOException, NoSuchProviderException {
        KeyStore keystore = KeyStore.getInstance(p_keystoreTYPE, p_keystorePROVIDER);
        FileInputStream fis;
        fis = new FileInputStream(p_keystorePATH);
        keystore.load(fis, p_keystorePWD.toCharArray());
        fis.close();
        printList(keystore.aliases());
        return keystore;
    }

    private void printList(Enumeration enumer) {
        while (enumer.hasMoreElements()) {
            System.out.println("" + enumer.nextElement());
        }
    }

    /**
     * Method will be called by constructors to create a certificate path Object
     * by the given path <b>p_certPath</b>
     * 
     * @param p_certPath path to certificate chain in the filesystem
     * @return CertPath object
     * 
     * @throws <b>CertificateException </b> if an exception occurs while
     *             decoding or the encoding requested is not supported
     * @throws <b>FileNotFoundException </b> if the file does not exist, is a
     *             directory rather than a regular file, or for some other
     *             reason cannot be opened for reading
     */
    private CertPath getCertPath(String p_certPath) throws CertificateException, FileNotFoundException {
        FileInputStream fis = null;
        CertificateFactory cf = null;
        try {
            fis = new FileInputStream(p_certPath);
            cf = CertificateFactory.getInstance(m_strCertificateType);
            List list = new ArrayList(cf.generateCertificates(fis));
            return cf.generateCertPath(list);
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("Path " + p_certPath + " isn't correct or certificate does not exist!");
        } catch (CertificateException ex) {
            List list = new ArrayList(cf.generateCertificates(fis));
            return cf.generateCertPath(list);
        }
    }

    /**
     * Method will be called by constructors to create a certificate path Object
     * by the given path <b>p_certPath</b>
     * 
     * @param p_certPath the certificate chain
     * 
     * @return the CertPath instance of the certificate chain
     * 
     * @throws CertificateException if the requested certificate type is not
     *             available in the default provider package or any of the other
     *             provider packages that were searched or if the certificate
     *             path couldn't be generated
     */
    private CertPath getCertPath(Certificate[] p_certPath) throws CertificateException {
        Collection coll = new ArrayList();
        CertificateFactory cf = null;
        cf = CertificateFactory.getInstance(m_strCertificateType);
        for (int i = 0; i < p_certPath.length; i++) {
            coll.add(p_certPath[i]);
        }
        return cf.generateCertPath(new ArrayList(coll));
    }

    /**
     * For debugging-information
     * 
     * @param list list, which includes X509Certificate instances
     */
    private void printList(List list) {
        Iterator it = list.iterator();
        while (it.hasNext()) {
            X509Certificate cert = (X509Certificate) it.next();
            System.out.println("" + cert.getIssuerX500Principal().getName());
        }
    }

    /**
     * Default-Constructor has to be inaccessible because of need of parameters
     */
    private DBECertPathValidator() {
    }

    /**
     * Constructor for initialisation of certification path and PKIX parameters<br />
     * Constructor uses
     * 
     * @see PKIXParameters(Keystore ks) to set trusted CAs, creates
     *      CertificationPath from certificate given by <b>p_CertificatePath</b>
     *      and sets revocation-list-check flag with <b>p_checkRevocation</b>.
     * 
     * @param p_TrustedCAKeystorePath path to keystore saving trusted CAs
     * @param p_keystorePWD password of keystore
     * @param p_keystoreTYPE type of given keystore
     * @param p_CertificatePath path to certificate to validate
     * @param p_checkRevocation revocation check necessary or not?
     * @throws NoSuchProviderException
     * 
     * @throws <b>FileNotFoundException </b> if the file does not exist, is a
     *             directory rather than a regular file, or for some other
     *             reason cannot be opened for reading
     * @throws <b>CertificateException </b> if an exception occurs while
     *             decoding or the encoding requested is not supported
     * @throws <b>NoSuchAlgorithmException </b> if provider has not been
     *             configured
     * @throws <b>InvalidAlgorithmParameterException </b> if the keystore does
     *             not contain at least one trusted certificate entry
     * @throws <b>KeyStoreException </b> if the keystore has not been
     *             initialized
     * @throws <b>IOException </b> if there is an I/O or format problem with the
     *             keystore data, if a password is required but not given, or if
     *             the given password was incorrect
     */
    public DBECertPathValidator(String p_trustedCAKeystorePath, String p_keystoreTYPE, String p_keystorePROVIDER, String p_keystorePWD, String p_certificatePath, boolean p_checkRevocation) throws KeyStoreException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, CertificateException, FileNotFoundException, IOException, NoSuchProviderException {
        checkObject(p_trustedCAKeystorePath, 1);
        checkObject(p_certificatePath, 2);
        m_objPKIXParams = new PKIXParameters(getKeystore(p_trustedCAKeystorePath, p_keystoreTYPE, p_keystorePROVIDER, p_keystorePWD));
        m_objPKIXParams.setRevocationEnabled(p_checkRevocation);
        m_objCertPath = getCertPath(p_certificatePath);
    }

    /**
     * Constructor for initialisation of certification path and PKIX parameters<br />
     * Constructor uses
     * 
     * @see PKIXParameters(Set set) to set trusted CAs, creates
     *      CertificationPath from certificate given by <b>p_CertificatePath</b>
     *      and sets revocation-list-check flag with <b>p_checkRevocation</b>.
     * 
     * @param p_trustedCAs Collection including all trusted CAs
     * @param p_certificatePath path to certificate to validate
     * @param p_checkRevocation revocation check necessary or not?
     * 
     * @throws <b>InvalidAlgorithmParameterException </b>
     * @throws <b>CertificateException </b>
     * @throws <b>FileNotFoundException </b>
     */
    public DBECertPathValidator(Collection p_trustedCAs, String p_certificatePath, boolean p_checkRevocation) throws InvalidAlgorithmParameterException, CertificateException, FileNotFoundException {
        checkObject(p_trustedCAs, 1);
        checkObject(p_certificatePath, 2);
        m_objPKIXParams = new PKIXParameters(new TreeSet(p_trustedCAs));
        m_objPKIXParams.setRevocationEnabled(p_checkRevocation);
        m_objCertPath = getCertPath(p_certificatePath);
    }

    /**
     * Constructor for initialisation of certification path and PKIX parameters<br />
     * Constructor uses
     * 
     * @see PKIXParameters(Set set) to set trusted CAs, creates
     *      CertificationPath from certificate given by <b>p_CertificatePath</b>
     *      and sets revocation-list-check flag with <b>p_checkRevocation</b>.
     * 
     * @param p_trustedCAs Keystore including all trusted CAs
     * @param p_certificatePath certificate chain to validate
     * @param p_checkRevocation revocation check necessary or not?
     * 
     * @throws InvalidAlgorithmParameterException if the keystore does not
     *             contain at least one trusted certificate entry
     * @throws KeyStoreException if the keystore has not been initialized
     * @throws FileNotFoundException
     * 
     * @throws CertificateException
     * 
     * @throws IllegalArgumentException if the first or second parameter is null
     */
    public DBECertPathValidator(KeyStore p_trustedCAs, Certificate[] p_certificatePath, boolean p_checkRevocation) throws KeyStoreException, InvalidAlgorithmParameterException, CertificateException, IllegalArgumentException {
        checkObject(p_trustedCAs, 1);
        checkObject(p_certificatePath, 2);
        m_objPKIXParams = new PKIXParameters(p_trustedCAs);
        m_objPKIXParams.setRevocationEnabled(p_checkRevocation);
        m_objCertPath = getCertPath(p_certificatePath);
    }

    /**
     * @see org.dbe.studio.identity.utilities.interfaces.ICertPathValidator#validate()
     * 
     * <br />
     * Concrete implementation of the method validate() of the interface
     * ICertPathValidator. Validates a given certificate chain with the class
     * CertPathValidator<br />
     * <br />
     * 
     * While validation, it is possible that some exception can appear. Those
     * will bot be thrown! Instead, you can get the error description with
     * @see #getErrorMessage(). If the reason is that the certificate chain
     *      isn't OK, the
     * @see #getCertificateIndex() method delivers the index of the failed
     *      certificate in the chain.
     * 
     * If validation is successful, you can get the CertPathValidatorResult -
     * object with the method
     * @see #getValidatorResult()
     * 
     * @return true, if certificate chain is valid, false otherwise
     */
    public boolean validate() {
        try {
            CertPathValidator cpv = CertPathValidator.getInstance(m_strValidatorType);
            m_objValidatorResult = cpv.validate(m_objCertPath, m_objPKIXParams);
            return true;
        } catch (NoSuchAlgorithmException e) {
            m_strFailureCause = e.getCause().getMessage();
        } catch (CertPathValidatorException e) {
            m_strFailureCause = e.getLocalizedMessage();
            m_iCertIndex = e.getIndex();
        } catch (InvalidAlgorithmParameterException e) {
            m_strFailureCause = e.getLocalizedMessage();
        }
        return false;
    }

    /**
     * If validation fails and the reason is a not valid certificate chain, you
     * will get the index of the not valid certificate in certificate chain,
     * otherwise -1.
     * 
     * @return returns the index of the not valid certificate in certificate
     *         chain or -1 if not set.
     */
    public int getCertificateIndex() {
        return m_iCertIndex;
    }

    /**
     * If validation is successful, you can get the
     * 
     * @see CertPathValidatorResult object
     * 
     * @return returns the ValidatorResult if successful, otherwise null.
     */
    public CertPathValidatorResult getValidatorResult() {
        return m_objValidatorResult;
    }

    /**
     * If validation fails, you can get the error message
     * 
     * @return returns the errormessage or null.
     */
    public String getErrorMessage() {
        return m_strFailureCause;
    }
}
