package mil.army.usace.ehlschlaeger.rgik.util;

import java.lang.reflect.Array;

/**
 * Collected methods which allow easy implementation of <code>hashCode</code>.
 * Taken from <a
 * href="http://www.javapractices.com/topic/TopicAction.do?Id=28">http
 * ://www.javapractices.com/topic/TopicAction.do?Id=28</a>.
 * <p>
 * Example use case:
 * 
 * <pre>
 * public int hashCode() {
 *     int result = HashCodeUtil.SEED;
 *     //collect the contributions of various fields
 *     result = HashCodeUtil.hash(result, fPrimitive);
 *     result = HashCodeUtil.hash(result, fObject);
 *     result = HashCodeUtil.hash(result, fArray);
 *     return result;
 * }
 * </pre>
 */
public class HashCodeUtil {

    /**
     * An initial value for a <code>hashCode</code>, to which is added
     * contributions from fields. Using a non-zero value decreases collisions of
     * <code>hashCode</code> values.
     */
    public static final int SEED = 23;

    /**
     * More readable way to start a hash code.
     */
    public static int start() {
        return SEED;
    }

    /**
     * booleans.
     */
    public static int hash(int aSeed, boolean aBoolean) {
        return firstTerm(aSeed) + (aBoolean ? 1 : 0);
    }

    /**
     * chars.
     */
    public static int hash(int aSeed, char aChar) {
        return firstTerm(aSeed) + (int) aChar;
    }

    /**
     * ints.
     */
    public static int hash(int aSeed, int aInt) {
        return firstTerm(aSeed) + aInt;
    }

    /**
     * longs.
     */
    public static int hash(int aSeed, long aLong) {
        return firstTerm(aSeed) + (int) (aLong ^ (aLong >>> 32));
    }

    /**
     * floats.
     */
    public static int hash(int aSeed, float aFloat) {
        return hash(aSeed, Float.floatToIntBits(aFloat));
    }

    /**
     * doubles.
     */
    public static int hash(int aSeed, double aDouble) {
        return hash(aSeed, Double.doubleToLongBits(aDouble));
    }

    /**
     * <code>aObject</code> is a possibly-null object field, and possibly an
     * array.
     * 
     * If <code>aObject</code> is an array, then each element may be a primitive
     * or a possibly-null object.
     */
    public static int hash(int aSeed, Object aObject) {
        int result = aSeed;
        if (aObject == null) {
            result = hash(result, 0);
        } else if (!isArray(aObject)) {
            result = hash(result, aObject.hashCode());
        } else {
            int length = Array.getLength(aObject);
            for (int idx = 0; idx < length; ++idx) {
                Object item = Array.get(aObject, idx);
                result = hash(result, item);
            }
        }
        return result;
    }

    private static final int ODD_PRIME_NUMBER = 37;

    private static int firstTerm(int aSeed) {
        return ODD_PRIME_NUMBER * aSeed;
    }

    private static boolean isArray(Object aObject) {
        return aObject.getClass().isArray();
    }
}
