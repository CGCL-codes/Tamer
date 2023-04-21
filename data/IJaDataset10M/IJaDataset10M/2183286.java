package org.fest.swing.util;

/**
 * Understands a range of values.
 * @param <T> the generic type of the values in this range.
 *
 * @author Yvonne Wang
 */
public final class GenericRange<T> {

    /** Initial value of this range. */
    public final T from;

    /** Final value of this range. */
    public final T to;

    /**
   * Creates a new </code>{@link GenericRange}</code>.
   * @param from the initial value of this range.
   * @param to the final value of this range.
   */
    public GenericRange(T from, T to) {
        this.from = from;
        this.to = to;
    }
}
