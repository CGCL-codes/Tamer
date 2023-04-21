package org.dishevelled.matrix.io.impl.nonblocking;

import org.dishevelled.matrix.Matrix1D;
import org.dishevelled.matrix.impl.nonblocking.NonBlockingSparseMatrix1D;
import org.dishevelled.matrix.io.impl.AbstractTextMatrix1DReader;

/**
 * Abstract tab-delimited text reader for non-blocking sparse matrices of objects in one dimension.
 *
 * @param <E> 1D matrix element type
 * @author  Michael Heuer
 * @version $Revision$ $Date$
 */
public abstract class AbstractNonBlockingSparseTextMatrix1DReader<E> extends AbstractTextMatrix1DReader<E> {

    /** {@inheritDoc} */
    protected final Matrix1D<E> createMatrix1D(final long size, final int cardinality) {
        return new NonBlockingSparseMatrix1D<E>(size, cardinality);
    }
}
