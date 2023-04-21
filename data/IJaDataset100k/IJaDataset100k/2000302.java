package org.middleheaven.quantity.math.impl;

import java.util.List;
import org.middleheaven.quantity.math.Complex;
import org.middleheaven.quantity.math.BigInt;
import org.middleheaven.quantity.math.Matrix;
import org.middleheaven.quantity.math.Numeral;
import org.middleheaven.quantity.math.Real;
import org.middleheaven.quantity.math.Vector;
import org.middleheaven.quantity.math.structure.Field;
import org.middleheaven.quantity.math.structure.MathStructuresFactory;

public class StandardMathStructuresFactory extends MathStructuresFactory {

    @Override
    public <T extends Numeral<?>> T numberFor(Class<T> superclass, Object... values) {
        if (values.length == 1) {
            if (superclass.equals(Complex.class)) {
                return superclass.cast(new RealPairComplex(values[0].toString()));
            } else if (superclass.equals(Real.class)) {
                return superclass.cast(new BigDecimalReal(values[0].toString()));
            } else if (superclass.equals(BigInt.class)) {
                return superclass.cast(new LongInteger(values[0].toString()));
            } else {
                throw new IllegalArgumentException(superclass.getName() + " is not a recognized Number");
            }
        } else if (values.length == 2) {
            if (superclass.equals(Complex.class)) {
                return superclass.cast(new RealPairComplex((Real) values[0], (Real) values[1]));
            }
        }
        throw new IllegalArgumentException("Array bigger than 1 is not supported for type " + superclass.getName());
    }

    @Override
    public <T extends Field<T>> Vector<T> vectorize(List<T> elements) {
        return new DenseVector<T>(elements);
    }

    @Override
    public <T extends Field<T>> Vector<T> vectorize(Vector<T> other) {
        return new DenseVector<T>(other);
    }

    @Override
    public <T extends Field<T>> Vector<T> vectorize(int dimension, T value) {
        return new DenseVector<T>(dimension, value);
    }

    @Override
    public <T extends Field<T>> Matrix<T> matrixFrom(List<Vector<T>> rows) {
        return new DenseMatrix<T>(rows);
    }

    @Override
    public <T extends Field<T>> Matrix<T> diagonal(int size, T value) {
        return new SingleValueVectorDiagonalMatrix<T>(size, value);
    }
}
