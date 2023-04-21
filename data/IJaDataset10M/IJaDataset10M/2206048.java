package org.oss.owasp.crypto;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.Certificate;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.owasp.oss.TestBase;
import org.owasp.oss.ca.CertificationAuthority;
import org.owasp.oss.crypto.Crypto;
import org.owasp.oss.crypto.CryptoException;
import org.owasp.oss.crypto.OSSKeyStore;

public class KeyStoreTest extends TestBase {

    OSSKeyStore _store = null;

    public KeyStoreTest() {
        try {
            _store = new OSSKeyStore(_testResourcePath + "testStore.bks", "testpass");
        } catch (CryptoException e) {
            e.printStackTrace();
        }
    }

    protected void setUp() throws Exception {
        super.setUp();
        if (Security.getProvider("BC") == null) Security.addProvider(new BouncyCastleProvider());
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public static Test suite() {
        return new TestSuite(KeyStoreTest.class);
    }

    public void testCreateKeystore() throws Exception {
        KeyPair keyPair = Crypto.generateKeyPair();
        Certificate[] certChain = new Certificate[1];
        certChain[0] = CertificationAuthority.makeDummyCertificate(keyPair.getPrivate(), keyPair.getPublic());
        _store.setKeyEntry("test1", keyPair.getPrivate(), certChain);
        _store.store();
        PrivateKey privKey = _store.getPrivateKey("test1");
        assertEquals(privKey, keyPair.getPrivate());
    }

    public void testRenewKey() throws Exception {
        KeyPair keyPair = Crypto.generateKeyPair();
        Certificate[] certChain = new Certificate[1];
        certChain[0] = CertificationAuthority.makeDummyCertificate(keyPair.getPrivate(), keyPair.getPublic());
        _store.setKeyEntry("test1", keyPair.getPrivate(), certChain);
        _store.store();
        assertTrue(_store.getPrivateKey("test1") != null);
        _store.setKeyEntry("test1", keyPair.getPrivate(), certChain);
        _store.store();
        assertTrue(_store.getPrivateKey("test1") != null);
        assertTrue(_store.getPublicKey("test1") != null);
        assertTrue(_store.getCertificate("test1") != null);
    }

    public void testDeleteKey() throws Exception {
        KeyPair keyPair = Crypto.generateKeyPair();
        Certificate[] certChain = new Certificate[1];
        certChain[0] = CertificationAuthority.makeDummyCertificate(keyPair.getPrivate(), keyPair.getPublic());
        _store.setKeyEntry("test1", keyPair.getPrivate(), certChain);
        _store.store();
        assertTrue(_store.getPrivateKey("test1") != null);
        _store.setKeyEntry("test1", keyPair.getPrivate(), certChain);
        _store.store();
        _store.delete("test1");
        try {
            _store.getPrivateKey("test1");
            assertTrue(false);
        } catch (Exception e) {
        }
    }

    public void genericKeyStoreTest(KeyStore store, String name, KeyPair kp, Certificate[] certChain, String keyPass, String storePass) throws Exception {
        store.load(null, null);
        FileOutputStream os = new FileOutputStream(name);
        store.setKeyEntry("test", kp.getPrivate(), keyPass.toCharArray(), certChain);
        store.store(os, storePass.toCharArray());
        os.close();
        FileInputStream is = new FileInputStream(name);
        store.load(is, storePass.toCharArray());
        if (store.containsAlias("test")) {
            assertEquals(store.getKey("test", keyPass.toCharArray()), kp.getPrivate());
        }
        is.close();
        new File(name).delete();
    }

    public void testKeyStoreFormat() {
    }
}
