package org.mili.core.lang;

import org.apache.commons.lang.*;
import org.apache.commons.lang.math.*;

/**
 * This class extends functionality of class org.apache.commons.Validate.
 *
 * @author Michael Lieshoff
 */
public class ASFValidate extends Validate {

    /**
     * Constructor. This class should not normally be instantiated.
     */
    public ASFValidate() {
        super();
    }

    /**
     * <p>
     * Checks if a number is in range and throws <code>IllegalArgumentException</code>, if
     * number is out of range.
     * </p>
     *
     * <p>The method is used to check numbers in range or not. As example if a port parameter is
     * checked.</p>
     *
     * <pre>
     * ASFValidate.isInRange(13, new IntegerRange(1, 10), "Not in range!");
     * </pre>
     *
     * @param n number.
     * @param r range.
     * @param message message if number is not in range.
     * @throws IllegalArgumentException if number is not in range.
     */
    public static void isInRange(Number n, Range r, String message) {
        Validate.notNull(n, "number can't be null!");
        Validate.notNull(r, "range can't be null!");
        if (message == null) {
            message = "The validated number[" + n + "] is not in range[" + r.getMinimumNumber() + ".." + r.getMaximumNumber() + "]";
        }
        Validate.isTrue(r.containsNumber(n), message);
    }

    /**
     * <p>
     * Checks if a number is in range and throws <code>IllegalArgumentException</code>, if
     * number is out of range.
     * </p>
     *
     * <p>The method is used to check numbers in range or not. As example if a port parameter is
     * checked.</p>
     *
     * <pre>
     * ASFValidate.isInRange(13, new IntegerRange(1, 10));
     * </pre>
     *
     * @param n number.
     * @param r range.
     * @throws IllegalArgumentException if number is not in range.
     */
    public static void isInRange(Number n, Range r) {
        isInRange(n, r, null);
    }
}
