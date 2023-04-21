package uchicago.src.sim.network;

import java.util.List;

public class AdjacencyMatrixFactory {

    private AdjacencyMatrixFactory() {
    }

    /**
   * Returns an AdjacencyMatrix of the appropriate type.
   *
   * @param rows the number of rows in the matrix
   * @param cols the number of cols in the matrix
   * @param type the type of the matrix. type refers to the size of the matrix
   * elements (ij values) and can be one of NetworkConstants.BINARY,
   * NetworkConstants.LARGE, NetworkConstants.SMALL
   */
    public static AdjacencyMatrix createAdjacencyMatrix(int rows, int cols, int type) {
        switch(type) {
            case NetworkConstants.BINARY:
                return new AdjacencyBitMatrix(rows, cols);
            case NetworkConstants.SMALL:
                return new AdjacencyByteMatrix(rows, cols);
            case NetworkConstants.LARGE:
                return new AdjacencyDoubleMatrix(rows, cols);
            default:
                throw new IllegalArgumentException("Illegal matrix type, must be one " + " of NetworkConstants.BINARY, NetworkConstants.LARGE, or " + "NetworkConstants.SMALL");
        }
    }

    public static AdjacencyMatrix createAdjacencyMatrix(List labels, int type) {
        switch(type) {
            case NetworkConstants.BINARY:
                return new AdjacencyBitMatrix(labels);
            case NetworkConstants.SMALL:
                return new AdjacencyByteMatrix(labels);
            case NetworkConstants.LARGE:
                return new AdjacencyDoubleMatrix(labels);
            default:
                throw new IllegalArgumentException("Illegal matrix type, must be one " + " of NetworkConstants.BINARY, NetworkConstants.LARGE, or " + "NetworkConstants.SMALL");
        }
    }
}
