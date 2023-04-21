package org.apache.commons.math3.linear;

import org.apache.commons.math3.FieldElement;

/**
 * Interface defining a visitor for matrix entries.
 *
 * @param <T> the type of the field elements
 * @version $Id: FieldMatrixChangingVisitor.java 1244107 2012-02-14 16:17:55Z erans $
 * @since 2.0
 */
public interface FieldMatrixChangingVisitor<T extends FieldElement<?>> {

    /**
     * Start visiting a matrix.
     * <p>This method is called once before any entry of the matrix is visited.</p>
     * @param rows number of rows of the matrix
     * @param columns number of columns of the matrix
     * @param startRow Initial row index
     * @param endRow Final row index (inclusive)
     * @param startColumn Initial column index
     * @param endColumn Final column index (inclusive)
     */
    void start(int rows, int columns, int startRow, int endRow, int startColumn, int endColumn);

    /**
     * Visit one matrix entry.
     * @param row row index of the entry
     * @param column column index of the entry
     * @param value current value of the entry
     * @return the new value to be set for the entry
     */
    T visit(int row, int column, T value);

    /**
     * End visiting a matrix.
     * <p>This method is called once after all entries of the matrix have been visited.</p>
     * @return the value that the <code>walkInXxxOrder</code> must return
     */
    T end();
}
