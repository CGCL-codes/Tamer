package de.grogra.xl.lang;

/**
 * Instances of <code>CharToCharGenerator</code> represent
 * generator functions which take <code>char</code>s as input
 * and yield sequences of values of
 * type <code>char</code>. Such instances are created by
 * lambda expressions of the XL programming language, e.g.,
 * <code>CharToCharGenerator f = char x => char*</code>
 * <i>some generator expression containing x</i><code>;</code>.
 *
 * @author Ole Kniemeyer
 */
public interface CharToCharGenerator {

    /**
	 * Generates the sequence of values for parameter <code>x</code>.
	 * 
	 * @param cons each value is yielded to this consumer
	 * @param x where the generator function is to be evaluated
	 */
    void evaluateChar(CharConsumer cons, char x);
}
