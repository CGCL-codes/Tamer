package test.org.tolven.security.key;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import javax.crypto.SecretKey;
import junit.framework.TestCase;
import org.tolven.security.CertificateHelper;
import org.tolven.security.key.AccountPrivateKey;
import org.tolven.security.key.AccountSecretKey;

/**
 * This class is used to testing AccountSecretKey.
 * 
 * @author Joseph Isaac
 * 
 */
public class AccountSecretKeyTestCase extends TestCase {

    public void testGetSecretKey() throws GeneralSecurityException, IOException {
        SecurityTestSuite.initProperties();
        char[] password = "password".toCharArray();
        KeyStore keyStore = CertificateHelperTestCase.getUserPKCS12(CertificateHelperTestCase.UID, password);
        CertificateHelper certHelper = new CertificateHelper();
        PublicKey theUserPublicKey = certHelper.getX509Certificate(keyStore).getPublicKey();
        PrivateKey theUserPrivateKey = certHelper.getPrivateKey(keyStore, password);
        AccountPrivateKey accountPrivateKey = AccountPrivateKey.getInstance();
        String privateKeyAlgorithm = System.getProperty(AccountPrivateKey.ACCOUNT_PRIVATE_KEY_ALGORITHM_PROP);
        int privateKeyLength = Integer.parseInt(System.getProperty(AccountPrivateKey.ACCOUNT_PRIVATE_KEY_LENGTH_PROP));
        String kbeKeyAlgorithm = System.getProperty(AccountSecretKey.ACCOUNT_USER_KBE_KEY_ALGORITHM_PROP);
        int kbeKeyLength = Integer.parseInt(System.getProperty(AccountSecretKey.ACCOUNT_USER_KBE_KEY_LENGTH));
        PublicKey theAccountPublicKey = accountPrivateKey.init(theUserPublicKey, privateKeyAlgorithm, privateKeyLength, kbeKeyAlgorithm, kbeKeyLength);
        PrivateKey theAccountPrivateKey = accountPrivateKey.getPrivateKey(theUserPrivateKey);
        AccountSecretKey accountSecretKey = AccountSecretKey.getInstance();
        kbeKeyAlgorithm = System.getProperty(AccountSecretKey.ACCOUNT_USER_KBE_KEY_ALGORITHM_PROP);
        kbeKeyLength = Integer.parseInt(System.getProperty(AccountSecretKey.ACCOUNT_USER_KBE_KEY_LENGTH));
        SecretKey theOriginalSecretKey = accountSecretKey.init(theAccountPublicKey, kbeKeyAlgorithm, kbeKeyLength);
        SecretKey requestedSecretKey = accountSecretKey.getSecretKey(theAccountPrivateKey);
        assertTrue(requestedSecretKey.equals(theOriginalSecretKey));
    }

    public void testGetInstance() {
        AccountSecretKey.getInstance();
    }

    public void testInitPublicKey() throws GeneralSecurityException, IOException {
        SecurityTestSuite.initProperties();
        char[] password = "password".toCharArray();
        KeyStore keyStore = CertificateHelperTestCase.getUserPKCS12(CertificateHelperTestCase.UID, password);
        CertificateHelper certHelper = new CertificateHelper();
        PublicKey theUserPublicKey = certHelper.getX509Certificate(keyStore).getPublicKey();
        AccountPrivateKey accountPrivateKey = AccountPrivateKey.getInstance();
        String privateKeyAlgorithm = System.getProperty(AccountPrivateKey.ACCOUNT_PRIVATE_KEY_ALGORITHM_PROP);
        int privateKeyLength = Integer.parseInt(System.getProperty(AccountPrivateKey.ACCOUNT_PRIVATE_KEY_LENGTH_PROP));
        String kbeKeyAlgorithm = System.getProperty(AccountSecretKey.ACCOUNT_USER_KBE_KEY_ALGORITHM_PROP);
        int kbeKeyLength = Integer.parseInt(System.getProperty(AccountSecretKey.ACCOUNT_USER_KBE_KEY_LENGTH));
        PublicKey accountPublicKey = accountPrivateKey.init(theUserPublicKey, privateKeyAlgorithm, privateKeyLength, kbeKeyAlgorithm, kbeKeyLength);
        AccountSecretKey accountSecretKey = AccountSecretKey.getInstance();
        kbeKeyAlgorithm = System.getProperty(AccountSecretKey.ACCOUNT_USER_KBE_KEY_ALGORITHM_PROP);
        kbeKeyLength = Integer.parseInt(System.getProperty(AccountSecretKey.ACCOUNT_USER_KBE_KEY_LENGTH));
        SecretKey secretKey = accountSecretKey.init(accountPublicKey, kbeKeyAlgorithm, kbeKeyLength);
        assertTrue(secretKey.getAlgorithm().equals(System.getProperty(AccountSecretKey.ACCOUNT_USER_KBE_KEY_ALGORITHM_PROP)));
    }

    public void testInitInitPublicKey() throws GeneralSecurityException, IOException {
        SecurityTestSuite.initProperties();
        char[] password = "password".toCharArray();
        KeyStore keyStore = CertificateHelperTestCase.getUserPKCS12(CertificateHelperTestCase.UID, password);
        CertificateHelper certHelper = new CertificateHelper();
        PublicKey theUserPublicKey = certHelper.getX509Certificate(keyStore).getPublicKey();
        AccountPrivateKey accountPrivateKey = AccountPrivateKey.getInstance();
        String privateKeyAlgorithm = System.getProperty(AccountPrivateKey.ACCOUNT_PRIVATE_KEY_ALGORITHM_PROP);
        int privateKeyLength = Integer.parseInt(System.getProperty(AccountPrivateKey.ACCOUNT_PRIVATE_KEY_LENGTH_PROP));
        String kbeKeyAlgorithm = System.getProperty(AccountSecretKey.ACCOUNT_USER_KBE_KEY_ALGORITHM_PROP);
        int kbeKeyLength = Integer.parseInt(System.getProperty(AccountSecretKey.ACCOUNT_USER_KBE_KEY_LENGTH));
        PublicKey accountPublicKey = accountPrivateKey.init(theUserPublicKey, privateKeyAlgorithm, privateKeyLength, kbeKeyAlgorithm, kbeKeyLength);
        AccountSecretKey accountSecretKey = AccountSecretKey.getInstance();
        kbeKeyAlgorithm = System.getProperty(AccountSecretKey.ACCOUNT_USER_KBE_KEY_ALGORITHM_PROP);
        kbeKeyLength = Integer.parseInt(System.getProperty(AccountSecretKey.ACCOUNT_USER_KBE_KEY_LENGTH));
        accountSecretKey.init(accountPublicKey, kbeKeyAlgorithm, kbeKeyLength);
        try {
            accountSecretKey.init(accountPublicKey, kbeKeyAlgorithm, kbeKeyLength);
            fail("Intializing twice is not allowed because keys are immutable");
        } catch (IllegalStateException ex) {
        }
    }
}
