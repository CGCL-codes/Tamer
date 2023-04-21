package org.middleheaven.quantity.math.structure;

import java.util.List;
import org.middleheaven.quantity.math.Matrix;
import org.middleheaven.quantity.math.Numeral;
import org.middleheaven.quantity.math.Vector;
import org.middleheaven.quantity.math.impl.StandardMathStructuresFactory;

public abstract class MathStructuresFactory {

    private static MathStructuresFactory currentFactory = new StandardMathStructuresFactory();

    public static MathStructuresFactory getFactory() {
        return currentFactory;
    }

    public static void setFactory(MathStructuresFactory factory) {
        currentFactory = factory;
    }

    protected MathStructuresFactory() {
    }

    ;

    public abstract <T extends Numeral<?>> T numberFor(Class<T> superclass, Object... value);

    public <T extends Numeral<T>> T promote(Numeral<?> other, Class<T> targetType) {
        Class<?> originType = other.getClass();
        if (originType.equals(targetType)) {
            return targetType.cast(other);
        } else {
            return numberFor(targetType, originType.toString());
        }
    }

    public abstract <T extends Field<T>> Matrix<T> matrixFrom(List<Vector<T>> rows);

    public abstract <T extends Field<T>> Matrix<T> diagonal(int size, T value);

    public abstract <T extends Field<T>> Vector<T> vectorize(List<T> elements);

    public abstract <T extends Field<T>> Vector<T> vectorize(Vector<T> other);

    public abstract <T extends Field<T>> Vector<T> vectorize(int dimension, T value);
}
