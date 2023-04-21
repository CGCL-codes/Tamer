package net.lunglet.array4j.matrix.packed;

import net.lunglet.array4j.Storage;
import net.lunglet.array4j.matrix.AbstractMatrix;
import net.lunglet.array4j.matrix.dense.DenseVector;

public abstract class AbstractPackedMatrix<V extends DenseVector> extends AbstractMatrix<V> implements PackedMatrix {

    protected enum PackedType {

        LOWER_TRIANGULAR {

            @Override
            public PackedType transpose() {
                return UPPER_TRIANGULAR;
            }
        }
        , SYMMETRIC {

            @Override
            public PackedType transpose() {
                return SYMMETRIC;
            }
        }
        , UPPER_TRIANGULAR {

            @Override
            public PackedType transpose() {
                return LOWER_TRIANGULAR;
            }
        }
        ;

        public abstract PackedType transpose();
    }

    private static final long serialVersionUID = 1L;

    protected final PackedType packedType;

    protected final Storage storage;

    public AbstractPackedMatrix(final int rows, final int columns, final PackedType packedType, final Storage storage) {
        super(rows, columns);
        if (rows != columns) {
            throw new IllegalArgumentException();
        }
        this.packedType = packedType;
        this.storage = storage;
    }

    protected final void checkCanSet(final int row, final int column) {
        if (!nonZeroElement(row, column)) {
            throw new IllegalArgumentException();
        }
    }

    protected final int elementOffset(final int m, final int n) {
        checkRowIndex(m);
        checkColumnIndex(n);
        final int i, j;
        if (m <= n) {
            i = m;
            j = n;
        } else {
            j = m;
            i = n;
        }
        if (packedType.equals(PackedType.LOWER_TRIANGULAR)) {
            return i + (2 * columns - (j + 1)) * j / 2;
        } else {
            return i + (j + 1) * j / 2;
        }
    }

    protected final int getPackedLength() {
        return rows * (rows + 1) / 2;
    }

    @Override
    public final boolean isLowerTriangular() {
        return packedType.equals(PackedType.LOWER_TRIANGULAR);
    }

    @Override
    public final boolean isSymmetric() {
        return packedType.equals(PackedType.SYMMETRIC);
    }

    @Override
    public final boolean isUpperTriangular() {
        return packedType.equals(PackedType.UPPER_TRIANGULAR);
    }

    protected final boolean nonZeroElement(final int row, final int column) {
        if (packedType.equals(PackedType.UPPER_TRIANGULAR)) {
            return row <= column;
        } else if (packedType.equals(PackedType.LOWER_TRIANGULAR)) {
            return row >= column;
        } else {
            return true;
        }
    }
}
