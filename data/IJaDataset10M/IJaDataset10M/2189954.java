package math.linearalgebra;

import math.abstractalgebra.ClosedBinaryOperation;
import math.abstractalgebra.Ring;

public final class MatrixMulOperation<T> implements ClosedBinaryOperation<Matrix<T>> {

    private static final long serialVersionUID = 1L;

    private final Ring<T> ring;

    public MatrixMulOperation(Ring<T> ring) {
        super();
        this.ring = ring;
    }

    public Matrix<T> perform(Matrix<T> x, Matrix<T> y) {
        return new MultiplyMatrix<T>(ring, x, y);
    }
}
