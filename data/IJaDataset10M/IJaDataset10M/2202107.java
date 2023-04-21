package de.grogra.xl.lang;

/**
 * Instances of <code>DoubleToLong</code> represent functions
 * which take <code>double</code>s as input and return values of
 * type <code>long</code>. Such instances are created by
 * lambda expressions of the XL programming language, e.g.,
 * <code>DoubleToLong f = double x => long</code>
 * <i>some expression containing x</i><code>;</code>.
 *
 * @author Ole Kniemeyer
 */
public interface DoubleToLong {

    /**
	 * Computes the value of this function at <code>x</code>.
	 * 
	 * @param x where the function is to be evaluated
	 * @return function value at <code>x</code>
	 */
    long evaluateLong(double x);
}
