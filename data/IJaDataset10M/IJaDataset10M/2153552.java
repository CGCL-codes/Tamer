package tests.security.interfaces;

import dalvik.annotation.TestLevel;
import dalvik.annotation.TestTargetNew;
import dalvik.annotation.TestTargetClass;
import junit.framework.TestCase;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPublicKeySpec;

@TestTargetClass(RSAPublicKey.class)
public class RSAPublicKeyTest extends TestCase {

    /**
     * @tests java.security.interfaces.RSAPublicKey
     * #getPublicExponent()
     */
    @TestTargetNew(level = TestLevel.COMPLETE, notes = "", method = "getPublicExponent", args = {  })
    public void test_getPublicExponent() throws Exception {
        KeyFactory gen = KeyFactory.getInstance("RSA");
        final BigInteger n = BigInteger.valueOf(3233);
        final BigInteger e = BigInteger.valueOf(17);
        RSAPublicKey key = (RSAPublicKey) gen.generatePublic(new RSAPublicKeySpec(n, e));
        assertEquals("invalid public exponent", e, key.getPublicExponent());
    }
}
