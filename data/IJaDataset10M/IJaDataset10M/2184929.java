package la4j.inversion;

import la4j.err.MatrixInversionException;
import la4j.factory.Factory;
import la4j.matrix.Matrix;

public interface MatrixInvertor {

    public Matrix inverse(Matrix matrix, Factory factory) throws MatrixInversionException;
}
