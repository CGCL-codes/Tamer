package org.proteusframework.core.util;

import java.util.List;

/**
 * Convenience class for asserting various states.
 *
 * @author Tacoma Four
 */
public final class Assert {

    /**
     * Utility class.
     */
    private Assert() {
    }

    /**
     * Asserts that the given object is not <code>null</code>. If this
     * is not the case, some kind of unchecked exception is thrown.
     *
     * @param object the value to tests
     */
    public static void isNotNull(Object object) {
        if (object == null) {
            throw new AssertionFailedException("Assert.isNotNull(object) failed");
        }
    }

    /**
     * Asserts that the given object is not <code>null</code>. If this
     * is not the case, some kind of unchecked exception is thrown.
     * The given message is included in that exception, to aid debugging.
     *
     * @param object  the value to tests
     * @param message the message to include in the exception
     */
    public static void parameterNotNull(Object object, String message) {
        if (object == null) {
            throw new IllegalArgumentException("null argument:" + message);
        }
    }

    /**
     * Asserts that the array is of a specific size.
     *
     * @param array   Array to inspect
     * @param length  Expected size
     * @param message the message to include in the exception
     */
    public static void assertSize(byte[] array, int length, String message) {
        if (array.length != length) {
            throw new AssertionFailedException(message);
        }
    }

    /**
     * Asserts that the list meets a minimum size requirement.
     *
     * @param list    List to evaluate
     * @param minSize Expected minimal size of the list
     * @param message the message to include in the exception
     */
    public static void isMinSize(List<?> list, int minSize, String message) {
        if (list.size() < minSize) {
            throw new AssertionFailedException(message);
        }
    }

    /**
     * Asserts that the list contains at least the specified minimum number of entries.
     *
     * @param list    List to evaluate
     * @param minSize Minimum number of entries that must be present in the list
     * @param message the message to include in the exception
     */
    public static void isAtLeasSize(List<?> list, int minSize, String message) {
        if (minSize > list.size()) {
            throw new AssertionFailedException(message);
        }
    }

    /**
     * Asserts that the object has not been initialized.
     *
     * @param object  Object that must be null in order for the assertion to pass
     * @param message the message to include in the exception
     */
    public static void unitialized(Object object, String message) {
        if (null != object) {
            throw new AssertionFailedException(message);
        }
    }

    /**
     * Asserts that the given flag is true.
     *
     * @param flag    Flag that must be true for the assertion to pass
     * @param message the message to include in the exception
     */
    public static void assertTrue(boolean flag, String message) {
        if (!flag) {
            throw new AssertionFailedException(message);
        }
    }

    /**
     * Assert the two values are identical
     *
     * @param expectedLength Expected length
     * @param actualLength   Actual length
     * @param message        the message to include in the excpetion
     */
    public static void equals(int expectedLength, int actualLength, String message) {
        if (expectedLength != actualLength) {
            throw new AssertionFailedException(message);
        }
    }

    /**
     * Asserts the two objects are identical via the source object's <code>equals()</code> method.
     *
     * @param source  Source object
     * @param match   Match object
     * @param message the message to include in the excpetion
     */
    public static void equals(Object source, Object match, String message) {
        if (!source.equals(match)) {
            throw new AssertionFailedException(message);
        }
    }
}
