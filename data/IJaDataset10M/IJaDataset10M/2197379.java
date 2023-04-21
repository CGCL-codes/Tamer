package openr66.protocol.networkhandler.ssl;

import goldengate.common.logging.GgInternalLogger;
import goldengate.common.logging.GgInternalLoggerFactory;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Enumeration;
import javax.net.ssl.KeyManagerFactory;

/**
 * SecureKeySTore for SSL
 *
 * @author The Netty Project (netty@googlegroups.com)
 * @author Trustin Lee (trustin@gmail.com)
 *
 * @version $Rev: 392 $, $Date: 2009-10-06 15:18:33 -0400 (Tue, 06 Oct 2009) $
 *
 */
public class R66SecureKeyStore {

    /**
     * Internal Logger
     */
    private static final GgInternalLogger logger = GgInternalLoggerFactory.getLogger(R66SecureKeyStore.class);

    public static KeyStore keyStore;

    public static KeyManagerFactory keyManagerFactory;

    public static String keyStorePasswd;

    public static String keyPassword;

    public static X509Certificate[] x509Array;

    public static boolean initSecureKeyStore(String filename, String keystorepasswd, String keypassword) {
        try {
            keyStore = KeyStore.getInstance("JKS");
        } catch (KeyStoreException e) {
            logger.error("Cannot create KeyStore Instance", e);
            return false;
        }
        try {
            keyStore.load(new FileInputStream(filename), keystorepasswd.toCharArray());
        } catch (NoSuchAlgorithmException e) {
            logger.error("Cannot create KeyStore Instance", e);
            return false;
        } catch (CertificateException e) {
            logger.error("Cannot create KeyStore Instance", e);
            return false;
        } catch (FileNotFoundException e) {
            logger.error("Cannot create KeyStore Instance", e);
            return false;
        } catch (IOException e) {
            logger.error("Cannot create KeyStore Instance", e);
            return false;
        }
        try {
            keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        } catch (NoSuchAlgorithmException e) {
            logger.error("Cannot create KeyManagerFactory Instance", e);
            return false;
        }
        try {
            keyManagerFactory.init(keyStore, keypassword.toCharArray());
        } catch (UnrecoverableKeyException e) {
            logger.error("Cannot create KeyManagerFactory Instance", e);
            return false;
        } catch (KeyStoreException e) {
            logger.error("Cannot create KeyManagerFactory Instance", e);
            return false;
        } catch (NoSuchAlgorithmException e) {
            logger.error("Cannot create KeyManagerFactory Instance", e);
            return false;
        }
        keyStorePasswd = keystorepasswd;
        keyPassword = keypassword;
        x509Array = getAcceptedIssuers();
        return true;
    }

    /**
     *
     * @return the X509Certificate array of accepted issuers
     */
    public static X509Certificate[] getAcceptedIssuers() {
        if (keyStore != null) {
            Enumeration<String> enumAlias;
            try {
                enumAlias = keyStore.aliases();
            } catch (KeyStoreException e) {
                logger.error("Get Aliases", e);
                return new X509Certificate[0];
            }
            int nb = 0;
            while (enumAlias.hasMoreElements()) {
                nb++;
                enumAlias.nextElement();
            }
            try {
                enumAlias = keyStore.aliases();
            } catch (KeyStoreException e) {
                logger.error("Get Aliases", e);
                return new X509Certificate[0];
            }
            int i = 0;
            X509Certificate[] array = new X509Certificate[nb];
            while (enumAlias.hasMoreElements()) {
                String alias = enumAlias.nextElement();
                try {
                    if (keyStore.isKeyEntry(alias)) {
                        X509Certificate cert = (X509Certificate) keyStore.getCertificate(alias);
                        array[i] = cert;
                        logger.info("New cert: " + alias + " for " + cert.getIssuerDN().toString() + " until " + cert.getNotAfter().toString());
                    }
                } catch (KeyStoreException e) {
                    logger.error("Get Certificate", e);
                    return new X509Certificate[0];
                }
            }
            return array;
        }
        return new X509Certificate[0];
    }

    /**
     * @return the certificate Password
     */
    public static char[] getCertificatePassword() {
        if (keyPassword != null) {
            return keyPassword.toCharArray();
        }
        return "secret".toCharArray();
    }

    /**
     * @return the KeyStore Password
     */
    public static char[] getKeyStorePassword() {
        if (keyStorePasswd != null) {
            return keyStorePasswd.toCharArray();
        }
        return "secret".toCharArray();
    }
}
