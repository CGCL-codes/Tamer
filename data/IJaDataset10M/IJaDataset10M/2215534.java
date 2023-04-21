package junit.extensions;

import junit.framework.ComparisonFailure;

/**
 * Static convenience assertions.
 * @author Tom Roche
 * @version $Id: AssertUtils.java 1 2006-06-06 03:55:36Z twall $
 */
public class AssertUtils extends junit.framework.Assert {

    public static final String copyright = "Licensed Materials	-- Property of IBM\n" + "(c) Copyright International Business Machines Corporation, 2003\nUS Government " + "Users Restricted Rights - Use, duplication or disclosure restricted by GSA " + "ADP Schedule Contract with IBM Corp.";

    /**
	 * There must be a better way to do this ...
	 */
    protected static final String FQNAME = "junit.extensions.Assert";

    /**
	 * Protect ctor: static-only class
	 */
    protected AssertUtils() {
    }

    /**
	 * Asserts that two Strings are equal, optionally trimming whitespace.
	 * Note that, even with trim=true, null and a whitespace-only String
	 * are still unequal. 
	 * @param trim leading and trailing whitespace
	 */
    public static void assertEquals(String expected, String actual, boolean trim) {
        assertEquals(null, expected, actual, trim);
    }

    /**
	 * Asserts that two Strings are equal, optionally trimming whitespace.
	 * Note that, even with trim=true, null and a whitespace-only String
	 * are still unequal. 
	 * @param trim leading and trailing whitespace
	 */
    public static void assertEquals(String message, String expected, String actual, boolean trim) {
        if (!trim) assertEquals(message, expected, actual);
        if (expected == null && actual == null) return;
        if (((expected == null) && (actual != null)) || ((actual == null) && (expected != null))) throw new ComparisonFailure(message, expected, actual);
        if (trim) {
            expected = expected.trim();
            actual = actual.trim();
        }
        String error = FQNAME + ".assertEquals(String, String, String, boolean): INTERNAL ERROR";
        assertNotNull(error, expected);
        assertNotNull(error, actual);
        assertEquals(message, expected, actual);
    }
}
