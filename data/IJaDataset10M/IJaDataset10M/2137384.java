package net.sf.jdsc.asserts;

import static java.lang.Byte.valueOf;
import static net.sf.jdsc.asserts.internal.ExceptionUtil.throwException;

/**
 * @author <a href="mailto:twyss@users.sourceforge.net">twyss</a>
 * @version 1.0
 */
public class ByteBounds extends Bounds {

    public static boolean checkLowerBound(byte value, byte lowerBound) throws ValueOutOfBoundsException {
        return checkLowerBound(value, (String) null, lowerBound);
    }

    public static boolean checkLowerBound(byte value, byte lowerBound, boolean inclusive) throws ValueOutOfBoundsException {
        return checkLowerBound(value, (String) null, lowerBound, inclusive);
    }

    public static boolean checkLowerBound(byte value, String name, byte lowerBound) throws ValueOutOfBoundsException {
        return checkLowerBound(value, name, lowerBound, DEFAULT_INCLUSIVE);
    }

    public static boolean checkLowerBound(byte value, String name, byte lowerBound, boolean inclusive) throws ValueOutOfBoundsException {
        return checkLowerBound(value, name, lowerBound, inclusive, ValueOutOfBoundsException.class);
    }

    public static <X extends Throwable> boolean checkLowerBound(byte value, byte lowerBound, Class<X> exception) throws X {
        return checkLowerBound(value, (String) null, lowerBound, exception);
    }

    public static <X extends Throwable> boolean checkLowerBound(byte value, byte lowerBound, boolean inclusive, Class<X> exception) throws X {
        return checkLowerBound(value, (String) null, lowerBound, inclusive, exception);
    }

    public static <X extends Throwable> boolean checkLowerBound(byte value, String name, byte lowerBound, Class<X> exception) throws X {
        return checkLowerBound(value, name, lowerBound, DEFAULT_INCLUSIVE, exception);
    }

    public static <X extends Throwable> boolean checkLowerBound(byte value, String name, byte lowerBound, boolean inclusive, Class<X> exception) throws X {
        String message = null;
        if (value < lowerBound) message = buildLowerMessage(name, valueOf(value), valueOf(lowerBound)); else if (!inclusive && value == lowerBound) message = buildEqualMessage(name, valueOf(value), valueOf(lowerBound));
        if (message != null) throwException(exception, PARAMETER_TYPES, message, valueOf(value), valueOf(lowerBound));
        return true;
    }

    public static boolean checkUpperBound(byte value, byte upperBound) throws ValueOutOfBoundsException {
        return checkUpperBound(value, (String) null, upperBound);
    }

    public static boolean checkUpperBound(byte value, byte upperBound, boolean inclusive) throws ValueOutOfBoundsException {
        return checkUpperBound(value, (String) null, upperBound, inclusive);
    }

    public static boolean checkUpperBound(byte value, String name, byte upperBound) throws ValueOutOfBoundsException {
        return checkUpperBound(value, name, upperBound, DEFAULT_INCLUSIVE);
    }

    public static boolean checkUpperBound(byte value, String name, byte upperBound, boolean inclusive) throws ValueOutOfBoundsException {
        return checkUpperBound(value, name, upperBound, inclusive, ValueOutOfBoundsException.class);
    }

    public static <X extends Throwable> boolean checkUpperBound(byte value, byte upperBound, Class<X> exception) throws X {
        return checkUpperBound(value, (String) null, upperBound, exception);
    }

    public static <X extends Throwable> boolean checkUpperBound(byte value, byte upperBound, boolean inclusive, Class<X> exception) throws X {
        return checkUpperBound(value, (String) null, upperBound, inclusive, exception);
    }

    public static <X extends Throwable> boolean checkUpperBound(byte value, String name, byte upperBound, Class<X> exception) throws X {
        return checkUpperBound(value, name, upperBound, DEFAULT_INCLUSIVE, exception);
    }

    public static <X extends Throwable> boolean checkUpperBound(byte value, String name, byte upperBound, boolean inclusive, Class<X> exception) throws X {
        String message = null;
        if (value > upperBound) message = buildGreaterMessage(name, valueOf(value), valueOf(upperBound)); else if (!inclusive && value == upperBound) message = buildEqualMessage(name, valueOf(value), valueOf(upperBound));
        if (message != null) throwException(exception, PARAMETER_TYPES, message, valueOf(value), valueOf(upperBound));
        return true;
    }

    public static boolean checkBounds(byte value, byte lowerBound, byte upperBound) throws ValueOutOfBoundsException {
        return checkBounds(value, (String) null, lowerBound, upperBound);
    }

    public static boolean checkBounds(byte value, byte lowerBound, boolean lowerInclusive, byte upperBound, boolean upperInclusive) throws ValueOutOfBoundsException {
        return checkBounds(value, (String) null, lowerBound, lowerInclusive, upperBound, upperInclusive);
    }

    public static boolean checkBounds(byte value, String name, byte lowerBound, byte upperBound) throws ValueOutOfBoundsException {
        return checkBounds(value, name, lowerBound, DEFAULT_INCLUSIVE, upperBound, DEFAULT_INCLUSIVE);
    }

    public static boolean checkBounds(byte value, String name, byte lowerBound, boolean lowerInclusive, byte upperBound, boolean upperInclusive) throws ValueOutOfBoundsException {
        return checkBounds(value, name, lowerBound, lowerInclusive, upperBound, upperInclusive, ValueOutOfBoundsException.class);
    }

    public static <X extends Throwable> boolean checkBounds(byte value, byte lowerBound, byte upperBound, Class<X> exception) throws X {
        return checkBounds(value, (String) null, lowerBound, upperBound, exception);
    }

    public static <X extends Throwable> boolean checkBounds(byte value, byte lowerBound, boolean lowerInclusive, byte upperBound, boolean upperInclusive, Class<X> exception) throws X {
        return checkBounds(value, (String) null, lowerBound, lowerInclusive, upperBound, upperInclusive, exception);
    }

    public static <X extends Throwable> boolean checkBounds(byte value, String name, byte lowerBound, byte upperBound, Class<X> exception) throws X {
        return checkBounds(value, name, lowerBound, DEFAULT_INCLUSIVE, upperBound, DEFAULT_INCLUSIVE, exception);
    }

    public static <X extends Throwable> boolean checkBounds(byte value, String name, byte lowerBound, boolean lowerInclusive, byte upperBound, boolean upperInclusive, Class<X> exception) throws X {
        checkLowerBound(value, name, lowerBound, lowerInclusive, exception);
        checkUpperBound(value, name, upperBound, upperInclusive, exception);
        return true;
    }
}
