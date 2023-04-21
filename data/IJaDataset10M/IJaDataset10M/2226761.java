package gumbo.core.util;

/**
 * An object whose state is quantitative in nature and, as such, it can be
 * tested for numerical equality. Intended to work like Object.equals() (i.e.
 * reflexive, symmetrical, transitive, consistent), except numerical state is
 * tested (never identity), and a numerical tolerance is allowed.
 * <p>
 * As with Object.equals(), a concrete implementation of this method should be
 * final and test for the object type, to assure that the contract for
 * quantitative equality cannot be violated (i.e. a subclass does not violate
 * symmetry and transitivity).
 * <p>
 * Similar in purpose to Qualitative, but with a strictly numerical connotation
 * and the need to accommodate floating point roundoff error.
 * <p>
 * Derived from gumbo.util.core.Quantitative.
 * @author jonb
 * @see Qualitative
 */
public interface Quantitative {

    /**
	 * Returns true if the target has the same value as this object, within plus
	 * or minus a tolerance, inclusive. Intended to work like Object.equals()
	 * (i.e. reflexive, symmetrical, transitive, consistent).
	 * @param obj Temp input target object. Possibly null.
	 * @param tolerance Magnitude of the tolerance (sign is ignored). If zero,
	 * the values must be exactly equal. Specific definition is left to the
	 * concrete implementation.
	 * @return The result.
	 */
    public boolean equalsValue(Object obj, double tolerance);
}
