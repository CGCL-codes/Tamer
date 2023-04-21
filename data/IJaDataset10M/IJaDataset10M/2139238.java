package org.jquantlib.math.matrixutilities.internal;

import java.util.ListIterator;
import java.util.Set;
import org.jquantlib.math.matrixutilities.Array;
import org.jquantlib.math.matrixutilities.Matrix;

/**
 * This is the main interface responsible for {@link Matrix} and {@link Array} accessors
 *
 * @author Richard Gomes
 */
public interface Address {

    public static final String INVALID_BACKWARD_INDEXING = "invalid backward indexing";

    public static final String INVALID_ROW_INDEX = "invalid row index";

    public static final String INVALID_COLUMN_INDEX = "invalid column index";

    public static final String GAP_INDEX_FOUND = "gap index found";

    /**
     * @return the number of rows mapped
     */
    public int rows();

    /**
     * @return the number of columns mapped
     */
    public int cols();

    /**
     * @return the base address (inclusive)
     */
    public int base();

    /**
     * @return the last address (exclusive)
     */
    public int last();

    /**
     * <code>row0</code> is the row offset of the upmost row of a Matrix.
     * <p>
     * Example: Matrix B, being a sub-matrix of A, has <code>row0</code> like shown below:
     * <pre>
     *     Matrix A = new Matrix(5, 8);
     *     int row0 = 1; int row1 = 4;
     *     int col0 = 5; int col1 = 8;
     *     Matrix B = A.range(row0, row1, col0, col1);
     * </pre>
     *
     * @return
     */
    public int row0();

    /**
     * <code>col0</code> is the row offset of the leftmost column of a Matrix.
     * <p>
     * Example: Matrix B, being a sub-matrix of A, has <code>col0</code> like shown below:
     * <pre>
     *     Matrix A = new Matrix(5, 8);
     *     int row0 = 1; int row1 = 4;
     *     int col0 = 5; int col1 = 8;
     *     Matrix B = A.range(row0, row1, col0, col1);
     * </pre>
     *
     * @return
     */
    public int col0();

    /**
     * Tells if the underlying memory storage can be accessed in a continuous way.
     * <p>
     * When <code>contiguous</code> is <code>true</code>, certain operations are benefited by bulk operations.
     */
    public boolean isContiguous();

    /**
     * This is a convenience method intended to return {@link Flags#FORTRAN}
     *
     * @return Flags#FORTRAN
     */
    public boolean isFortran();

    /**
     * @return a set of flags in effect on this {@link Address} object.
     */
    public Set<Address.Flags> flags();

    public enum Flags {

        /**
         * Tells if this {@link Address} is intended to Fortran 1-based indexing.
         * <p>
         * In FORTRAN language, access to vectors and matrices are 1-based, like this:
         * <pre>
         *     for (i = 1; i <= n; i++)
         * </pre>
         * rather than what you can see in Java, C, C++, etc:
         * <pre>
         *     for (i = 0; i < n; i++)
         * </pre>
         */
        FORTRAN
    }

    /**
     * This is the main interface responsible for generation of Iterators associated to
     * classes {@link Matrix} and  {@link Array}
     *
     * @see ListIterator
     */
    public interface Offset {

        public abstract int op();
    }

    /**
     * This interface defines {@link Array} accessors
     *
     * @author Richard Gomes
     */
    public interface ArrayAddress extends Cloneable, Address {

        public ArrayAddress clone();

        public int op(int index);

        public ArrayAddress toFortran();

        public ArrayAddress toJava();

        public ArrayOffset offset();

        public ArrayOffset offset(int index);

        /**
         * This interface defines Iterators associated to class {@link Array}
         *
         * @see ListIterator
         */
        public interface ArrayOffset extends Offset, ListIterator<Double> {

            public void setIndex(final int index);
        }
    }

    /**
     * This interface defines {@link Matrix} accessors
     *
     * @author Richard Gomes
     */
    public interface MatrixAddress extends Cloneable, Address {

        public MatrixAddress clone();

        public int op(int row, int col);

        public MatrixAddress toFortran();

        public MatrixAddress toJava();

        public MatrixOffset offset();

        public MatrixOffset offset(final int row, final int col);

        /**
         * This interface defines Iterators associated to class {@link Matrix}
         *
         * @see ListIterator
         */
        public interface MatrixOffset extends Offset {

            public void setRow(final int row);

            public void setCol(final int col);

            public void nextRow();

            public void prevRow();

            public void nextCol();

            public void prevCol();
        }
    }
}
