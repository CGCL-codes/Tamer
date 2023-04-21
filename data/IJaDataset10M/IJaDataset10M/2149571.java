package org.jazzteam.edu.algo.matrixOperations;

/**
 * Contains information about a square submatrix within a matrix.
 * 
 * @author Alexey Kononov
 */
public class SquareSubmatrixInfo implements Comparable<SquareSubmatrixInfo> {

    private int parentRow;

    private int parentColumn;

    private int size;

    public int getParentRow() {
        return parentRow;
    }

    public void setParentRow(int parentRow) {
        this.parentRow = parentRow;
    }

    public int getParentColumn() {
        return parentColumn;
    }

    public void setParentColumn(int parentColumn) {
        this.parentColumn = parentColumn;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    /**
	 * Compares this object to another based on the values of their <code>size</code> fields.
	 */
    @Override
    public int compareTo(SquareSubmatrixInfo other) {
        int result;
        if (other == null) {
            result = 1;
        } else {
            result = ((Integer) size).compareTo(other.size);
        }
        return result;
    }
}
