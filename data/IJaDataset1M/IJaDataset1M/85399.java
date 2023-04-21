package org.apache.harmony.crypto.tests.javax.crypto;

import javax.crypto.SecretKey;
import junit.framework.TestCase;

/**
 * Tests for <code>SecretKey</code> class field
 * 
 */
public class SecretKeyTest extends TestCase {

    /**
     * Constructor for SecretKeyTest.
     * 
     * @param arg0
     */
    public SecretKeyTest(String arg0) {
        super(arg0);
    }

    /**
     * Test for <code>serialVersionUID</code> field
     */
    public void testField() {
        checkSecretKey sk = new checkSecretKey();
        assertEquals("Incorrect serialVersionUID", sk.getSerVerUID(), -4795878709595146952L);
    }

    public class checkSecretKey implements SecretKey {

        public String getAlgorithm() {
            return "SecretKey";
        }

        public String getFormat() {
            return "Format";
        }

        public byte[] getEncoded() {
            return new byte[0];
        }

        public long getSerVerUID() {
            return serialVersionUID;
        }
    }
}
