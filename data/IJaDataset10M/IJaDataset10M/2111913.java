package com.bradrydzewski.gwtgantt.table.override;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

/**
 * A rectangular grid that can contain text, html, or a child
 * {@link com.google.gwt.user.client.ui.Widget} within its cells. It must be
 * resized explicitly to the desired number of rows and columns.
 * <p>
 * <img class='gallery' src='doc-files/Table.png'/>
 * </p>
 * <p>
 * <h3>Example</h3>
 * {@example com.google.gwt.examples.GridExample}
 * </p>
 * 
 * <h3>Use in UiBinder Templates</h3>
 * <p>
 * Grid widget consists of &lt;g:row> elements. Each &lt;g:row> element
 * can contain one or more &lt;g:cell> or &lt;g:customCell> elements.
 * Using &lt;g:cell> attribute it is possible to place pure HTML content. 
 * &lt;g:customCell> is used as a container for 
 * {@link com.google.gwt.user.client.ui.Widget} type objects. (Note that the
 * tags of the row, cell and customCell elements are not capitalized. This
 * is meant to signal that the item is not a runtime object, and so cannot
 * have a <code>ui:field</code> attribute.)
 * <p>
 * For example:
 * 
 * <pre>
 * &lt;g:Grid>
 *  &lt;g:row>
 *    &lt;g:customCell>
 *      &lt;g:Label>foo&lt;/g:Label>
 *    &lt;/g:customCell>
 *    &lt;g:customCell>
 *      &lt;g:Label>bar&lt;/g:Label>
 *    &lt;/g:customCell>
 *  &lt;/g:row>
 *  &lt;g:row>
 *    &lt;g:cell>
 *      &lt;div>foo&lt;/div>
 *    &lt;/g:cell>
 *    &lt;g:cell>
 *      &lt;div>bar&lt;/div>
 *    &lt;/g:cell>
 *  &lt;/g:row>
 * &lt;/g:Grid>
 * </pre>
 */
public class Grid extends HTMLTable {

    /**
   * Native method to add rows into a table with a given number of columns.
   * 
   * @param table the table element
   * @param rows number of rows to add
   * @param columns the number of columns per row
   */
    private static native void addRows(Element table, int rows, int columns);

    /**
   * Number of columns in the current grid.
   */
    protected int numColumns;

    /**
   * Number of rows in the current grid.
   */
    protected int numRows;

    /**
   * Constructor for <code>Grid</code>.
   */
    public Grid() {
        super();
        setCellFormatter(new CellFormatter());
        setRowFormatter(new RowFormatter());
        setColumnFormatter(new ColumnFormatter());
    }

    /**
   * Constructs a grid with the requested size.
   * 
   * @param rows the number of rows
   * @param columns the number of columns
   * @throws IndexOutOfBoundsException
   */
    public Grid(int rows, int columns) {
        this();
        resize(rows, columns);
    }

    /**
   * Replaces the contents of the specified cell with a single space.
   * 
   * @param row the cell's row
   * @param column the cell's column
   * @throws IndexOutOfBoundsException
   */
    @Override
    public boolean clearCell(int row, int column) {
        Element td = getCellFormatter().getElement(row, column);
        boolean b = internalClearCell(td, false);
        DOM.setInnerHTML(td, "&nbsp;");
        return b;
    }

    /**
   * Return number of columns. For grid, row argument is ignored as all grids
   * are rectangular.
   */
    @Override
    public int getCellCount(int row) {
        return numColumns;
    }

    /**
   * Gets the number of columns in this grid.
   * 
   * @return the number of columns
   */
    public int getColumnCount() {
        return numColumns;
    }

    /**
   * Return number of rows.
   */
    @Override
    public int getRowCount() {
        return numRows;
    }

    /**
   * Inserts a new row into the table. If you want to add multiple rows at once,
   * use {@link #resize(int, int)} or {@link #resizeRows(int)} as they are more
   * efficient.
   * 
   * @param beforeRow the index before which the new row will be inserted
   * @return the index of the newly-created row
   * @throws IndexOutOfBoundsException
   */
    @Override
    public int insertRow(int beforeRow) {
        int index = super.insertRow(beforeRow);
        numRows++;
        for (int i = 0; i < numColumns; i++) {
            insertCell(index, i);
        }
        return index;
    }

    @Override
    public void removeRow(int row) {
        super.removeRow(row);
        numRows--;
    }

    /**
   * Resizes the grid.
   * 
   * @param rows the number of rows
   * @param columns the number of columns
   * @throws IndexOutOfBoundsException
   */
    public void resize(int rows, int columns) {
        resizeColumns(columns);
        resizeRows(rows);
    }

    /**
   * Resizes the grid to the specified number of columns.
   * 
   * @param columns the number of columns
   * @throws IndexOutOfBoundsException
   */
    public void resizeColumns(int columns) {
        if (numColumns == columns) {
            return;
        }
        if (columns < 0) {
            throw new IndexOutOfBoundsException("Cannot set number of columns to " + columns);
        }
        if (numColumns > columns) {
            for (int i = 0; i < numRows; i++) {
                for (int j = numColumns - 1; j >= columns; j--) {
                    removeCell(i, j);
                }
            }
        } else {
            for (int i = 0; i < numRows; i++) {
                for (int j = numColumns; j < columns; j++) {
                    insertCell(i, j);
                }
            }
        }
        numColumns = columns;
        getColumnFormatter().resizeColumnGroup(columns, false);
    }

    /**
   * Resizes the grid to the specified number of rows.
   * 
   * @param rows the number of rows
   * @throws IndexOutOfBoundsException
   */
    public void resizeRows(int rows) {
        if (numRows == rows) {
            return;
        }
        if (rows < 0) {
            throw new IndexOutOfBoundsException("Cannot set number of rows to " + rows);
        }
        if (numRows < rows) {
            addRows(getBodyElement(), rows - numRows, numColumns);
            numRows = rows;
        } else {
            while (numRows > rows) {
                removeRow(numRows - 1);
            }
        }
    }

    /**
   * Creates a new, empty cell.
   */
    @Override
    protected Element createCell() {
        Element td = super.createCell();
        DOM.setInnerHTML(td, "&nbsp;");
        return td;
    }

    /**
   * Checks that a cell is a valid cell in the table.
   * 
   * @param row the cell's row
   * @param column the cell's column
   * @throws IndexOutOfBoundsException
   */
    @Override
    protected void prepareCell(int row, int column) {
        prepareRow(row);
        if (column < 0) {
            throw new IndexOutOfBoundsException("Cannot access a column with a negative index: " + column);
        }
        if (column >= numColumns) {
            throw new IndexOutOfBoundsException("Column index: " + column + ", Column size: " + numColumns);
        }
    }

    /**
   * Checks that the column index is valid.
   * 
   * @param column The column index to be checked
   * @throws IndexOutOfBoundsException if the column is negative
   */
    @Override
    protected void prepareColumn(int column) {
        super.prepareColumn(column);
        if (column >= numColumns) {
            throw new IndexOutOfBoundsException("Column index: " + column + ", Column size: " + numColumns);
        }
    }

    /**
   * Checks that the row index is valid.
   * 
   * @param row The row index to be checked
   * @throws IndexOutOfBoundsException if the row is negative
   */
    @Override
    protected void prepareRow(int row) {
        if (row < 0) {
            throw new IndexOutOfBoundsException("Cannot access a row with a negative index: " + row);
        }
        if (row >= numRows) {
            throw new IndexOutOfBoundsException("Row index: " + row + ", Row size: " + numRows);
        }
    }
}
