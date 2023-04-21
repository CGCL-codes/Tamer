package info.nekonya.math.matrix;

import info.nekonya.math.matrix.impl.ContiguousMatrix;

/**
 * A factory for creating matrixes with various implementations.  This is the only way to create a
 * matrix: there are no public implementations.
 * 
 * Example usage:
 * 
 * <code>
 * Matrix m = MatrixFactory.makeContiguousMatrix(10, 20);
 * </code>
 */
public class MatrixFactory {

    public static Matrix makeContiguousMatrix(int rowCount, int columnCount) {
        Matrix m = new ContiguousMatrix(rowCount, columnCount);
        return m;
    }
}
