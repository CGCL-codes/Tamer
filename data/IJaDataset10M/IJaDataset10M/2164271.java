package org.apache.harmony.security.tests.java.security;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.Provider;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.DSAParameterSpec;
import java.security.interfaces.DSAPublicKey;
import java.security.interfaces.DSAParams;

public class KeyPairGenerator5Test extends junit.framework.TestCase {

    private class MockKeyPairGenerator extends KeyPairGenerator {

        protected MockKeyPairGenerator(String algorithm) {
            super(algorithm);
        }
    }

    public void test_generateKeyPair() {
        MockKeyPairGenerator mockKeyPairGenerator = new MockKeyPairGenerator("MOCKKEYPAIRGENERATOR");
        assertNull(mockKeyPairGenerator.generateKeyPair());
    }
}
