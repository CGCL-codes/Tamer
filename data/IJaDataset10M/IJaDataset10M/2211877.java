package de.denkselbst.niffler.util;

/**
 * A simple structure to hold two elements; saves the trouble of
 * creating arrays or structure classes for simple cases.
 * 
 * @param <E> type of the first element. 
 * @param <F> type of the second element.
 * 
 * @author patrick
 */
public class Tuple2<E, F> {

    /**
     * The first component.
     */
    public final E component1;

    /**
     * The second component.
     */
    public final F component2;

    /**
     * Constructs a tuple.
     * @param comp1 the first component.
     * @param comp2 the second component.
     */
    public Tuple2(E comp1, F comp2) {
        this.component1 = comp1;
        this.component2 = comp2;
    }
}
