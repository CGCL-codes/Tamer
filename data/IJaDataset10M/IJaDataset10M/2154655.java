package de.grogra.xl.lang;

/**
 * Instances of <code>LongToByteGenerator</code> represent
 * generator functions which take <code>long</code>s as input
 * and yield sequences of values of
 * type <code>byte</code>. Such instances are created by
 * lambda expressions of the XL programming language, e.g.,
 * <code>LongToByteGenerator f = long x => byte*</code>
 * <i>some generator expression containing x</i><code>;</code>.
 *
 * @author Ole Kniemeyer
 */
public interface LongToByteGenerator {

    /**
	 * Generates the sequence of values for parameter <code>x</code>.
	 * 
	 * @param cons each value is yielded to this consumer
	 * @param x where the generator function is to be evaluated
	 */
    void evaluateByte(ByteConsumer cons, long x);
}
