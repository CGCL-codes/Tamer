package net.sf.javareflector.predicate;

import com.google.common.base.Predicate;
import net.sf.javareflector.XObject;

/**
 * Adds utility methods to allow for convenient construction of complex predicate.
 * <p/>
 * Created 4/15/11.
 */
public abstract class AbstractPredicate<T> extends XObject implements Predicate<T> {

    /**
     * Returns a predicate which is the logical OR of this predicate and the given predicate.
     *
     * @param predicate the predicate to chain with this predicate.
     * @return the resulting "or'd" predicate.
     */
    public Predicate<T> or(final Predicate<? super T> predicate) {
        return new CompoundPredicate<T>(this, predicate) {

            @Override
            protected boolean evaluate(boolean left, boolean right) {
                return left || right;
            }
        };
    }

    /**
     * Returns a predicate which is the logical AND of this predicate and the given predicate.
     *
     * @param predicate the predicate to chain with this predicate.
     * @return the resulting "and" predicate.
     */
    public Predicate<T> and(final Predicate<? super T> predicate) {
        return new CompoundPredicate<T>(this, predicate) {

            @Override
            protected boolean evaluate(boolean left, boolean right) {
                return left && right;
            }
        };
    }
}
