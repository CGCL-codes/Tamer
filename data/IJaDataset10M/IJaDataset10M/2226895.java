package tests.security.interfaces;

import dalvik.annotation.BrokenTest;
import dalvik.annotation.TestLevel;
import dalvik.annotation.TestTargetNew;
import dalvik.annotation.TestTargetClass;
import junit.framework.TestCase;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.security.interfaces.DSAPrivateKey;
import java.security.interfaces.DSAPublicKey;
import java.security.spec.DSAParameterSpec;

@TestTargetClass(DSAPublicKey.class)
public class DSAPublicKeyTest extends TestCase {

    /**
     * @tests java.security.interfaces.DSAPublicKey 
     * #getY()
     * test covers following use cases
     *   Case 1: check with predefined p, q, g, x
     *   Case 2: check with random p, q, g, x. It takes some time (up to  
     *           minute)
     */
    @TestTargetNew(level = TestLevel.COMPLETE, notes = "", method = "getY", args = {  })
    public void test_getY() throws Exception {
        KeyPairGenerator keyGen = null;
        KeyPair keys = null;
        DSAPrivateKey priv = null;
        DSAPublicKey publ = null;
        keyGen = KeyPairGenerator.getInstance("DSA");
        keyGen.initialize(new DSAParameterSpec(Util.P, Util.Q, Util.G), new SecureRandom(new MySecureRandomSpi(), null) {
        });
        keys = keyGen.generateKeyPair();
        priv = (DSAPrivateKey) keys.getPrivate();
        publ = (DSAPublicKey) keys.getPublic();
        assertNotNull("Invalid Y value", publ.getY());
        keyGen = KeyPairGenerator.getInstance("DSA");
        keys = keyGen.generateKeyPair();
        priv = (DSAPrivateKey) keys.getPrivate();
        publ = (DSAPublicKey) keys.getPublic();
        assertNotNull("Invalid Y value", publ.getY());
    }
}
