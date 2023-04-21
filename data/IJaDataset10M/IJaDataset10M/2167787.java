package net.sourceforge.align.filter.aligner.align.hmm.util.matrix;

/**
 * Reprezntuje fabryke pełnych macierzy.
 * @author loomchild
 */
public class FullMatrixFactory implements MatrixFactory {

    public <T> Matrix<T> createMatrix(int width, int height) {
        return new FullMatrix<T>(width, height);
    }
}
