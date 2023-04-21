package org.jmathematics.calc.matrix;

import java.util.concurrent.Callable;

public interface Simplex<T> extends Callable<Void> {

    void setMatrix(Matrix<T> matrix);

    Matrix<T> getMatrix();

    void setFormula(Vector<T> formula);

    void addRestriction(Vector<T> restriction);

    Vector<T> getResult();

    Void call();
}
