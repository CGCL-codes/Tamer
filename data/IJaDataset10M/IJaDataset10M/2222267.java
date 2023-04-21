package org.apache.harmony.security.tests.javax.security.cert;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.SignatureException;
import javax.security.cert.Certificate;
import javax.security.cert.CertificateEncodingException;
import javax.security.cert.CertificateException;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 */
public class CertificateTest extends TestCase {

    /**
     * The stub class used for testing of non abstract methods.
     */
    private class TBTCert extends Certificate {

        public byte[] getEncoded() throws CertificateEncodingException {
            return null;
        }

        public void verify(PublicKey key) throws CertificateException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException, SignatureException {
        }

        public void verify(PublicKey key, String sigProvider) throws CertificateException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException, SignatureException {
        }

        public String toString() {
            return null;
        }

        public PublicKey getPublicKey() {
            return null;
        }
    }

    /**
     * equals(Object obj) method testing. Tests the correctness of equal
     * operation: it should be reflexive, symmetric, transitive, consistent
     * and should be false on null object.
     */
    public void testEquals() {
        TBTCert tbt_cert = new TBTCert() {

            public byte[] getEncoded() {
                return new byte[] { 1, 2, 3 };
            }
        };
        TBTCert tbt_cert_1 = new TBTCert() {

            public byte[] getEncoded() {
                return new byte[] { 1, 2, 3 };
            }
        };
        TBTCert tbt_cert_2 = new TBTCert() {

            public byte[] getEncoded() {
                return new byte[] { 1, 2, 3 };
            }
        };
        TBTCert tbt_cert_3 = new TBTCert() {

            public byte[] getEncoded() {
                return new byte[] { 3, 2, 1 };
            }
        };
        assertTrue("The equivalence relation should be reflexive.", tbt_cert.equals(tbt_cert));
        assertEquals("The Certificates with equal encoded form should be equal", tbt_cert, tbt_cert_1);
        assertTrue("The equivalence relation should be symmetric.", tbt_cert_1.equals(tbt_cert));
        assertEquals("The Certificates with equal encoded form should be equal", tbt_cert_1, tbt_cert_2);
        assertTrue("The equivalence relation should be transitive.", tbt_cert.equals(tbt_cert_2));
        assertFalse("Should not be equal to null object.", tbt_cert.equals(null));
        assertFalse("The Certificates with differing encoded form " + "should not be equal", tbt_cert.equals(tbt_cert_3));
        assertFalse("The Certificates should not be equals to the object " + "which is not an instance of Certificate", tbt_cert.equals(new Object()));
    }

    /**
     * hashCode() method testing.
     */
    public void testHashCode() {
        TBTCert tbt_cert = new TBTCert() {

            public byte[] getEncoded() {
                return new byte[] { 1, 2, 3 };
            }
        };
        TBTCert tbt_cert_1 = new TBTCert() {

            public byte[] getEncoded() {
                return new byte[] { 1, 2, 3 };
            }
        };
        assertTrue("Equal objects should have the same hash codes.", tbt_cert.hashCode() == tbt_cert_1.hashCode());
    }

    public static Test suite() {
        return new TestSuite(CertificateTest.class);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }
}
