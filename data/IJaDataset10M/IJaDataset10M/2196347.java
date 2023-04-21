package de.grogra.xl.lang;

/**
 * Instances of <code>FloatToShort</code> represent functions
 * which take <code>float</code>s as input and return values of
 * type <code>short</code>. Such instances are created by
 * lambda expressions of the XL programming language, e.g.,
 * <code>FloatToShort f = float x => short</code>
 * <i>some expression containing x</i><code>;</code>.
 *
 * @author Ole Kniemeyer
 */
public interface FloatToShort {

    /**
	 * Computes the value of this function at <code>x</code>.
	 * 
	 * @param x where the function is to be evaluated
	 * @return function value at <code>x</code>
	 */
    short evaluateShort(float x);
}
