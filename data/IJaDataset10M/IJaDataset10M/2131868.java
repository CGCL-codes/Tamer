package math.linearalgebra;

import java.io.Serializable;

/**
 * A square block of elements of arbitrary type.
 * @author egoff
 *
 * @param <T>
 */
public interface Matrix<T> extends Serializable {

    /**
	 * @return The # rows
	 */
    public int getRowCount();

    /**
	 * @return The # columns
	 */
    public int getColumnCount();

    /**
	 * @param row
	 * @return the vector for the given row.
	 */
    public Vector<T> getRow(int row);

    /**
	 * @param column
	 * @return the vector for the given column.
	 */
    public Vector<T> getColumn(int column);
}
