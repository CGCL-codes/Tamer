package vgsoft.vgsudoku;

class NumberConflict {

    private boolean rowConflict = false, columnConflict = false, boxConflict = false;

    private int row = 0, rowIndex = 0, column = 0, columnIndex = 0, bRow = 0, bColumn = 0, bRowIndex = 0, bColumnIndex = 0;

    private Integer number = 0;

    /**
	 * Used to construct a new NumberConflict object. Use -1 for every value that is not used.
	 * @param number The conflicting number
	 * @param row The conflicting Row
	 * @param rowIndex When a Row conflict, the index of the number within the Row.
	 * @param column The conflicting Column
	 * @param columnIndex When a Column confict, the index of the number within the Column.
	 * @param boxRow The conflicting Box's top Row
	 * @param boxColumn The conflicting Box's left Column
	 * @param boxRowIndex When a Box conflict, the number's row in the Box.
	 * @param boxColumnIndex When a Box conflict, the number's column in the Box.
	 */
    NumberConflict(Integer number, int row, int rowIndex, int column, int columnIndex, int boxRow, int boxColumn, int boxRowIndex, int boxColumnIndex) {
        this.number = number;
        if (row >= 0) {
            this.row = row;
            this.rowIndex = rowIndex;
            this.rowConflict = true;
        }
        if (column >= 0) {
            this.column = column;
            this.columnIndex = columnIndex;
            this.columnConflict = true;
        }
        if (boxRow >= 0) {
            this.bRow = boxRow;
            this.bColumn = boxColumn;
            this.bRowIndex = boxRowIndex;
            this.bColumnIndex = boxColumnIndex;
            this.boxConflict = true;
        }
    }

    int getBoxColumn() {
        return bColumn;
    }

    int getBoxColumnIndex() {
        return bColumnIndex;
    }

    boolean isBoxConflict() {
        return boxConflict;
    }

    int getBoxRow() {
        return bRow;
    }

    int getBoxRowIndex() {
        return bRowIndex;
    }

    int getColumn() {
        return column;
    }

    boolean isColumnConflict() {
        return columnConflict;
    }

    int getColumnIndex() {
        return columnIndex;
    }

    Integer getNumber() {
        return number;
    }

    int getRow() {
        return row;
    }

    boolean isRowConflict() {
        return rowConflict;
    }

    int getRowIndex() {
        return rowIndex;
    }
}
