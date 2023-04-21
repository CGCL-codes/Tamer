package org.op4j.operators.qualities;

/**
 * <p>
 * This interface contains methods for ending array iterations.
 * </p>
 * 
 * @since 1.0
 * 
 * @author Daniel Fern&aacute;ndez
 *
 */
public interface NavigatingArrayOperator<T> {

    /**
     * <p>
     * Ends the current iteration. After the execution of this method, all subsequent operations
     * will be applied on the iterated array as a whole instead of in a by-element basis.
     * </p>
     * 
     * @return an operator which will execute all operations on the iterated array as a whole.
     */
    public NavigableArrayOperator<T> endFor();
}
