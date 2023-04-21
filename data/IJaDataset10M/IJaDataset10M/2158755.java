package org.ladybug.gui.toolbox.tablecellrenderers;

/**
 * @author Aurelian Pop
 */
class TableCoordinate {

    private final int col;

    private final int row;

    private int hashCode = 0;

    public TableCoordinate(final int row, final int col) {
        this.row = row;
        this.col = col;
    }

    public int getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }

    @Override
    public int hashCode() {
        if (hashCode == 0) {
            hashCode = 17;
            hashCode = hashCode * 31 + row;
            hashCode = hashCode * 31 + col;
        }
        return hashCode;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof TableCoordinate) {
            final TableCoordinate that = (TableCoordinate) obj;
            return row == that.row && col == that.col;
        }
        return false;
    }

    @Override
    public String toString() {
        return "TableCoordinate [row=" + row + ", col=" + col + "]";
    }
}
