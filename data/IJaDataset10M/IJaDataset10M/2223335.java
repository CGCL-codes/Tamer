package de.grogra.xl.lang;

/**
 * Instances of <code>IntToChar</code> represent functions
 * which take <code>int</code>s as input and return values of
 * type <code>char</code>. Such instances are created by
 * lambda expressions of the XL programming language, e.g.,
 * <code>IntToChar f = int x => char</code>
 * <i>some expression containing x</i><code>;</code>.
 *
 * @author Ole Kniemeyer
 */
public interface IntToChar {

    /**
	 * Computes the value of this function at <code>x</code>.
	 * 
	 * @param x where the function is to be evaluated
	 * @return function value at <code>x</code>
	 */
    char evaluateChar(int x);
}
