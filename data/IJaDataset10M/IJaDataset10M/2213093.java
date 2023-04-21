package edu.ucla.sspace.svs;

import edu.ucla.sspace.vector.SparseDoubleVector;

/**
 * This interface combines two {@link SparseDoubleVector} to create a new {@link
 * SparseDoubleVector} that a combination or mixture of features from both
 * {@link SparseDoubleVector}s.
 *
 * @author Keith Stevens
 */
public interface VectorCombinor {

    /**
     * Combines features from {@code v1} and {@code v2} to produce a new vector.
     * {@link VectorCombinor} are allowed to modify {@code v1} if needed.
     */
    SparseDoubleVector combine(SparseDoubleVector v1, SparseDoubleVector v2);

    /**
     * Combines features from {@code v1} and {@code v2} to produce a new vector.
     * {@link VectorCombinor} are <b>not</b> allowed to modify {@code v1} if
     * needed.
     */
    SparseDoubleVector combineUnmodified(SparseDoubleVector v1, SparseDoubleVector v2);
}
