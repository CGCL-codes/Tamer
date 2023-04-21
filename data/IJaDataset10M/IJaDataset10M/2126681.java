package org.apache.harmony.crypto.tests.javax.crypto;

import javax.crypto.BadPaddingException;
import junit.framework.TestCase;

/**
 * Tests for <code>BadPaddingException</code> class constructors and methods.
 * 
 */
public class BadPaddingExceptionTest extends TestCase {

    public static void main(String[] args) {
    }

    /**
     * Constructor for BadPaddingExceptionTests.
     * 
     * @param arg0
     */
    public BadPaddingExceptionTest(String arg0) {
        super(arg0);
    }

    static String[] msgs = { "", "Check new message", "Check new message Check new message Check new message Check new message Check new message" };

    static Throwable tCause = new Throwable("Throwable for exception");

    /**
     * Test for <code>BadPaddingException()</code> constructor Assertion:
     * constructs BadPaddingException with no detail message
     */
    public void testBadPaddingException01() {
        BadPaddingException tE = new BadPaddingException();
        assertNull("getMessage() must return null.", tE.getMessage());
        assertNull("getCause() must return null", tE.getCause());
    }

    /**
     * Test for <code>BadPaddingException(String)</code> constructor
     * Assertion: constructs BadPaddingException with detail message msg.
     * Parameter <code>msg</code> is not null.
     */
    public void testBadPaddingException02() {
        BadPaddingException tE;
        for (int i = 0; i < msgs.length; i++) {
            tE = new BadPaddingException(msgs[i]);
            assertEquals("getMessage() must return: ".concat(msgs[i]), tE.getMessage(), msgs[i]);
            assertNull("getCause() must return null", tE.getCause());
        }
    }

    /**
     * Test for <code>BadPaddingException(String)</code> constructor
     * Assertion: constructs BadPaddingException when <code>msg</code> is null
     */
    public void testBadPaddingException03() {
        String msg = null;
        BadPaddingException tE = new BadPaddingException(msg);
        assertNull("getMessage() must return null.", tE.getMessage());
        assertNull("getCause() must return null", tE.getCause());
    }
}
