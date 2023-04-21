package edu.ucla.sspace.vector;

import edu.ucla.sspace.util.DoubleEntry;
import java.io.Serializable;
import java.util.Arrays;

/**
 * A view of a sparse {@link DoubleVector} that that allows the backing data to
 * be resized and also viewed from an offset.  Furthermore, this class allows
 * the viewed data to be immutable, where all mutating operations throw {@link
 * UnsupportedOperationException}.
 * 
 * @author Keith Stevens
 * @authod David Jurgens
 */
class ViewDoubleAsDoubleSparseVector extends DoubleVectorView implements SparseDoubleVector {

    private static final long serialVersionUID = 1L;

    /**
     * A {@link SparseVector} reference to the {@link DoubleVector} data backing
     * this view.
     */
    private final SparseVector sparseVector;

    /**
     * Creates a new {@link DoubleVector} view of the data in the provided
     * {@link DoubleVector}.
     *
     * @param v the {@code DoubleVector} to view as containing double data.
     */
    public <T extends DoubleVector & SparseVector<Double>> ViewDoubleAsDoubleSparseVector(T v) {
        this(v, 0, v.length(), false);
    }

    /**
     * Creates a new, optionally immutable {@link DoubleVector} view of the data
     * in the provided {@link DoubleVector}.
     *
     * @param v the {@code DoubleVector} to view as containing double data.
     * @param isImmutable {@code true} if this view should not allow mutating
     *        operations to change the state of the backing vector
     */
    public <T extends DoubleVector & SparseVector<Double>> ViewDoubleAsDoubleSparseVector(T v, boolean isImmutable) {
        this(v, 0, v.length(), isImmutable);
    }

    /**
     * Creates a new {@link DoubleVector} sub-view of the data in the provided
     * {@link DoubleVector} using the offset and length to specify a viewing
     * region.
     *
     * @param v the {@code Vector} whose data is reflected in this view.
     * @param offset the index of {@code v} at which the first index of this
     *               view starts
     * @param length the length of this view.
     */
    public <T extends DoubleVector & SparseVector<Double>> ViewDoubleAsDoubleSparseVector(T v, int offset, int length) {
        this(v, offset, length, false);
    }

    /**
     * Creates a new, optionally immutable {@link DoubleVector} sub-view of the
     * data in the provided {@link DoubleVector} using the offset and length to
     * specify a viewing region.
     *
     * @param v the {@code Vector} whose data is reflected in this view.
     * @param offset the index of {@code v} at which the first index of this
     *               view starts
     * @param length the length of this view.
     * @param isImmutable {@code true} if this view should not allow mutating
     *        operations to change the state of the backing vector.
     */
    public <T extends DoubleVector & SparseVector<Double>> ViewDoubleAsDoubleSparseVector(T v, int offset, int length, boolean isImmutable) {
        super(v, offset, length, isImmutable);
        sparseVector = v;
    }

    /**
     * {@inheritDoc}
     */
    public int[] getNonZeroIndices() {
        if (vectorOffset == 0 && vectorLength == sparseVector.length()) return sparseVector.getNonZeroIndices(); else {
            int inRange = 0;
            int[] indices = sparseVector.getNonZeroIndices();
            for (int nz : indices) {
                if (nz >= vectorOffset && nz < vectorOffset + vectorLength) inRange++;
            }
            int[] arr = new int[inRange];
            int idx = 0;
            for (int nz : indices) {
                if (nz >= vectorOffset && nz < vectorOffset + vectorLength) arr[idx++] = nz;
            }
            return arr;
        }
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public double magnitude() {
        if (magnitude < 0) {
            double m = 0;
            if (sparseVector instanceof Iterable) {
                for (DoubleEntry e : (Iterable<DoubleEntry>) sparseVector) {
                    int idx = e.index();
                    if (idx >= vectorOffset && idx < vectorOffset + vectorLength) {
                        double d = e.value();
                        m += d * d;
                    }
                }
            } else {
                for (int nz : sparseVector.getNonZeroIndices()) {
                    if (nz >= vectorOffset && nz < vectorOffset + vectorLength) {
                        double d = doubleVector.get(nz);
                        m += d * d;
                    }
                }
            }
            magnitude = Math.sqrt(m);
        }
        return magnitude;
    }
}
