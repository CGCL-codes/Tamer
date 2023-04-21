package es.optsicom.lib.analysis.table;

public class SheetTable {

    Cell[][] cells;

    public SheetTable(int numRows, int numCols) {
        this.cells = new Cell[numRows][numCols];
    }

    public void setCell(int numRow, int numCol, Cell cell) {
        this.cells[numRow][numCol] = cell;
    }

    public Cell getCell(int numRow, int numCol) {
        try {
            return this.cells[numRow][numCol];
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    public int getNumRows() {
        return cells.length;
    }

    public int getNumCols() {
        return cells[0].length;
    }
}
