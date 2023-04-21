package sdsu.compare;

/**
 * A concrete class for comparing Numeric objects which contain values
 * that can be converted to a float.
 * @version 1.0 4 June 1997 
 * @author Roger Whitney (<a href=mailto:whitney@cs.sdsu.edu>whitney@cs.sdsu.edu</a>)
 */
public class FloatComparer extends Comparer {

    private static Comparer singleInstance;

    /**
	 * Force use of instance method to get object instance.
	 */
    private FloatComparer() {
    }

    /**
	 * Returns a FloatComparer object.
	 */
    public static Comparer getInstance() {
        if (singleInstance == null) singleInstance = new FloatComparer();
        return singleInstance;
    }

    /**
	 * Returns true if the leftOperand is less than the rightOperand.
	 * @exception ClassCastException If operand objects are not Number objects.
	 *  ClassCastException is a RuntimeException, so compiler does not force you
	 *  to catch this exception.
	 */
    public final boolean lessThan(Object leftOperand, Object rightOperand) throws ClassCastException {
        float leftFloatOperand = ((Number) leftOperand).floatValue();
        float rightFloatOperand = ((Number) rightOperand).floatValue();
        if (leftFloatOperand < rightFloatOperand) return true; else return false;
    }

    /**
	 * Returns true if the leftOperand is greater than the rightOperand.
	 * @exception ClassCastException If operand objects are not Number objects.
	 *  ClassCastException is a RuntimeException, so compiler does not force you
	 *  to catch this exception.
	 */
    public final boolean greaterThan(Object leftOperand, Object rightOperand) throws ClassCastException {
        float leftFloatOperand = ((Number) leftOperand).floatValue();
        float rightFloatOperand = ((Number) rightOperand).floatValue();
        if (leftFloatOperand > rightFloatOperand) return true; else return false;
    }

    /**
	 * Returns true if the leftOperand is equal to the rightOperand.
	 * @exception ClassCastException If operand objects are not Number objects.
	 *  ClassCastException is a RuntimeException, so compiler does not force you
	 *  to catch this exception.
	 */
    public final boolean equals(Object leftOperand, Object rightOperand) throws ClassCastException {
        float leftFloatOperand = ((Number) leftOperand).floatValue();
        float rightFloatOperand = ((Number) rightOperand).floatValue();
        if (leftFloatOperand == rightFloatOperand) return true; else return false;
    }
}
