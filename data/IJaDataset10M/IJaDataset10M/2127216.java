package net.sf.collections15;

/**
 * Interface for any object that decorates a <code>Predicate</code> instance.
 * <p/>
 * This interface enables tools to access the decorated <code>Predicate</code>.
 *
 * @author Chris Lambrou
 * @since Collections15 1.0
 */
public interface PredicateDecorator<E> {

    /**
     * Returns the <code>Predicate</code> decorated by this
     * <code>Predicate</code>.
     *
     * @return The decorated <code>Predicate</code>.
     */
    Predicate<? super E> getDecoratedPredicate();
}
