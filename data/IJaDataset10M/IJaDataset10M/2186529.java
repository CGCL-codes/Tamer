package org.plazmaforge.studio.reportdesigner.model.table;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.plazmaforge.studio.reportdesigner.model.DesignContainer;
import org.plazmaforge.studio.reportdesigner.model.IToolElement;
import org.plazmaforge.studio.reportdesigner.model.DesignElement;

public class Table extends DesignContainer implements ITable, IToolElement {

    public static final int TABLE_CORNER_WIDTH = 2;

    /**
     * Table header
     */
    private ITableHeader tableHeader;

    /**
     * Table body
     */
    private ITableBody tableBody;

    final PropertyChangeListener tableListener = new PropertyChangeListener() {

        public void propertyChange(PropertyChangeEvent evt) {
            if (PROPERTY_WIDTH.equals(evt.getPropertyName())) {
                resizeColumns((Integer) evt.getNewValue());
            } else if (PROPERTY_HEIGHT.equals(evt.getPropertyName())) {
                resizeRows((Integer) evt.getNewValue());
            } else if (PROPERTY_SIZE.equals(evt.getPropertyName())) {
                Dimension size = (Dimension) evt.getNewValue();
                resizeColumns(size == null ? null : size.width);
                resizeRows(size == null ? null : size.height);
            } else if (PROPERTY_BOUNDS.equals(evt.getPropertyName())) {
                Rectangle rectangle = (Rectangle) evt.getNewValue();
                resizeColumns(rectangle == null ? null : rectangle.width);
                resizeRows(rectangle == null ? null : rectangle.height);
            }
        }
    };

    private ITableHeader getTableHeader() {
        if (tableHeader == null) {
            tableHeader = new TableHeader(this);
        }
        return tableHeader;
    }

    public Table() {
        addPropertyChangeListener(tableListener);
    }

    /**
     * Create simple table
     * 
     * - 2 rows
     * - 2 columns
     * 
     * @param table
     */
    public static void createSimpleTable(ITable table) {
        createTable(table, 2, 2);
    }

    /**
     * Create table with <code>columnCount</code> columns and <code>rowCount</code> rows  
     * @param table
     * @param columnCount
     * @param rowCount
     */
    public static void createTable(ITable table, int columnCount, int rowCount) {
        int cellSize = 50;
        List<TableColumn> columns = new ArrayList<TableColumn>();
        for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
            TableColumn col = new TableColumn();
            col.setWidth(cellSize);
            table.addColumn(col);
            columns.add(col);
        }
        for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
            TableRow row = new TableRow();
            row.setHeight(cellSize);
            table.addRow(row);
            for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
                TableColumn col = columns.get(columnIndex);
                TableCell cell = new TableCell();
                cell.setColumn(col);
                cell.setRow(row);
                table.addCell(cell);
            }
        }
    }

    private ITableBody getTableBody() {
        if (tableBody == null) {
            tableBody = new TableBody(this);
        }
        return tableBody;
    }

    public List<ITableRow> getRows() {
        return getTableBody().getRows();
    }

    public List<ITableCell> getCells() {
        return getTableBody().getCells();
    }

    public List<ITableColumn> getColumns() {
        return getTableHeader().getColumns();
    }

    public void addColumn(ITableColumn column) {
        addColumn(column, true);
    }

    public void addColumn(ITableColumn column, boolean isFireStructure) {
        _addColumn(column);
        addChild(column, isFireStructure);
    }

    public void removeColumn(ITableColumn column) {
        _removeColumn(column);
        removeChild((DesignElement) column);
    }

    public void addRow(ITableRow row) {
        addRow(row, true);
    }

    public void addRow(ITableRow row, boolean isFireStructure) {
        _addRow(row);
        addChild(row, isFireStructure);
    }

    public void removeRow(ITableRow row) {
        _removeRow(row);
        removeChild((DesignElement) row);
    }

    public void addCell(ITableCell cell) {
        addCell(cell, true);
    }

    public void addCell(ITableCell cell, boolean isFireStructure) {
        _addCell(cell);
        addChild(cell, isFireStructure);
    }

    public void removeCell(ITableCell cell) {
        _removeCell(cell);
        removeChild((DesignElement) cell);
    }

    protected void _addColumn(ITableColumn column) {
        getTableHeader().addColumn(column);
    }

    protected void _removeColumn(ITableColumn column) {
        getTableHeader().removeColumn(column);
    }

    protected void _addRow(ITableRow row) {
        getTableBody().addRow(row);
    }

    protected void _removeRow(ITableRow row) {
        getTableBody().removeRow(row);
    }

    protected void _addCell(ITableCell cell) {
        getTableBody().addCell(cell);
    }

    protected void _removeCell(ITableCell cell) {
        getTableBody().removeCell(cell);
    }

    protected void clear(boolean isFireStructure) {
        _removeAllColumns();
        _removeAllRows();
        _removeAllCells();
        removeChildren(isFireStructure);
    }

    protected void _removeAllColumns() {
        getTableHeader().removeAllColumns();
    }

    protected void _removeAllRows() {
        getTableBody().removeAllRows();
    }

    protected void _removeAllCells() {
        getTableBody().removeAllCells();
    }

    public void removeAllColumns() {
        _removeAllColumns();
    }

    public void removeAllRows() {
        _removeAllRows();
    }

    public void removeAllCells() {
        _removeAllCells();
    }

    /**
     * Resize all rows after set height of the table
     */
    protected void resizeRows(Integer height) {
        if (height == null || height < 0) {
            return;
        }
        height -= TABLE_CORNER_WIDTH;
        if (height < 0) {
            return;
        }
        List<ITableRow> rows = getRows();
        int count = rows.size();
        if (count == 0) {
            return;
        }
        int lastIndex = count - 1;
        int curLength = 0;
        int h = 0;
        ITableRow row = null;
        for (int i = 0; i < count; i++) {
            row = rows.get(i);
            if (curLength >= height) {
                row.setHeight(0);
                continue;
            }
            h = row.getHeight();
            if (h < 0) {
                h = 0;
            }
            if (curLength + h > height) {
                h = height - curLength;
                if (h < 0) {
                    h = 0;
                }
            }
            curLength += h;
            if (i == lastIndex) {
                if (curLength < height) {
                    h = h + height - curLength;
                }
            }
            row.setHeight(h);
        }
    }

    /**
     * Resize all columns after set width of the table
     */
    protected void resizeColumns(Integer width) {
        if (width == null || width < 0) {
            return;
        }
        width -= TABLE_CORNER_WIDTH;
        if (width < 0) {
            return;
        }
        List<ITableColumn> columns = getColumns();
        int count = columns.size();
        if (count == 0) {
            return;
        }
        int lastIndex = count - 1;
        int curLength = 0;
        int w = 0;
        ITableColumn column = null;
        for (int i = 0; i < count; i++) {
            column = columns.get(i);
            if (curLength >= width) {
                column.setWidth(0);
                continue;
            }
            w = column.getWidth();
            if (w < 0) {
                w = 0;
            }
            if (curLength + w > width) {
                w = width - curLength;
                if (w < 0) {
                    w = 0;
                }
            }
            curLength += w;
            if (i == lastIndex) {
                if (curLength < width) {
                    w = w + width - curLength;
                }
            }
            column.setWidth(w);
        }
    }

    public int getRowsHeight() {
        int h = TableUtils.getRowsHeight(this);
        h += TABLE_CORNER_WIDTH;
        return h;
    }

    public int getColumnsWidth() {
        int w = TableUtils.getColumnsWidth(this);
        w += TABLE_CORNER_WIDTH;
        return w;
    }

    public boolean isEmptyColumns() {
        return getTableHeader().isEmptyColumns();
    }

    public boolean isLastColumn(ITableColumn column) {
        return getTableHeader().isLastColumn(column);
    }

    public int indexOfColumn(ITableColumn column) {
        return getTableHeader().indexOfColumn(column);
    }

    public boolean isEmptyRows() {
        return getTableBody().isEmptyRows();
    }

    public boolean isLastRow(ITableRow row) {
        return getTableBody().isLastRow(row);
    }

    public int indexOfRow(ITableRow row) {
        return getTableBody().indexOfRow(row);
    }

    public int getColumnCount() {
        return getTableHeader().getColumnCount();
    }

    public int getRowCount() {
        return getTableBody().getRowCount();
    }

    /**
     * Insert column content to <code>index</code> position
     */
    public void insertColumnContent(int index, int width) {
        checkColumnIndex(index);
    }

    /**
     * Add column content to end
     * @param width
     */
    public void addColumnContent(int width) {
    }

    /**
     * Remove column content
     * @param index
     */
    public void removeColumnContent(int index) {
        checkColumnIndex(index);
    }

    /**
     * Insert row content to <code>index</code> position
     */
    public void insertRowContent(int index, int height) {
    }

    /**
     * Add row content to end
     * @param height
     */
    public void addRowContent(int height) {
    }

    /**
     * Remove row content
     * @param index
     */
    public void removeRowContent(int index) {
    }

    protected void checkColumnIndex(int index) {
        if (index < 0 || index >= getColumnCount()) {
            throw new RuntimeException("Invalid column index [index=" + index + ", column count=" + getColumnCount() + "]");
        }
    }

    public static List<ITableCell> getCellsOfRow(ITableRow row) {
        return TableUtils.getCellsOfRow(row);
    }

    public static List<ITableCell> getCellsOfColumn(ITableColumn column) {
        return TableUtils.getCellsOfColumn(column);
    }

    public static List<ITableCell> getCellsByColumnRange(List<ITableCell> cells, int startColumnIndex, int endColumnIndex) {
        return TableUtils.getCellsByColumnRange(cells, startColumnIndex, endColumnIndex);
    }

    public static List<ITableCell> getCellsByRowRange(List<ITableCell> cells, int startRowIndex, int endRowIndex) {
        return TableUtils.getCellsByRowRange(cells, startRowIndex, endRowIndex);
    }

    public static List<ITableCell> getCellsByColumnRange(ITableRow row, int startColumnIndex, int endColumnIndex) {
        return TableUtils.getCellsByColumnRange(row, startColumnIndex, endColumnIndex);
    }

    public static List<ITableCell> getCellsByRowRange(ITableColumn column, int startRowIndex, int endRowIndex) {
        return TableUtils.getCellsByRowRange(column, startRowIndex, endRowIndex);
    }

    public static ITableCell findCellByColumn(List<ITableCell> cells, int columnIndex) {
        return TableUtils.findCellByColumn(cells, columnIndex);
    }

    public static ITableCell findCellByRow(List<ITableCell> cells, int rowIndex) {
        return TableUtils.findCellByRow(cells, rowIndex);
    }

    public int[] findEmptySpaceOfRow(ITableRow row, int startColumnIndex, int endColumnIndex) {
        return TableUtils.findEmptySpaceOfRow(row, startColumnIndex, endColumnIndex);
    }

    public int[] findEmptySpaceOfRow(List<ITableCell> cells, ITableRow row, int startColumnIndex, int endColumnIndex) {
        return TableUtils.findEmptySpaceOfRow(cells, row, startColumnIndex, endColumnIndex);
    }

    public int[] findEmptySpaceOfColumn(ITableColumn column, int startRowIndex, int endRowIndex) {
        return TableUtils.findEmptySpaceOfColumn(column, startRowIndex, endRowIndex);
    }

    public int[] findEmptySpaceOfColumn(List<ITableCell> cells, ITableColumn column, int startRowIndex, int endRowIndex) {
        return TableUtils.findEmptySpaceOfColumn(cells, column, startRowIndex, endRowIndex);
    }

    public void setCellsBorder() {
        setCellsBorder(true);
    }

    public void setCellsBorder(boolean flag) {
        List<ITableCell> cells = getCells();
        if (cells == null || cells.isEmpty()) {
            return;
        }
        for (ITableCell cell : cells) {
            cell.setBorder(flag);
        }
    }
}
