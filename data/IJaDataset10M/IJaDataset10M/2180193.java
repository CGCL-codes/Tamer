package de.grogra.xl.lang;

/**
 * Instances of <code>FloatToByte</code> represent functions
 * which take <code>float</code>s as input and return values of
 * type <code>byte</code>. Such instances are created by
 * lambda expressions of the XL programming language, e.g.,
 * <code>FloatToByte f = float x => byte</code>
 * <i>some expression containing x</i><code>;</code>.
 *
 * @author Ole Kniemeyer
 */
public interface FloatToByte {

    /**
	 * Computes the value of this function at <code>x</code>.
	 * 
	 * @param x where the function is to be evaluated
	 * @return function value at <code>x</code>
	 */
    byte evaluateByte(float x);
}
