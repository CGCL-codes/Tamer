package net.sourceforge.align.filter.aligner.align.hmm.util.matrix;

/**
 * Reprezentuje kompletną macierz dwuwymiarową.
 * @author loomchild
 */
public class FullMatrix<T> implements Matrix<T> {

    private Object[][] dataArray;

    private int width;

    private int height;

    /**
	 * Tworzy macierz.
	 * @param width Szerokość (wymiar x), >= 1.
	 * @param height Wysokość (wymiar y), >= 1.
	 */
    public FullMatrix(int width, int height) {
        this.width = width;
        this.height = height;
        this.dataArray = new Object[width][height];
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public T get(int x, int y) {
        return (T) dataArray[x][y];
    }

    public void set(int x, int y, T data) {
        dataArray[x][y] = data;
    }

    public MatrixIterator<T> getIterator() {
        return new FullMatrixIterator<T>(this);
    }
}
