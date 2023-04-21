package org.dishevelled.functor;

/**
 * Typed functor that takes two arguments and returns no value.
 *
 * @param <E> first argument type
 * @param <F> second argument type
 * @author  Michael Heuer
 * @version $Revision: 1059 $ $Date: 2012-01-03 15:03:02 -0500 (Tue, 03 Jan 2012) $
 */
public interface BinaryProcedure<E, F> {

    /**
     * Execute this procedure with the specified arguments.
     *
     * @param e first argument to this execution
     * @param f second argument to this execution
     */
    void run(E e, F f);
}
