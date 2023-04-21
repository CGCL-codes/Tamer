package triptracker.client.map.core;

import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public class Matrix2D<E> implements Iterable<E> {

    private E[][] mat;

    public Matrix2D() {
    }

    public Matrix2D(E[][] matrix) {
        mat = matrix;
    }

    public void setMatrix(E[][] matrix) {
        mat = matrix;
    }

    public E[][] getMatrix() {
        return mat;
    }

    public void set(int i, int j, E value) {
        mat[i][j] = value;
    }

    public E get(int i, int j) {
        return mat[i][j];
    }

    public int getLength(int dimension) {
        if (dimension < 0 || dimension > 1) {
            throw new IllegalArgumentException();
        }
        switch(dimension) {
            case 0:
                return mat.length;
            case 1:
                return mat[0].length;
            default:
                return 0;
        }
    }

    public Iterator<E> iterator() {
        return new MatrixIterator();
    }

    private class MatrixIterator implements ListIterator<E> {

        private final int length = mat.length * mat[0].length;

        private int cursor = 0;

        private int cursorI = 0;

        private int cursorJ = 0;

        /**
		 * {@inheritDoc}
		 */
        public boolean hasNext() {
            if (cursor < length) {
                return true;
            } else {
                return false;
            }
        }

        /**
		 * {@inheritDoc}
		 */
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            E o = mat[cursorI][cursorJ];
            moveCursor(true);
            return o;
        }

        /**
		 * {@inheritDoc}
		 */
        public boolean hasPrevious() {
            if (cursor > 0) {
                return true;
            } else {
                return false;
            }
        }

        /**
		 * {@inheritDoc}
		 */
        public E previous() {
            if (!hasPrevious()) {
                throw new NoSuchElementException();
            }
            moveCursor(false);
            return mat[cursorI][cursorJ];
        }

        /**
		 * {@inheritDoc}
		 */
        public int nextIndex() {
            return cursor;
        }

        /**
		 * {@inheritDoc}
		 */
        public int previousIndex() {
            return (cursor - 1);
        }

        /**
		 * {@inheritDoc}
		 */
        public void remove() {
            throw new UnsupportedOperationException();
        }

        /**
		 * {@inheritDoc}
		 */
        public void set(E o) {
            System.out.println("Setting new Matrix value");
            mat[cursorI][cursorJ] = o;
        }

        /**
		 * {@inheritDoc}
		 */
        public void add(E o) {
            throw new UnsupportedOperationException();
        }

        /**
		 * Move the cursor forward or backwards in the list and recalculate
		 * cursorI and cursorJ.
		 * 
		 * @param forward if true move forward, if false move backwards
		 */
        private void moveCursor(boolean forward) {
            if (forward) {
                cursor++;
            } else {
                cursor--;
            }
            cursorI = (cursor % mat.length);
            cursorJ = (cursor / mat.length);
        }
    }
}
