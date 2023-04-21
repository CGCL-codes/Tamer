package model.misc;

/**
 *
 * @author Vince Kane
 */
public class Array2D<T> {

    protected Object[][] elements;

    protected int cols;

    protected int rows;

    public Array2D(int cols, int rows) {
        elements = new Object[cols][rows];
        this.cols = cols;
        this.rows = rows;
    }

    public void put(T element, int col, int row) {
        elements[col][row] = element;
    }

    public T get(int col, int row) {
        return (T) elements[col][row];
    }

    public int getWidth() {
        return cols;
    }

    public int getHeight() {
        return rows;
    }
}
