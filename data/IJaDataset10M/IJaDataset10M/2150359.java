package tests.security.cert;

import dalvik.annotation.TestTargets;
import dalvik.annotation.TestLevel;
import dalvik.annotation.TestTargetNew;
import dalvik.annotation.TestTargetClass;
import junit.framework.TestCase;
import java.security.cert.CertificateExpiredException;

/**
 * Tests for <code>DigestException</code> class constructors and methods.
 * 
 */
@TestTargetClass(CertificateExpiredException.class)
public class CertificateExpiredExceptionTest extends TestCase {

    static String[] msgs = { "", "Check new message", "Check new message Check new message Check new message Check new message Check new message" };

    static Throwable tCause = new Throwable("Throwable for exception");

    /**
     * Test for <code>CertificateExpiredException()</code> constructor
     * Assertion: constructs CertificateExpiredException with no detail message
     */
    @TestTargetNew(level = TestLevel.COMPLETE, notes = "", method = "CertificateExpiredException", args = {  })
    public void testCertificateExpiredException01() {
        CertificateExpiredException tE = new CertificateExpiredException();
        assertNull("getMessage() must return null.", tE.getMessage());
        assertNull("getCause() must return null", tE.getCause());
    }

    /**
     * Test for <code>CertificateExpiredException(String)</code> constructor
     * Assertion: constructs CertificateExpiredException with detail message
     * msg. Parameter <code>msg</code> is not null.
     */
    @TestTargetNew(level = TestLevel.PARTIAL_COMPLETE, notes = "", method = "CertificateExpiredException", args = { java.lang.String.class })
    public void testCertificateExpiredException02() {
        CertificateExpiredException tE;
        for (int i = 0; i < msgs.length; i++) {
            tE = new CertificateExpiredException(msgs[i]);
            assertEquals("getMessage() must return: ".concat(msgs[i]), tE.getMessage(), msgs[i]);
            assertNull("getCause() must return null", tE.getCause());
        }
    }

    /**
     * Test for <code>CertificateExpiredException(String)</code> constructor
     * Assertion: constructs CertificateExpiredException when <code>msg</code>
     * is null
     */
    @TestTargetNew(level = TestLevel.PARTIAL_COMPLETE, notes = "Verifies null as a parameter.", method = "CertificateExpiredException", args = { java.lang.String.class })
    public void testCertificateExpiredException03() {
        String msg = null;
        CertificateExpiredException tE = new CertificateExpiredException(msg);
        assertNull("getMessage() must return null.", tE.getMessage());
        assertNull("getCause() must return null", tE.getCause());
    }
}
