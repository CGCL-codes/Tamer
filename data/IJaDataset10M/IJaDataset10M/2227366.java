package org.vramework.commons.junit;

import java.util.Calendar;
import junit.framework.AssertionFailedError;
import junit.framework.TestCase;
import org.vramework.commons.datatypes.Strings;
import org.vramework.commons.datatypes.VCalendar;

/**
 * Base classe.
 */
public abstract class XTestCaseBase extends TestCase {

    private boolean allowProduction = false;

    /**
   * Constructor called by the JUnit framework.
   * @param arg0
   */
    public XTestCaseBase(String arg0) {
        super(arg0);
    }

    /**
   * Constructor to create a test case from your own test code.
   */
    public XTestCaseBase() {
        super("XTTestCase " + Strings.getCurrentMsTestString());
    }

    public void allowProduction(boolean allow) {
        allowProduction = allow;
    }

    public boolean isAllowProduction() {
        return allowProduction;
    }

    public void allowProduction() {
        allowProduction(true);
    }

    public void forbidProduction() {
        allowProduction(false);
    }

    /**
   * Asserts that values[] contains value.
   * @param values
   * @param value
   * @throws AssertionFailedError
   */
    public static void assertContains(String[] values, String value) throws AssertionFailedError {
        assertContains("List of values should contain '" + value + "' but doesn't.", values, value);
    }

    /**
   * Asserts that values[] contains value. If it doesn't
   * an AssertionFailedError is thrown with the given message.
   * @param msg
   * @param values
   * @param value
   * @throws AssertionFailedError
   */
    public static void assertContains(String msg, String[] values, String value) throws AssertionFailedError {
        if (!Strings.contains(values, value)) {
            throw new AssertionFailedError(msg);
        }
    }

    /**
   * Asserts that s contains part.
   * @param s
   * @param part
   * @throws AssertionFailedError
   */
    public static void assertContains(String s, String part) throws AssertionFailedError {
        assertContains("'" + s + "' doesnt't contain '" + part + "'.", s, part);
    }

    /**
   * Asserts that s contains part. If it doesn't an
   * AssertionFailedError is thrown with the given message.
   * @param msg
   * @param s
   * @param part
   * @throws AssertionFailedError
   */
    public static void assertContains(String msg, String s, String part) throws AssertionFailedError {
        if (s == null || s.length() == 0 || s.indexOf(part) == -1) {
            throw new AssertionFailedError(msg);
        }
    }

    /**
   * compares the given Calendar objects using PinkCalender::equalsRoundedToMinutes
   * @param c1
   * @param c2
   */
    public static void assertEquals(Calendar c1, Calendar c2) {
        assertTrue(VCalendar.equalsRoundedToMinutes(c1, c2));
    }
}
