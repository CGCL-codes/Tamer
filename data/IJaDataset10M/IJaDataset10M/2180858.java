package org.middleheaven.quantity.math.structure;

import org.middleheaven.quantity.math.Matrix;

public interface MatrixInvertionAlgorithm {

    public <F extends Field<F>> Matrix<F> invert(Matrix<F> matrix);
}
