package de.grogra.xl.lang;

/**
 * Instances of <code>IntToObjectGenerator</code> represent
 * generator functions which take <code>int</code>s as input
 * and yield sequences of values of
 * type <code>V</code>. Such instances are created by
 * lambda expressions of the XL programming language, e.g.,
 * <code>IntToObjectGenerator f = int x => V*</code>
 * <i>some generator expression containing x</i><code>;</code>.
 *
 * @author Ole Kniemeyer
 */
public interface IntToObjectGenerator<V> {

    /**
	 * Generates the sequence of values for parameter <code>x</code>.
	 * 
	 * @param cons each value is yielded to this consumer
	 * @param x where the generator function is to be evaluated
	 */
    void evaluateObject(ObjectConsumer<? super V> cons, int x);
}
