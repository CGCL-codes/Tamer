package de.grogra.xl.lang;

/**
 * Instances of <code>ByteToBooleanGenerator</code> represent
 * generator functions which take <code>byte</code>s as input
 * and yield sequences of values of
 * type <code>boolean</code>. Such instances are created by
 * lambda expressions of the XL programming language, e.g.,
 * <code>ByteToBooleanGenerator f = byte x => boolean*</code>
 * <i>some generator expression containing x</i><code>;</code>.
 *
 * @author Ole Kniemeyer
 */
public interface ByteToBooleanGenerator {

    /**
	 * Generates the sequence of values for parameter <code>x</code>.
	 * 
	 * @param cons each value is yielded to this consumer
	 * @param x where the generator function is to be evaluated
	 */
    void evaluateBoolean(BooleanConsumer cons, byte x);
}
