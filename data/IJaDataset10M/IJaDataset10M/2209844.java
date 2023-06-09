package java.security.cert;

import java.io.ByteArrayInputStream;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Principal;
import java.security.PublicKey;
import java.security.SignatureException;
import java.util.Date;
import java.util.Set;
import javax.security.auth.x500.X500Principal;
import org.apache.harmony.luni.util.Base64;
import org.apache.harmony.security.tests.support.cert.TestUtils;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * X509CertificateTest
 */
public class X509CertificateTest extends TestCase {

    static String base64cert = "MIIByzCCATagAwIBAgICAiswCwYJKoZIhvcNAQEFMB0xGzAZBgNVBAoT" + "EkNlcnRpZmljYXRlIElzc3VlcjAeFw0wNjA0MjYwNjI4MjJaFw0zMzAz" + "MDExNjQ0MDlaMB0xGzAZBgNVBAoTEkNlcnRpZmljYXRlIElzc3VlcjCB" + "nzANBgkqhkiG9w0BAQEFAAOBjQAwgYkCgYEAkLGLsPdSPDMyP1OUOKu+" + "U3cvbNK5RGaQ3bXc5aDjvApx43BcaoXgt6YD/5yXz0OsIooj5yA37+bY" + "JGcVrvFD5FMPdDd3vjNPQOep0MzG4CdbkaZde5SigPabOMQYS4oUyLBx" + "W3LGG0mUODe5AGGqtqXU0GlKg4K2je6cCtookCUCAwEAAaMeMBwwGgYD" + "VR0RAQH/BBAwDoEMcmZjQDgyMi5OYW1lMAsGCSqGSIb3DQEBBQOBgQBZ" + "pVXj01dOpqnZErU+Qb50j8lJD1dIaz1eJTvJCSadj7ziV1VtnnapI07c" + "XEa7ONzcHQTYTG10poHfOK/a0BaULF3GlctDESilwQYbW5BdfpAlZpbH" + "AFLcUDh6Eq50kc+0A/anh/j3mgBNuvbIMo7hHNnZB6k/prswm2BszyLD" + "yw==";

    static String base64crl = "MIHXMIGXAgEBMAkGByqGSM44BAMwFTETMBEGA1UEChMKQ1JMIElzc3Vl" + "chcNMDYwNDI3MDYxMzQ1WhcNMDYwNDI3MDYxNTI1WjBBMD8CAgIrFw0w" + "NjA0MjcwNjEzNDZaMCowCgYDVR0VBAMKAQEwHAYDVR0YBBUYEzIwMDYw" + "NDI3MDYxMzQ1LjQ2OFqgDzANMAsGA1UdFAQEBAQEBDAJBgcqhkjOOAQD" + "AzAAMC0CFQCk0t0DTyu82QpajbBlxX9uXvUDSgIUSBN4g+xTEeexs/0k" + "9AkjBhjF0Es=";

    private static class MyX509Certificate extends X509Certificate {

        public void checkValidity() throws CertificateExpiredException, CertificateNotYetValidException {
        }

        public void checkValidity(Date date) throws CertificateExpiredException, CertificateNotYetValidException {
        }

        public int getVersion() {
            return 3;
        }

        public BigInteger getSerialNumber() {
            return null;
        }

        public Principal getIssuerDN() {
            return null;
        }

        public Principal getSubjectDN() {
            return null;
        }

        public Date getNotBefore() {
            return null;
        }

        public Date getNotAfter() {
            return null;
        }

        public byte[] getTBSCertificate() throws CertificateEncodingException {
            return null;
        }

        public byte[] getSignature() {
            return null;
        }

        public String getSigAlgName() {
            return null;
        }

        public String getSigAlgOID() {
            return null;
        }

        public byte[] getSigAlgParams() {
            return null;
        }

        public boolean[] getIssuerUniqueID() {
            return null;
        }

        public boolean[] getSubjectUniqueID() {
            return null;
        }

        public boolean[] getKeyUsage() {
            return null;
        }

        public int getBasicConstraints() {
            return 0;
        }

        public void verify(PublicKey key) throws CertificateException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException, SignatureException {
        }

        public void verify(PublicKey key, String sigProvider) throws CertificateException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException, SignatureException {
        }

        public String toString() {
            return "";
        }

        public PublicKey getPublicKey() {
            return null;
        }

        public byte[] getEncoded() throws CertificateEncodingException {
            return null;
        }

        public Set getNonCriticalExtensionOIDs() {
            return null;
        }

        public Set getCriticalExtensionOIDs() {
            return null;
        }

        public byte[] getExtensionValue(String oid) {
            return null;
        }

        public boolean hasUnsupportedCriticalExtension() {
            return false;
        }
    }

    /**
     * @tests java.security.cert.X509Certificate#getType()
     */
    public void testGetType() {
        assertEquals("X.509", new MyX509Certificate().getType());
    }

    /**
     * @tests java.security.cert.X509Certificate#getIssuerX500Principal()
     */
    public void testGetIssuerX500Principal() {
        MyX509Certificate cert = new MyX509Certificate() {

            public byte[] getEncoded() {
                return TestUtils.getX509Certificate_v1();
            }

            ;
        };
        assertEquals(new X500Principal("CN=Z"), cert.getIssuerX500Principal());
    }

    /**
     * @tests java.security.cert.X509Certificate#getSubjectX500Principal()
     */
    public void testGetSubjectX500Principal() {
        MyX509Certificate cert = new MyX509Certificate() {

            public byte[] getEncoded() {
                return TestUtils.getX509Certificate_v1();
            }

            ;
        };
        assertEquals(new X500Principal("CN=Y"), cert.getSubjectX500Principal());
    }

    /**
     * @tests java.security.cert.X509Certificate#getExtendedKeyUsage()
     */
    public void testGetExtendedKeyUsage() throws CertificateParsingException {
        assertNull(new MyX509Certificate().getExtendedKeyUsage());
    }

    /**
     * @tests java.security.cert.X509Certificate#getSubjectAlternativeNames()
     */
    public void testGetSubjectAlternativeNames() throws CertificateParsingException {
        assertNull(new MyX509Certificate().getSubjectAlternativeNames());
    }

    /**
     * @tests java.security.cert.X509Certificate#getIssuerAlternativeNames()
     */
    public void testGetIssuerAlternativeNames() throws CertificateParsingException {
        assertNull(new MyX509Certificate().getIssuerAlternativeNames());
    }

    /**
     * @tests java.security.cert.X509Certificate#getExtensionValue()
     */
    public void testGetExtensionValue() throws Exception {
        ByteArrayInputStream is = null;
        CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
        is = new ByteArrayInputStream(Base64.decode(base64cert.getBytes("UTF-8")));
        X509Certificate cert = (X509Certificate) certFactory.generateCertificate(is);
        cert.getExtensionValue("1.1.1.1");
        is = new ByteArrayInputStream(Base64.decode(base64crl.getBytes("UTF-8")));
        X509CRL crl = (X509CRL) certFactory.generateCRL(is);
        crl.getExtensionValue("1.1.1.1");
    }

    public static Test suite() {
        return new TestSuite(X509CertificateTest.class);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }
}
