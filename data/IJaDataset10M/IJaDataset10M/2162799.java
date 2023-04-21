package net.lunglet.array4j.matrix.dense;

import net.lunglet.array4j.ComplexFloat;

abstract class AbstractCFloatDense extends AbstractDenseMatrix<CFloatDenseVector, ComplexFloat[]> {

    private static final long serialVersionUID = 1L;

    public AbstractCFloatDense() {
        super(null, 0, 0, 0, 0, 0, 0, null);
    }
}
