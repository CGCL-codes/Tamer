package tests.security.cert;

import dalvik.annotation.TestTargets;
import dalvik.annotation.TestLevel;
import dalvik.annotation.TestTargetNew;
import dalvik.annotation.TestTargetClass;
import junit.framework.TestCase;
import java.security.cert.CertificateParsingException;

/**
 * Tests for <code>CertificateParsingException</code> class constructors and
 * methods.
 * 
 */
@TestTargetClass(CertificateParsingException.class)
public class CertificateParsingExceptionTest extends TestCase {

    private static String[] msgs = { "", "Check new message", "Check new message Check new message Check new message Check new message Check new message" };

    private static Throwable tCause = new Throwable("Throwable for exception");

    /**
     * Test for <code>CertificateParsingException()</code> constructor
     * Assertion: constructs CertificateParsingException with no detail message
     */
    @TestTargetNew(level = TestLevel.COMPLETE, notes = "", method = "CertificateParsingException", args = {  })
    public void testCertificateParsingException01() {
        CertificateParsingException tE = new CertificateParsingException();
        assertNull("getMessage() must return null.", tE.getMessage());
        assertNull("getCause() must return null", tE.getCause());
    }

    /**
     * Test for <code>CertificateParsingException(String)</code> constructor
     * Assertion: constructs CertificateParsingException with detail message
     * msg. Parameter <code>msg</code> is not null.
     */
    @TestTargetNew(level = TestLevel.PARTIAL_COMPLETE, notes = "", method = "CertificateParsingException", args = { java.lang.String.class })
    public void testCertificateParsingException02() {
        CertificateParsingException tE;
        for (int i = 0; i < msgs.length; i++) {
            tE = new CertificateParsingException(msgs[i]);
            assertEquals("getMessage() must return: ".concat(msgs[i]), tE.getMessage(), msgs[i]);
            assertNull("getCause() must return null", tE.getCause());
        }
    }

    /**
     * Test for <code>CertificateParsingException(String)</code> constructor
     * Assertion: constructs CertificateParsingException when <code>msg</code>
     * is null
     */
    @TestTargetNew(level = TestLevel.PARTIAL_COMPLETE, notes = "Verifies null as a parameter.", method = "CertificateParsingException", args = { java.lang.String.class })
    public void testCertificateParsingException03() {
        String msg = null;
        CertificateParsingException tE = new CertificateParsingException(msg);
        assertNull("getMessage() must return null.", tE.getMessage());
        assertNull("getCause() must return null", tE.getCause());
    }

    /**
     * Test for <code>CertificateParsingException(Throwable)</code>
     * constructor Assertion: constructs CertificateParsingException when
     * <code>cause</code> is null
     */
    @TestTargetNew(level = TestLevel.PARTIAL_COMPLETE, notes = "Verifies null as a parameter.", method = "CertificateParsingException", args = { java.lang.Throwable.class })
    public void testCertificateParsingException04() {
        Throwable cause = null;
        CertificateParsingException tE = new CertificateParsingException(cause);
        assertNull("getMessage() must return null.", tE.getMessage());
        assertNull("getCause() must return null", tE.getCause());
    }

    /**
     * Test for <code>CertificateParsingException(Throwable)</code>
     * constructor Assertion: constructs CertificateParsingException when
     * <code>cause</code> is not null
     */
    @TestTargetNew(level = TestLevel.PARTIAL_COMPLETE, notes = "", method = "CertificateParsingException", args = { java.lang.Throwable.class })
    public void testCertificateParsingException05() {
        CertificateParsingException tE = new CertificateParsingException(tCause);
        if (tE.getMessage() != null) {
            String toS = tCause.toString();
            String getM = tE.getMessage();
            assertTrue("getMessage() should contain ".concat(toS), (getM.indexOf(toS) != -1));
        }
        assertNotNull("getCause() must not return null", tE.getCause());
        assertEquals("getCause() must return ".concat(tCause.toString()), tE.getCause(), tCause);
    }

    /**
     * Test for <code>CertificateParsingException(String, Throwable)</code>
     * constructor Assertion: constructs CertificateParsingException when
     * <code>cause</code> is null <code>msg</code> is null
     */
    @TestTargetNew(level = TestLevel.PARTIAL_COMPLETE, notes = "Verifies null as parameters.", method = "CertificateParsingException", args = { java.lang.String.class, java.lang.Throwable.class })
    public void testCertificateParsingException06() {
        CertificateParsingException tE = new CertificateParsingException(null, null);
        assertNull("getMessage() must return null", tE.getMessage());
        assertNull("getCause() must return null", tE.getCause());
    }

    /**
     * Test for <code>CertificateParsingException(String, Throwable)</code>
     * constructor Assertion: constructs CertificateParsingException when
     * <code>cause</code> is null <code>msg</code> is not null
     */
    @TestTargetNew(level = TestLevel.PARTIAL_COMPLETE, notes = "Verifies null as the second parameter.", method = "CertificateParsingException", args = { java.lang.String.class, java.lang.Throwable.class })
    public void testCertificateParsingException07() {
        CertificateParsingException tE;
        for (int i = 0; i < msgs.length; i++) {
            tE = new CertificateParsingException(msgs[i], null);
            assertEquals("getMessage() must return: ".concat(msgs[i]), tE.getMessage(), msgs[i]);
            assertNull("getCause() must return null", tE.getCause());
        }
    }

    /**
     * Test for <code>CertificateParsingException(String, Throwable)</code>
     * constructor Assertion: constructs CertificateParsingException when
     * <code>cause</code> is not null <code>msg</code> is null
     */
    @TestTargetNew(level = TestLevel.PARTIAL_COMPLETE, notes = "Verifies null as the first parameter.", method = "CertificateParsingException", args = { java.lang.String.class, java.lang.Throwable.class })
    public void testCertificateParsingException08() {
        CertificateParsingException tE = new CertificateParsingException(null, tCause);
        if (tE.getMessage() != null) {
            String toS = tCause.toString();
            String getM = tE.getMessage();
            assertTrue("getMessage() must should ".concat(toS), (getM.indexOf(toS) != -1));
        }
        assertNotNull("getCause() must not return null", tE.getCause());
        assertEquals("getCause() must return ".concat(tCause.toString()), tE.getCause(), tCause);
    }

    /**
     * Test for <code>CertificateParsingException(String, Throwable)</code>
     * constructor Assertion: constructs CertificateParsingException when
     * <code>cause</code> is not null <code>msg</code> is not null
     */
    @TestTargetNew(level = TestLevel.PARTIAL_COMPLETE, notes = "Verifies positive functionality.", method = "CertificateParsingException", args = { java.lang.String.class, java.lang.Throwable.class })
    public void testCertificateParsingException09() {
        CertificateParsingException tE;
        for (int i = 0; i < msgs.length; i++) {
            tE = new CertificateParsingException(msgs[i], tCause);
            String getM = tE.getMessage();
            String toS = tCause.toString();
            if (msgs[i].length() > 0) {
                assertTrue("getMessage() must contain ".concat(msgs[i]), getM.indexOf(msgs[i]) != -1);
                if (!getM.equals(msgs[i])) {
                    assertTrue("getMessage() should contain ".concat(toS), getM.indexOf(toS) != -1);
                }
            }
            assertNotNull("getCause() must not return null", tE.getCause());
            assertEquals("getCause() must return ".concat(tCause.toString()), tE.getCause(), tCause);
        }
    }
}
