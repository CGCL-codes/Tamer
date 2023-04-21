package org.agilercp.testsupport;

/**
 * @author Heiko Seeberger
 * @param <T>
 */
public class Argument<T> {

    private T t;

    /**
     * Returns the value.
     * 
     * @return The arguments value or <code>null</code>
     */
    public T getValue() {
        return t;
    }

    /**
     * @param t
     *            The value to set
     */
    public void setValue(final T t) {
        this.t = t;
    }
}
