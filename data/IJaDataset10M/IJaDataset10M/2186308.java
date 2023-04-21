package net.sf.staccatocommons.defs.reduction;

import net.sf.staccatocommons.defs.Thunk;

/**
 * An imperative accumulator. See {@link Reduction} for more details.
 * 
 * @author flbulgarelli
 * @param <A>
 *          the type of the input accumulated value
 * @param <B>
 *          the type of the output accumulated value
 * @since 1.2
 */
public interface Accumulator<A, B> extends Thunk<B> {

    /**
   * Adds an element to this accumulator
   * 
   * @param element
   *          the element to add.
   */
    void accumulate(A element);

    /**
   * Answers the accumulated value
   */
    B value();
}
