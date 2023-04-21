package net.sf.wtk.ui.widget.layout.grid;

import java.util.ArrayList;
import java.util.Iterator;
import net.sf.wtk.common.collections.ArraySheet;
import net.sf.wtk.common.collections.Sheet;

public class Grid<T> implements Iterable<Cell<T>> {

    private final int columns;

    private final Sheet<Cell<T>> cells;

    public Grid(int columns) {
        this(-1, columns);
    }

    public Grid(int rows, int columns) {
        this.columns = columns;
        this.cells = new ArraySheet<Cell<T>>(rows, columns);
    }

    public Cell<T> placeCell(int nextRow, int nextColumn, CellSpec cellSpec, T value) {
        CellSpec spec = cellSpec;
        if (spec.getColSpan() > columns) {
            throw new IllegalArgumentException("A grid with " + columns + " columns cannot contain a cell with colspan " + spec.getColSpan());
        }
        searchPosition: while (true) {
            if (nextColumn + spec.getColSpan() > columns) {
                nextRow++;
                nextColumn = 0;
            }
            for (int stopColumn = nextColumn + spec.getColSpan(), column = nextColumn; column < stopColumn; column++) {
                Cell<?> cell = cells.get(nextRow, column);
                if (cell != null) {
                    nextColumn = cell.getLeftmostColumn() + cell.getSpec().getColSpan();
                    if (nextColumn >= columns) {
                        nextRow++;
                        nextColumn = 0;
                    }
                    continue searchPosition;
                }
            }
            break;
        }
        Cell<T> result = new Cell<T>(spec, nextRow, nextColumn, value);
        setCell(nextRow, nextColumn, spec, result);
        return result;
    }

    public Cell<T> getCell(int row, int column) {
        return cells.get(row, column);
    }

    public Iterator<Cell<T>> iterator() {
        ArrayList<Cell<T>> result = new ArrayList<Cell<T>>();
        for (int stopRow = cells.getRows(), row = 0; row < stopRow; row++) {
            for (int stopColumn = cells.getColumns(), column = 0; column < stopColumn; column++) {
                Cell<T> cell = cells.get(row, column);
                if (cell.getTopmostRow() == row && cell.getLeftmostColumn() == column) {
                    result.add(cell);
                }
            }
        }
        return result.iterator();
    }

    public int getRows() {
        return cells.getRows();
    }

    public int getColumns() {
        return cells.getColumns();
    }

    public void embed(Cell<?> cell, Grid<T> grid) {
        int topmostRow = cell.getTopmostRow();
        int leftmostColumn = cell.getLeftmostColumn();
        CellSpec cellSpec = cell.getSpec();
        embed(topmostRow, leftmostColumn, cellSpec, grid);
    }

    public void embed(int topmostRow, int leftmostColumn, CellSpec cellSpec, Grid<T> grid) {
        if ((!cells.isWithinBounds(topmostRow, leftmostColumn))) {
            throw new IllegalArgumentException("Given cell is not part of this grid.");
        }
        if ((grid.getRows() != cellSpec.getRowSpan()) || (grid.getColumns() != cellSpec.getColSpan())) {
            throw new IllegalArgumentException("Dimensions of given grid do not match the dimension of the cell, to which is should be embedded.");
        }
        setCell(topmostRow, leftmostColumn, cellSpec, null);
        for (Cell<T> embeddedCell : grid) {
            placeCell(topmostRow + embeddedCell.getTopmostRow(), leftmostColumn + embeddedCell.getLeftmostColumn(), embeddedCell.getSpec(), embeddedCell.getContents());
        }
    }

    private void setCell(int rowPosition, int columnPosition, CellSpec spec, Cell<T> cell) {
        for (int stopRow = rowPosition + spec.getRowSpan(), row = rowPosition; row < stopRow; row++) {
            for (int stopColumn = columnPosition + spec.getColSpan(), column = columnPosition; column < stopColumn; column++) {
                cells.set(row, column, cell);
            }
        }
    }
}
