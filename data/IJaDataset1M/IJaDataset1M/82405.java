package de.ifu.arbeitsgruppenassistent.client.planning.utils;

/**
 *
 * Some of the assert functionality of 1.4 for 1.3 versions of Rapla
 *
 */
public class Assert {

    static String NOT_NULL_ASSERTION = "notNull-Assertion";

    static String IS_TRUE_ASSERTION = "isTrue-Assertion";

    static String ASSERTION_FAIL = "Assertion fail";

    static boolean _bActivate = true;

    public static void notNull(Object obj, String text) {
        if (obj == null && isActivated()) {
            doAssert(getText(NOT_NULL_ASSERTION, text));
        }
    }

    public static void notNull(Object obj) {
        if (obj == null && isActivated()) {
            doAssert(getText(NOT_NULL_ASSERTION, ""));
        }
    }

    public static void isTrue(boolean condition, String text) {
        if (!condition && isActivated()) {
            doAssert(getText(IS_TRUE_ASSERTION, text));
        }
    }

    public static void isTrue(boolean condition) {
        if (!condition && isActivated()) {
            doAssert(getText(IS_TRUE_ASSERTION, ""));
        }
    }

    public static void fail() throws AssertionError {
        doAssert(getText(ASSERTION_FAIL, ""));
    }

    public static void fail(String text) throws AssertionError {
        doAssert(getText(ASSERTION_FAIL, text));
    }

    private static void doAssert(String text) throws AssertionError {
        System.err.println(text);
        throw new AssertionError(text);
    }

    static boolean isActivated() {
        return _bActivate;
    }

    static void setActivated(boolean bActivate) {
        _bActivate = bActivate;
    }

    static String getText(String type, String text) {
        return (type + " failed '" + text + "'");
    }
}
