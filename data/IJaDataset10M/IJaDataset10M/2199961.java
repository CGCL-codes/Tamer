package org.dishevelled.functor;

/**
 * Typed functor that takes three arguments and returns a <code>boolean</code>
 * value.
 *
 * @param <E> first argument type
 * @param <F> second argument type
 * @param <G> third argument type
 * @author  Michael Heuer
 * @version $Revision: 1059 $ $Date: 2012-01-03 15:03:02 -0500 (Tue, 03 Jan 2012) $
 */
public interface TernaryPredicate<E, F, G> {

    /**
     * Evaluate this predicate for the specified arguments.
     *
     * @param e first argument to this evaluation
     * @param f second argument to this evaluation
     * @param g third argument to this evaluation
     * @return the result of this evaluation
     */
    boolean test(E e, F f, G g);
}
