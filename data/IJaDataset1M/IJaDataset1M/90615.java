package eu.more.identityproviderservice.internal.certificates;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.cert.CertPath;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.security.auth.x500.X500Principal;
import org.bouncycastle.jce.X509Principal;
import org.bouncycastle.x509.X509V3CertificateGenerator;
import org.soda.dpws.DPWSException;
import eu.more.identityproviderservice.commons.Base64;
import eu.more.identityproviderservice.internal.ioOperations.IOOperations;
import eu.more.identityproviderservice.internal.keys.Keys;

/**
 * 
 * @author Emilio Salazar
 *
 **/
public class Certificates {

    /**
	 * Encoded a certificate into a base 64 coded string
	 * @param ca
	 * @return
	 * @throws DPWSException 
	 */
    public static String encodeCertificate(X509Certificate ca) throws DPWSException {
        byte[] encodedCA = null;
        try {
            encodedCA = ca.getEncoded();
        } catch (CertificateEncodingException e) {
            throw new DPWSException("CertificateEncodingException");
        }
        return Base64.encodeBytes(encodedCA);
    }

    /**
	 * Decodes an encoded ca into X509Certificate instance
	 * @param encodedCertificate
	 * @return
	 * @throws DPWSException 
	 */
    public static X509Certificate decodeCertificate(String encodedCertificate) throws DPWSException {
        byte[] encodedCA = Base64.decode(encodedCertificate);
        return loadCA(encodedCA);
    }

    /**
	 * Load a CA from a ASN1 encoded CA
	 * @param encodedCA
	 * @return
	 * @throws DPWSException 
	 */
    public static X509Certificate loadCA(byte[] encodedCA) throws DPWSException {
        ByteArrayInputStream fis = new ByteArrayInputStream(encodedCA);
        CertificateFactory factory = null;
        X509Certificate loadedCA = null;
        try {
            factory = CertificateFactory.getInstance("X.509");
            loadedCA = (X509Certificate) factory.generateCertificate(fis);
        } catch (CertificateException e) {
            throw new DPWSException("CertificateException");
        }
        return loadedCA;
    }

    /**
     * Load a X509 Certificate form the given path.
     * @param path: Location of the X509 Certificate to load.
     * @throws DPWSException 
     * @returns the CA loaded.
     **/
    public static X509Certificate loadCA(String path) throws DPWSException {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(path);
        } catch (FileNotFoundException e1) {
            throw new DPWSException(path + " could not been located.");
        }
        CertificateFactory factory = null;
        X509Certificate loadedCA = null;
        try {
            factory = CertificateFactory.getInstance("X.509");
            loadedCA = (X509Certificate) factory.generateCertificate(fis);
        } catch (CertificateException e) {
            throw new DPWSException("CertificateException");
        }
        return loadedCA;
    }

    /**
     * Stores the CA provided in the path given. If already exists some
     * file in the path, it is truncated.
     * @param path: Location where the X509 Certificate must be saved.
     * @param certificate: X509 CA to store.
     * @throws DPWSException 
     **/
    public static void saveCA(X509Certificate certificate, String path) throws DPWSException {
        try {
            IOOperations.store(certificate.getEncoded(), path);
        } catch (CertificateEncodingException e) {
            throw new DPWSException("CertificateEncodingException");
        }
    }

    /**
     * This method provided a simplified way of generate a certificate. The certificate
     * is signed with a SHA1 digest of a RSA private key. 
     * @param name: Subject of the certificate
     * @param valityPeriod: It is the number of days in which the new CA will be valid. 
     * @param CA: Issuer's certificate
     * @param prKey: Private key used to sign the new CA
     * @param puKey: Public key used to cipher the new CA
     * @throws DPWSException 
     * @returns the certificate issued
     **/
    public static X509Certificate generateCA(String name, int valityPeriod, X509Certificate CA, PublicKey puKey, PrivateKey prKey) throws DPWSException {
        Calendar calendar = Calendar.getInstance();
        BigInteger serialNumber = new BigInteger(160, new java.util.Random());
        Date initialDate = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, valityPeriod);
        Date expirationDate = calendar.getTime();
        X509V3CertificateGenerator v3CertGenerator = new X509V3CertificateGenerator();
        synchronized (v3CertGenerator) {
            v3CertGenerator.reset();
            v3CertGenerator.setNotBefore(initialDate);
            v3CertGenerator.setNotAfter(expirationDate);
            v3CertGenerator.setSerialNumber(serialNumber);
            v3CertGenerator.setIssuerDN(CA.getSubjectX500Principal());
            v3CertGenerator.setSubjectDN(new X509Principal("CN=" + name));
            v3CertGenerator.setPublicKey(puKey);
            v3CertGenerator.setSignatureAlgorithm("SHA1withRSA");
            try {
                return v3CertGenerator.generate(prKey);
            } catch (CertificateEncodingException e) {
                throw new DPWSException("CertificateEncodingException");
            } catch (InvalidKeyException e) {
                throw new DPWSException("InvalidKeyException");
            } catch (IllegalStateException e) {
                throw new DPWSException("IllegalStateException");
            } catch (NoSuchAlgorithmException e) {
                throw new DPWSException("NoSuchAlgorithmException");
            } catch (SignatureException e) {
                throw new DPWSException("SignatureException");
            }
        }
    }

    /**
     * This method provided a simplified way of generate a certificate. The certificate
     * is signed with a SHA1 digest of a RSA private key. The RSA 1024 bits key pair is 
     * generated by the method.
     * @param name: Subject of the certificate
     * @param valityPeriod: It is the number of days in which the new CA will be valid. 
     * @param CA: Issuer's certificate
	 * @throws DPWSException 
     * @returns the certificate issued
     **/
    public static X509Certificate generateCA(String name, int valityPeriod, X509Certificate CA) throws DPWSException {
        KeyPair keyPair = Keys.keyGenerate(Keys.availableAlgotrithms.RSA, 1024);
        return generateCA(name, valityPeriod, CA, keyPair.getPublic(), keyPair.getPrivate());
    }

    /**
     * This method provided a simplified way of generate a root certificate. The root certificate
     * is signed with a SHA1 digest of a RSA private key.
     * @param name: Subject of the certificate
     * @param valityPeriod: It is the number of days in which the new CA will be valid. 
     * @param prKey: Private key used to sign the new CA
     * @param puKey: Public key used to cipher the new CA
	 * @throws DPWSException 
     * @returns the certificate issued
     **/
    public static X509Certificate generateRootCA(String name, int valityPeriod, PublicKey puKey, PrivateKey prKey) throws DPWSException {
        Calendar calendar = Calendar.getInstance();
        BigInteger serialNumber = new BigInteger(160, new java.util.Random());
        Date initialDate = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, valityPeriod);
        Date expirationDate = calendar.getTime();
        X509V3CertificateGenerator v3CertGenerator = new X509V3CertificateGenerator();
        synchronized (v3CertGenerator) {
            v3CertGenerator.reset();
            v3CertGenerator.setNotBefore(initialDate);
            v3CertGenerator.setNotAfter(expirationDate);
            v3CertGenerator.setSerialNumber(serialNumber);
            X500Principal authName = new X500Principal("CN=" + name);
            v3CertGenerator.setIssuerDN(authName);
            v3CertGenerator.setSubjectDN(new X509Principal("CN=" + name));
            v3CertGenerator.setPublicKey(puKey);
            v3CertGenerator.setSignatureAlgorithm("SHA1withRSA");
            try {
                return v3CertGenerator.generate(prKey);
            } catch (CertificateEncodingException e) {
                throw new DPWSException("CertificateEncodingException");
            } catch (InvalidKeyException e) {
                throw new DPWSException("InvalidKeyException");
            } catch (IllegalStateException e) {
                throw new DPWSException("IllegalStateException");
            } catch (NoSuchAlgorithmException e) {
                throw new DPWSException("NoSuchAlgorithmException");
            } catch (SignatureException e) {
                throw new DPWSException("SignatureException");
            }
        }
    }

    /**
     * This method provided a simplified way of generate a root certificate. The certificate
     * is signed with a SHA1 digest of a RSA private key. The RSA 1024 bits key pair is generated
     * by the method.
     * @param name: Subject of the certificate
     * @param valityPeriod: It is the number of days in which the new CA will be valid. 
	 * @throws DPWSException 
     * @returns the certificate issued
     **/
    public static X509Certificate generateRootCA(String name, int valityPeriod) throws DPWSException {
        KeyPair keyPair = Keys.keyGenerate(Keys.availableAlgotrithms.RSA, 1024);
        return generateRootCA(name, valityPeriod, keyPair.getPublic(), keyPair.getPrivate());
    }

    /**
	 * Build a certificate path with the certificate array provided
	 * @param certArray: certificate array
	 * @return certPAth: A certificate's path 
	 * @throws DPWSException 
	 **/
    public static CertPath generateCertPath(X509Certificate[] certArray) throws DPWSException {
        List<X509Certificate> certList = Arrays.asList(certArray);
        CertificateFactory cf = null;
        try {
            cf = CertificateFactory.getInstance("X.509");
            return cf.generateCertPath(certList);
        } catch (CertificateException e) {
            throw new DPWSException("CertificateException");
        }
    }
}
