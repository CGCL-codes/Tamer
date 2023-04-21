package simtools.util;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Compare objects of type Number and String. Can handle any combination of both.
 * Will throw an error if the Object type does not match.
 * A String not containing a number is considered NaN and greater than any number
 * String comparison (lexicographic order) is OK if both strings do not contain numbers.
 */
public class NumberStringComparator implements Comparator, Serializable {

    static final long serialVersionUID = 5035953694017510082L;

    /** Internal method that tries to parse a String to a Number
	 *  First tries to parse to a Long, so it handles hex values and has a better 
	 *  precision for integer values. Then try with doubles.
	 * @param string
	 * @return Number : A converted number, or null
	 */
    public static Number stringToNumber(String string) {
        Number n = null;
        try {
            n = Long.decode(string);
        } catch (NumberFormatException nfe) {
            try {
                n = Double.valueOf(string);
            } catch (NumberFormatException nfe2) {
                return null;
            }
        }
        return n;
    }

    /**
	 * Compare objects of type Number and String. Can handle any combination of both.
	 * Will throw an error if the Object type does not match.
	 * A String not containing a number is considered NaN and greater than any number
	 * String comparison (lexicographic order) is OK if both strings do not contain numbers.
	 * @param o1 first number or sring 
	 * @param o2 second number or sring 
	 * @return int -1, 0 or 1 if o1 &lt; o2, o1 = o2, o1 &gt; o2
	 * @throws ClassCastException
	 */
    public static int numStringCompare(Object o1, Object o2) throws ClassCastException {
        if ((o1 == null) && (o2 != null)) return -1;
        if ((o1 != null) && (o2 == null)) return 1;
        if ((o1 == null) && (o2 == null)) return 0;
        if (o1 instanceof String) {
            if (o2 instanceof String) {
                Number n1 = stringToNumber((String) o1);
                Number n2 = stringToNumber((String) o2);
                if ((n1 == null) && (n2 == null)) return ((String) o1).compareTo((String) o2);
                return numStringCompare(n1, n2);
            }
            return -numStringCompare(o2, o1);
        }
        if ((o1 instanceof Long) && (o2 instanceof Long)) return ((Long) o1).compareTo((Long) o2);
        if (o1 instanceof Number) {
            if (o2 instanceof String) o2 = stringToNumber((String) o2);
            if (o2 == null) return -1;
            if (o2 instanceof Number) return Double.compare(((Number) o1).doubleValue(), ((Number) o2).doubleValue());
        }
        throw new ClassCastException();
    }

    /** Delegates to numStringCompare
	 * @see numStringCompare(java.lang.Object, java.lang.Object)
	 */
    public int compare(Object o1, Object o2) {
        return numStringCompare(o1, o2);
    }
}
