package org.apache.harmony.security.tests.java.security.cert;

import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.PublicKey;
import java.security.cert.TrustAnchor;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import javax.security.auth.x500.X500Principal;
import org.apache.harmony.security.tests.support.TestKeyPair;
import org.apache.harmony.security.tests.support.cert.TestUtils;
import junit.framework.TestCase;

/**
 * Unit tests for <code>TrustAnchor</code>
 */
public class TrustAnchor_ImplTest extends TestCase {

    private static final String keyAlg = "DSA";

    private static final String validCaNameRfc2253 = "CN=Test CA," + "OU=Testing Division," + "O=Test It All," + "L=Test Town," + "ST=Testifornia," + "C=Testland";

    /**
     * Test #1 for <code>TrustAnchor(X509Certificate, byte[])</code> constructor<br> 
     * Assertion: creates <code>TrustAnchor</code> instance<br>
     * Test preconditions: valid parameters passed<br>
     * Expected: must pass without any exceptions
     */
    public final void testTrustAnchorX509CertificatebyteArray01() throws KeyStoreException {
        KeyStore ks = TestUtils.getKeyStore(true, TestUtils.TRUSTED);
        if (ks == null) {
            fail(getName() + ": not performed (could not create test KeyStore)");
        }
        String certAlias = "testca1";
        new TrustAnchor((X509Certificate) ks.getCertificate(certAlias), getFullEncoding());
        new TrustAnchor((X509Certificate) ks.getCertificate(certAlias), getEncodingPSOnly());
        new TrustAnchor((X509Certificate) ks.getCertificate(certAlias), getEncodingESOnly());
        new TrustAnchor((X509Certificate) ks.getCertificate(certAlias), getEncodingNoMinMax());
    }

    /**
     * Test #2 for <code>TrustAnchor(X509Certificate, byte[])</code> constructor<br> 
     * Assertion: creates <code>TrustAnchor</code> instance<br>
     * Test preconditions: <code>null</code> as nameConstraints passed<br>
     * Expected: must pass without any exceptions
     */
    public final void testTrustAnchorX509CertificatebyteArray02() throws KeyStoreException {
        KeyStore ks = TestUtils.getKeyStore(true, TestUtils.TRUSTED);
        if (ks == null) {
            fail(getName() + ": not performed (could not create test KeyStore)");
        }
        String certAlias = "testca1";
        new TrustAnchor((X509Certificate) ks.getCertificate(certAlias), null);
    }

    /**
     * Test #3 for <code>TrustAnchor(X509Certificate, byte[])</code> constructor<br> 
     * Assertion: nameConstraints cloned by the constructor<br>
     * Test preconditions: modify passed nameConstraints<br>
     * Expected: modification must not change object internal state
     */
    public final void testTrustAnchorX509CertificatebyteArray03() throws KeyStoreException {
        KeyStore ks = TestUtils.getKeyStore(true, TestUtils.TRUSTED);
        if (ks == null) {
            fail(getName() + ": not performed (could not create test KeyStore)");
        }
        String certAlias = "testca1";
        byte[] nc = getEncodingPSOnly();
        byte[] ncCopy = nc.clone();
        TrustAnchor ta = new TrustAnchor((X509Certificate) ks.getCertificate(certAlias), ncCopy);
        ncCopy[0] = (byte) 0;
        assertTrue(Arrays.equals(nc, ta.getNameConstraints()));
    }

    /**
     * Test #4 for <code>TrustAnchor(X509Certificate, byte[])</code> constructor<br> 
     * Assertion: <code>NullPointerException</code> if <code>X509Certificate</code>
     * parameter is <code>null</code><br>
     * Test preconditions: pass <code>null</code> as <code>X509Certificate</code><br>
     * Expected: NullPointerException
     */
    public final void testTrustAnchorX509CertificatebyteArray04() throws KeyStoreException {
        KeyStore ks = TestUtils.getKeyStore(true, TestUtils.TRUSTED);
        if (ks == null) {
            fail(getName() + ": not performed (could not create test KeyStore)");
        }
        try {
            new TrustAnchor(null, getFullEncoding());
            fail("NullPointerException has not been thrown");
        } catch (NullPointerException ok) {
        }
    }

    /**
     * Test #5 for <code>TrustAnchor(X509Certificate, byte[])</code> constructor<br> 
     * Assertion: <code>IllegalArgumentException</code> if nameConstraints
     * parameter can not be decoded<br>
     * Test preconditions: pass invalid nameConstraints encoding<br>
     * Expected: IllegalArgumentException
     */
    public final void testTrustAnchorX509CertificatebyteArray05() throws KeyStoreException {
        KeyStore ks = TestUtils.getKeyStore(true, TestUtils.TRUSTED);
        if (ks == null) {
            fail(getName() + ": not performed (could not create test KeyStore)");
        }
        String certAlias = "testca1";
        byte[] nameConstraints = getFullEncoding();
        nameConstraints[2] = (byte) 0x8d;
        try {
            new TrustAnchor((X509Certificate) ks.getCertificate(certAlias), nameConstraints);
            fail("IllegalArgumentException has not been thrown");
        } catch (IllegalArgumentException ok) {
        }
        nameConstraints = getFullEncoding();
        nameConstraints[2] = (byte) 0x8b;
        try {
            new TrustAnchor((X509Certificate) ks.getCertificate(certAlias), nameConstraints);
            fail("IllegalArgumentException has not been thrown");
        } catch (IllegalArgumentException ok) {
        }
        nameConstraints = getFullEncoding();
        nameConstraints[3] &= (byte) 0x3f;
        try {
            new TrustAnchor((X509Certificate) ks.getCertificate(certAlias), nameConstraints);
            fail("IllegalArgumentException has not been thrown");
        } catch (IllegalArgumentException ok) {
        }
        nameConstraints = getEncodingESOnly();
        nameConstraints[2] = (byte) 0xa2;
        try {
            new TrustAnchor((X509Certificate) ks.getCertificate(certAlias), nameConstraints);
            fail("IllegalArgumentException has not been thrown");
        } catch (IllegalArgumentException ok) {
        }
        nameConstraints = getEncodingESOnly();
        nameConstraints[2] &= (byte) 0xdf;
        try {
            new TrustAnchor((X509Certificate) ks.getCertificate(certAlias), nameConstraints);
            fail("IllegalArgumentException has not been thrown");
        } catch (IllegalArgumentException ok) {
        }
        nameConstraints = getEncodingESOnly();
        nameConstraints[5] |= (byte) 0x20;
        try {
            new TrustAnchor((X509Certificate) ks.getCertificate(certAlias), nameConstraints);
            fail("IllegalArgumentException has not been thrown");
        } catch (IllegalArgumentException ok) {
        }
        nameConstraints = getEncodingESOnly();
        nameConstraints[12] = nameConstraints[13] = nameConstraints[14] = (byte) 0x6f;
        try {
            new TrustAnchor((X509Certificate) ks.getCertificate(certAlias), nameConstraints);
            fail("IllegalArgumentException has not been thrown");
        } catch (IllegalArgumentException ok) {
        }
    }

    /**
     * Test #6 for <code>TrustAnchor(X509Certificate, byte[])</code> constructor<br> 
     * Assertion: creates <code>TrustAnchor</code> instance<br>
     * Test preconditions: valid parameters passed (base as OID)<br>
     * Expected: must pass without any exceptions
     */
    public final void testTrustAnchorX509CertificatebyteArray06() throws KeyStoreException {
        KeyStore ks = TestUtils.getKeyStore(true, TestUtils.TRUSTED);
        if (ks == null) {
            fail(getName() + ": not performed (could not create test KeyStore)");
        }
        String certAlias = "testca1";
        byte[] nameConstraints = getEncodingOid();
        new TrustAnchor((X509Certificate) ks.getCertificate(certAlias), nameConstraints);
    }

    /**
     * Test #7 for <code>TrustAnchor(X509Certificate, byte[])</code> constructor<br> 
     * Assertion: <code>IllegalArgumentException</code> if nameConstraints
     * parameter can not be decoded<br>
     * Test preconditions: pass invalid nameConstraints (OID) encoding<br>
     * Expected: IllegalArgumentException
     */
    public final void testTrustAnchorX509CertificatebyteArray07() throws KeyStoreException {
        KeyStore ks = TestUtils.getKeyStore(true, TestUtils.TRUSTED);
        if (ks == null) {
            fail(getName() + ": not performed (could not create test KeyStore)");
        }
        String certAlias = "testca1";
        byte[] nameConstraints = getEncodingOid();
        nameConstraints[10] = (byte) 0xFF;
        try {
            new TrustAnchor((X509Certificate) ks.getCertificate(certAlias), nameConstraints);
            fail("IllegalArgumentException has not been thrown");
        } catch (IllegalArgumentException ok) {
        }
    }

    /**
     * Test #8 for <code>TrustAnchor(X509Certificate, byte[])</code> constructor<br> 
     * Assertion: <code>IllegalArgumentException</code> if nameConstraints
     * parameter can not be decoded<br>
     * Test preconditions: pass invalid nameConstraints encodings<br>
     * Expected: IllegalArgumentException
     */
    public final void testTrustAnchorX509CertificatebyteArray08() throws KeyStoreException {
        KeyStore ks = TestUtils.getKeyStore(true, TestUtils.TRUSTED);
        if (ks == null) {
            fail(getName() + ": not performed (could not create test KeyStore)");
        }
        String certAlias = "testca1";
        byte[] generalNameTag = new byte[] { (byte) 0xa0, (byte) 0xa4, (byte) 0xa5, (byte) 0x86, (byte) 0x87, (byte) 0x88 };
        byte[] wrongEncoding = new byte[] { (byte) 0x30, (byte) 0x0c, (byte) 0xa1, (byte) 0x0a, (byte) 0x30, (byte) 0x08, (byte) 0xa0, (byte) 0x03, (byte) 0x01, (byte) 0x01, (byte) 0xff, (byte) 0x80, (byte) 0x01, (byte) 0x00 };
        for (int i = 0; i < generalNameTag.length; i++) {
            wrongEncoding[6] = generalNameTag[i];
            try {
                new TrustAnchor((X509Certificate) ks.getCertificate(certAlias), wrongEncoding);
                fail("IllegalArgumentException has not been thrown for tag " + (generalNameTag[i] & 0xff));
            } catch (IllegalArgumentException ok) {
            }
        }
    }

    /**
     * Test #9 for <code>TrustAnchor(X509Certificate, byte[])</code> constructor<br> 
     * Assertion: <code>IllegalArgumentException</code> if nameConstraints
     * parameter can not be decoded<br>
     * Test preconditions: pass valid and then invalid nameConstraints encodings
     * (GeneralName choice is [0] OtherName)<br>
     * Expected: no exception for valid encoding and IllegalArgumentException for invalid
     * @throws KeyStoreException
     */
    public final void testTrustAnchorX509CertificatebyteArray09() throws KeyStoreException {
        KeyStore ks = TestUtils.getKeyStore(true, TestUtils.TRUSTED);
        if (ks == null) {
            fail(getName() + ": not performed (could not create test KeyStore)");
        }
        String certAlias = "testca1";
        byte[] encoding = new byte[] { (byte) 0x30, (byte) 0x13, (byte) 0xa1, (byte) 0x11, (byte) 0x30, (byte) 0x0f, (byte) 0xa0, (byte) 0x0a, (byte) 0x06, (byte) 0x03, (byte) 0x00, (byte) 0x01, (byte) 0x02, (byte) 0xA0, (byte) 0x03, 1, 1, (byte) 0xff, (byte) 0x80, (byte) 0x01, (byte) 0x00 };
        try {
            new TrustAnchor((X509Certificate) ks.getCertificate(certAlias), encoding);
        } catch (IllegalArgumentException failed) {
            fail("valid encoding not accepted");
        }
        encoding[13] = 1;
        try {
            new TrustAnchor((X509Certificate) ks.getCertificate(certAlias), encoding);
            fail("invalid encoding accepted");
        } catch (IllegalArgumentException ok) {
        }
    }

    /**
     * Test for <code>getNameConstraints()</code> method<br> 
     * Assertion: returns <code>nameConstraints</code> der encoding<br>
     * Test preconditions: valid nameConstraints parameter passed (not null)<br>
     * Expected: encoding passed to the ctor must match returned one<br>
     * Assertion: returns new <code>nameConstraints</code> der encoding each time<br>
     * Test preconditions: valid nameConstraints parameter passed (not null)<br>
     * Expected: must return new reference each time called
     */
    public final void testGetNameConstraints() throws KeyStoreException {
        KeyStore ks = TestUtils.getKeyStore(true, TestUtils.TRUSTED);
        if (ks == null) {
            fail(getName() + ": not performed (could not create test KeyStore)");
        }
        String certAlias = "testca1";
        byte[] nc = getFullEncoding();
        TrustAnchor ta = new TrustAnchor((X509Certificate) ks.getCertificate(certAlias), nc);
        byte[] ncRet = ta.getNameConstraints();
        assertTrue(Arrays.equals(nc, ncRet));
        assertNotSame(nc, ncRet);
        assertNotSame(ncRet, ta.getNameConstraints());
    }

    /**
     * Test #2 for <code>getCAName()</code> method<br>
     *  
     * Assertion: returns ... <code>null</code> if <code>TrustAnchor</code>
     * was not specified as public key and CA name or CA principal pair<br>
     * Test preconditions: test object is not specified as public key
     * and CA name or CA principal pair<br>
     * Expected: <code>null</code> as return value<br>
     * @throws KeyStoreException
     * 
     */
    public final void testGetCAPublicKey02() throws InvalidKeySpecException, KeyStoreException {
        KeyStore ks = TestUtils.getKeyStore(true, TestUtils.TRUSTED);
        if (ks == null) {
            fail(getName() + ": not performed (could not create test KeyStore)");
        }
        TrustAnchor ta = new TrustAnchor((X509Certificate) ks.getCertificate("testca1"), null);
        assertNull(ta.getCAPublicKey());
    }

    /**
     * Test #2 for <code>getCAName()</code> method<br>
     *  
     * Assertion: returns ... <code>null</code> if <code>TrustAnchor</code>
     * was not specified as public key and CA name or CA principal pair<br>
     * Test preconditions: test object is not specified as public key
     * and CA name or CA principal pair<br>
     * Expected: <code>null</code> as return value<br>
     * @throws KeyStoreException
     */
    public final void testGetCAName02() throws KeyStoreException {
        KeyStore ks = TestUtils.getKeyStore(true, TestUtils.TRUSTED);
        if (ks == null) {
            fail(getName() + ": not performed (could not create test KeyStore)");
        }
        TrustAnchor ta = new TrustAnchor((X509Certificate) ks.getCertificate("testca1"), null);
        assertNull(ta.getCAName());
    }

    /**
     * Test #1 for <code>getCAName()</code> method<br>
     *  
     * Assertion: returns most trusted CA certificate<br>
     * Test preconditions: valid certificate passed to the constructor<br>
     * Expected: the same certificate must be returned by the method<br>
     * @throws KeyStoreException
     * 
     */
    public final void testGetTrustedCert01() throws KeyStoreException {
        KeyStore ks = TestUtils.getKeyStore(true, TestUtils.TRUSTED);
        if (ks == null) {
            fail(getName() + ": not performed (could not create test KeyStore)");
        }
        X509Certificate cert = (X509Certificate) ks.getCertificate("testca1");
        TrustAnchor ta = new TrustAnchor(cert, null);
        assertEquals(cert, ta.getTrustedCert());
    }

    /**
     * Test #2 for <code>getCA()</code> method<br>
     *  
     * Assertion: returns ... <code>null</code> if <code>TrustAnchor</code>
     * was not specified as public key and CA name or CA principal pair<br>
     * Test preconditions: test object is not specified as public key
     * and CA name or CA principal pair<br>
     * Expected: <code>null</code> as return value<br>
     * @throws KeyStoreException
     */
    public final void testGetCA02() throws KeyStoreException {
        KeyStore ks = TestUtils.getKeyStore(true, TestUtils.TRUSTED);
        if (ks == null) {
            fail(getName() + ": not performed (could not create test KeyStore)");
        }
        TrustAnchor ta = new TrustAnchor((X509Certificate) ks.getCertificate("testca1"), null);
        assertNull(ta.getCA());
    }

    /**
     * Test for <code>toString()</code> method<br>
     *  
     * Assertion: returns string representation of this <code>TrustAnchor</code>
     * Test preconditions: several valid test objects created<br>
     * Expected: method returns not <code>null</code> in all cases<br>
     */
    public final void testToString() throws Exception {
        KeyStore ks = TestUtils.getKeyStore(true, TestUtils.TRUSTED);
        if (ks == null) {
            fail(getName() + ": not performed (could not create test KeyStore)");
        }
        String certAlias = "test";
        TrustAnchor ta = new TrustAnchor((X509Certificate) ks.getCertificate(certAlias), getFullEncoding());
        assertNotNull("#1", ta.toString());
        PublicKey pk = new TestKeyPair(keyAlg).getPublic();
        ta = new TrustAnchor(validCaNameRfc2253, pk, getEncodingESOnly());
        assertNotNull("#2", ta.toString());
        X500Principal x500p = new X500Principal(validCaNameRfc2253);
        ta = new TrustAnchor(x500p, pk, getEncodingNoMinMax());
        assertNotNull("#3", ta.toString());
        ta = new TrustAnchor(x500p, pk, null);
        assertNotNull("#4", ta.toString());
    }

    private static final byte[] getFullEncoding() {
        return new byte[] { (byte) 0x30, (byte) 0x81, (byte) 0x8c, (byte) 0xa0, (byte) 0x44, (byte) 0x30, (byte) 0x16, (byte) 0x86, (byte) 0x0e, (byte) 0x66, (byte) 0x69, (byte) 0x6c, (byte) 0x65, (byte) 0x3a, (byte) 0x2f, (byte) 0x2f, (byte) 0x66, (byte) 0x6f, (byte) 0x6f, (byte) 0x2e, (byte) 0x63, (byte) 0x6f, (byte) 0x6d, (byte) 0x80, (byte) 0x01, (byte) 0x00, (byte) 0x81, (byte) 0x01, (byte) 0x01, (byte) 0x30, (byte) 0x16, (byte) 0x86, (byte) 0x0e, (byte) 0x66, (byte) 0x69, (byte) 0x6c, (byte) 0x65, (byte) 0x3a, (byte) 0x2f, (byte) 0x2f, (byte) 0x62, (byte) 0x61, (byte) 0x72, (byte) 0x2e, (byte) 0x63, (byte) 0x6f, (byte) 0x6d, (byte) 0x80, (byte) 0x01, (byte) 0x00, (byte) 0x81, (byte) 0x01, (byte) 0x01, (byte) 0x30, (byte) 0x12, (byte) 0x86, (byte) 0x0a, (byte) 0x66, (byte) 0x69, (byte) 0x6c, (byte) 0x65, (byte) 0x3a, (byte) 0x2f, (byte) 0x2f, (byte) 0x6d, (byte) 0x75, (byte) 0x75, (byte) 0x80, (byte) 0x01, (byte) 0x00, (byte) 0x81, (byte) 0x01, (byte) 0x01, (byte) 0xa1, (byte) 0x44, (byte) 0x30, (byte) 0x16, (byte) 0x86, (byte) 0x0e, (byte) 0x68, (byte) 0x74, (byte) 0x74, (byte) 0x70, (byte) 0x3a, (byte) 0x2f, (byte) 0x2f, (byte) 0x66, (byte) 0x6f, (byte) 0x6f, (byte) 0x2e, (byte) 0x63, (byte) 0x6f, (byte) 0x6d, (byte) 0x80, (byte) 0x01, (byte) 0x00, (byte) 0x81, (byte) 0x01, (byte) 0x01, (byte) 0x30, (byte) 0x16, (byte) 0x86, (byte) 0x0e, (byte) 0x68, (byte) 0x74, (byte) 0x74, (byte) 0x70, (byte) 0x3a, (byte) 0x2f, (byte) 0x2f, (byte) 0x62, (byte) 0x61, (byte) 0x72, (byte) 0x2e, (byte) 0x63, (byte) 0x6f, (byte) 0x6d, (byte) 0x80, (byte) 0x01, (byte) 0x00, (byte) 0x81, (byte) 0x01, (byte) 0x01, (byte) 0x30, (byte) 0x12, (byte) 0x86, (byte) 0x0a, (byte) 0x68, (byte) 0x74, (byte) 0x74, (byte) 0x70, (byte) 0x3a, (byte) 0x2f, (byte) 0x2f, (byte) 0x6d, (byte) 0x75, (byte) 0x75, (byte) 0x80, (byte) 0x01, (byte) 0x00, (byte) 0x81, (byte) 0x01, (byte) 0x01 };
    }

    private static final byte[] getEncodingPSOnly() {
        return new byte[] { (byte) 0x30, (byte) 0x46, (byte) 0xa0, (byte) 0x44, (byte) 0x30, (byte) 0x16, (byte) 0x86, (byte) 0x0e, (byte) 0x66, (byte) 0x69, (byte) 0x6c, (byte) 0x65, (byte) 0x3a, (byte) 0x2f, (byte) 0x2f, (byte) 0x66, (byte) 0x6f, (byte) 0x6f, (byte) 0x2e, (byte) 0x63, (byte) 0x6f, (byte) 0x6d, (byte) 0x80, (byte) 0x01, (byte) 0x00, (byte) 0x81, (byte) 0x01, (byte) 0x01, (byte) 0x30, (byte) 0x16, (byte) 0x86, (byte) 0x0e, (byte) 0x66, (byte) 0x69, (byte) 0x6c, (byte) 0x65, (byte) 0x3a, (byte) 0x2f, (byte) 0x2f, (byte) 0x62, (byte) 0x61, (byte) 0x72, (byte) 0x2e, (byte) 0x63, (byte) 0x6f, (byte) 0x6d, (byte) 0x80, (byte) 0x01, (byte) 0x00, (byte) 0x81, (byte) 0x01, (byte) 0x01, (byte) 0x30, (byte) 0x12, (byte) 0x86, (byte) 0x0a, (byte) 0x66, (byte) 0x69, (byte) 0x6c, (byte) 0x65, (byte) 0x3a, (byte) 0x2f, (byte) 0x2f, (byte) 0x6d, (byte) 0x75, (byte) 0x75, (byte) 0x80, (byte) 0x01, (byte) 0x00, (byte) 0x81, (byte) 0x01, (byte) 0x01 };
    }

    private static final byte[] getEncodingESOnly() {
        return new byte[] { (byte) 0x30, (byte) 0x46, (byte) 0xa1, (byte) 0x44, (byte) 0x30, (byte) 0x16, (byte) 0x86, (byte) 0x0e, (byte) 0x68, (byte) 0x74, (byte) 0x74, (byte) 0x70, (byte) 0x3a, (byte) 0x2f, (byte) 0x2f, (byte) 0x66, (byte) 0x6f, (byte) 0x6f, (byte) 0x2e, (byte) 0x63, (byte) 0x6f, (byte) 0x6d, (byte) 0x80, (byte) 0x01, (byte) 0x00, (byte) 0x81, (byte) 0x01, (byte) 0x01, (byte) 0x30, (byte) 0x16, (byte) 0x86, (byte) 0x0e, (byte) 0x68, (byte) 0x74, (byte) 0x74, (byte) 0x70, (byte) 0x3a, (byte) 0x2f, (byte) 0x2f, (byte) 0x62, (byte) 0x61, (byte) 0x72, (byte) 0x2e, (byte) 0x63, (byte) 0x6f, (byte) 0x6d, (byte) 0x80, (byte) 0x01, (byte) 0x00, (byte) 0x81, (byte) 0x01, (byte) 0x01, (byte) 0x30, (byte) 0x12, (byte) 0x86, (byte) 0x0a, (byte) 0x68, (byte) 0x74, (byte) 0x74, (byte) 0x70, (byte) 0x3a, (byte) 0x2f, (byte) 0x2f, (byte) 0x6d, (byte) 0x75, (byte) 0x75, (byte) 0x80, (byte) 0x01, (byte) 0x00, (byte) 0x81, (byte) 0x01, (byte) 0x01 };
    }

    private static final byte[] getEncodingNoMinMax() {
        return new byte[] { (byte) 0x30, (byte) 0x68, (byte) 0xa0, (byte) 0x32, (byte) 0x30, (byte) 0x10, (byte) 0x86, (byte) 0x0e, (byte) 0x66, (byte) 0x69, (byte) 0x6c, (byte) 0x65, (byte) 0x3a, (byte) 0x2f, (byte) 0x2f, (byte) 0x66, (byte) 0x6f, (byte) 0x6f, (byte) 0x2e, (byte) 0x63, (byte) 0x6f, (byte) 0x6d, (byte) 0x30, (byte) 0x10, (byte) 0x86, (byte) 0x0e, (byte) 0x66, (byte) 0x69, (byte) 0x6c, (byte) 0x65, (byte) 0x3a, (byte) 0x2f, (byte) 0x2f, (byte) 0x62, (byte) 0x61, (byte) 0x72, (byte) 0x2e, (byte) 0x63, (byte) 0x6f, (byte) 0x6d, (byte) 0x30, (byte) 0x0c, (byte) 0x86, (byte) 0x0a, (byte) 0x66, (byte) 0x69, (byte) 0x6c, (byte) 0x65, (byte) 0x3a, (byte) 0x2f, (byte) 0x2f, (byte) 0x6d, (byte) 0x75, (byte) 0x75, (byte) 0xa1, (byte) 0x32, (byte) 0x30, (byte) 0x10, (byte) 0x86, (byte) 0x0e, (byte) 0x68, (byte) 0x74, (byte) 0x74, (byte) 0x70, (byte) 0x3a, (byte) 0x2f, (byte) 0x2f, (byte) 0x66, (byte) 0x6f, (byte) 0x6f, (byte) 0x2e, (byte) 0x63, (byte) 0x6f, (byte) 0x6d, (byte) 0x30, (byte) 0x10, (byte) 0x86, (byte) 0x0e, (byte) 0x68, (byte) 0x74, (byte) 0x74, (byte) 0x70, (byte) 0x3a, (byte) 0x2f, (byte) 0x2f, (byte) 0x62, (byte) 0x61, (byte) 0x72, (byte) 0x2e, (byte) 0x63, (byte) 0x6f, (byte) 0x6d, (byte) 0x30, (byte) 0x0c, (byte) 0x86, (byte) 0x0a, (byte) 0x68, (byte) 0x74, (byte) 0x74, (byte) 0x70, (byte) 0x3a, (byte) 0x2f, (byte) 0x2f, (byte) 0x6d, (byte) 0x75, (byte) 0x75 };
    }

    private static final byte[] getEncodingOid() {
        return new byte[] { (byte) 0x30, (byte) 0x09, (byte) 0xA0, (byte) 0x07, (byte) 0x30, (byte) 0x05, (byte) 0x88, (byte) 0x03, (byte) 0x2A, (byte) 0x03, (byte) 0x04 };
    }
}
