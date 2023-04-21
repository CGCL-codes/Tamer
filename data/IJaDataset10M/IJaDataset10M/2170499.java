package net.lunglet.array4j.matrix.packed;

import net.lunglet.array4j.Storage;
import net.lunglet.array4j.matrix.dense.FloatDenseMatrix;
import net.lunglet.array4j.matrix.packed.AbstractPackedMatrix.PackedType;

/**
 * Factory for creating packed matrices.
 */
public final class PackedFactory {

    /**
     * Create lower triangular single precision matrix with default storage.
     */
    public static FloatPackedMatrix floatLowerTriangular(final int rows, final int columns) {
        return new FloatPackedMatrixImpl(rows, columns, PackedType.LOWER_TRIANGULAR, Storage.DEFAULT);
    }

    /**
     * Create symmetric single precision matrix with default storage.
     */
    public static FloatPackedMatrix floatSymmetric(final int dim) {
        return floatSymmetric(dim, Storage.DEFAULT);
    }

    /**
     * Create symmetric single precision matrix with specific storage.
     */
    public static FloatPackedMatrix floatSymmetric(final int dim, final Storage storage) {
        return new FloatPackedMatrixImpl(dim, dim, PackedType.SYMMETRIC, storage);
    }

    /**
     * Create symmetric single precision matrix with direct storage.
     */
    public static FloatPackedMatrix floatSymmetricDirect(final int dim) {
        return floatSymmetric(dim, Storage.DIRECT);
    }

    /**
     * Create symmetric single precision matrix with heap storage.
     */
    public static FloatPackedMatrix floatSymmetricHeap(final int dim) {
        return floatSymmetric(dim, Storage.HEAP);
    }

    /**
     * Create upper triangular single precision matrix with default storage.
     */
    public static FloatPackedMatrix floatUpperTriangular(final int rows, final int columns) {
        return new FloatPackedMatrixImpl(rows, columns, PackedType.UPPER_TRIANGULAR, Storage.DEFAULT);
    }

    /**
     * Create symmetric single precision matrix from dense matrix.
     */
    public static FloatPackedMatrix symmetric(final FloatDenseMatrix a) {
        if (!a.isSquare()) {
            throw new IllegalArgumentException();
        }
        FloatPackedMatrix b = floatSymmetric(a.rows(), a.storage());
        for (int i = 0; i < a.rows(); i++) {
            for (int j = i; j < a.columns(); j++) {
                b.set(i, j, a.get(i, j));
            }
        }
        return b;
    }

    private PackedFactory() {
    }
}
