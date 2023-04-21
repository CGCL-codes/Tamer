package org.dishevelled.evolve.mutate;

/**
 * Null individual-wise mutation function.
 *
 * @param <I> individual type
 * @author  Michael Heuer
 * @version $Revision: 1059 $ $Date: 2012-01-03 15:03:02 -0500 (Tue, 03 Jan 2012) $
 */
public final class NullIndividualWiseMutation<I> implements IndividualWiseMutation<I> {

    /** {@inheritDoc} */
    public I mutate(final I individual) {
        return individual;
    }
}
