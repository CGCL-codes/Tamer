package de.grogra.xl.lang;

/**
 * Instances of <code>VoidToShortGenerator</code> represent
 * generator functions which take <code>void</code>s as input
 * and yield sequences of values of
 * type <code>short</code>. Such instances are created by
 * lambda expressions of the XL programming language, e.g.,
 * <code>VoidToShortGenerator f = void => short*</code>
 * <i>some generator expression</i><code>;</code>.
 *
 * @author Ole Kniemeyer
 */
public interface VoidToShortGenerator {

    /**
	 * Generates the sequence of values.
	 * 
	 * @param cons each value is yielded to this consumer
	 */
    void evaluateShort(ShortConsumer cons);
}
