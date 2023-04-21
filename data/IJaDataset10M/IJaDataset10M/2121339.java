package logviewer;

import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

/**
 * A fairly fast table model for log files. This model allows adding multiple
 * rows at once, then fires a single event, which greatly improves performance. The
 * table data is stored in a List of Lists.  This model uses ArrayList explicitly
 * rather than allowing a generic List type.  The way data is added to this model
 * takes advantage of the best performance usage of ArrayList, that is, adding
 * new items at the end. Column data is also required to be in an ArrayList,
 * however, for usage in LogViewer, columns are not added, so there is no 
 * advantage of using an ArrayList over any other list.
 *
 * @version   $Revision: 1156 $
 */
public class LogTableModel extends AbstractTableModel {

    private java.util.ArrayList rowData;

    private int initialSize = 500;

    private ArrayList columnNames = null;

    /**
     * Constructor for LogTableModel, uses default initial size of 500.
     *
     * @param columnNames a list of column names, may not be null or empty
     */
    public LogTableModel(ArrayList columnNames) {
        this(columnNames, 500);
    }

    /**
     * Constructor for LogTableModel
     *
     * @param columnNames a list of column names, may not be null or empty.
     * @param initialSize the initial number of rows in the model.
     */
    public LogTableModel(ArrayList columnNames, int initialSize) {
        if (columnNames == null || columnNames.size() == 0) throw new IllegalArgumentException("column names not given");
        this.columnNames = columnNames;
        this.initialSize = initialSize;
        rowData = new ArrayList(initialSize);
    }

    /**
     * Adds a row of data to the model. 
     *
     * @param data  The data for a single row.
     */
    public void addRow(ArrayList data) {
        int row = rowData.size();
        rowData.add(data);
        fireTableRowsInserted(row, row);
    }

    /**
     * Adds several rows at once to the model.  It is much more efficient to
     * use this method than to make multiple calls to <code>addRow</code>.
     *
     * @param data  The row data, each item in the list must be an ArrayList of
     * data for a complete row.
     */
    public void addRows(ArrayList data) {
        rowData.ensureCapacity(rowData.size() + data.size());
        int row = rowData.size();
        rowData.addAll(data);
        fireTableRowsInserted(row, row + data.size());
    }

    /**
     * Gets the row count.
     *
     * @return   The row count
     */
    public int getRowCount() {
        return rowData.size();
    }

    /**
     * Gets the column count.
     *
     * @return   The column count
     */
    public int getColumnCount() {
        return columnNames.size();
    }

    /**
     * Gets the column name of the given column index.
     *
     * @param column index to column
     * @return  The name of the column
     * @throws IndexOutOfBoundsException if column is out of range, that is, if
     * column < 0 or column > the number of columns in the table.
     */
    public String getColumnName(int column) {
        return (String) columnNames.get(column);
    }

    /**
     * Gets the data value at the given cell index.
     *
     * @param rowIndex the row index
     * @param columnIndex the column index
     * @return             The value in the cell
     * @throws IndexOutOfBoundsException if rowIndex or columnIndex are out of
     * range.
     */
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (rowIndex > rowData.size() - 1 || rowIndex < 0) return null;
        if (columnIndex > ((ArrayList) rowData.get(rowIndex)).size() - 1 || columnIndex < 0) return null;
        return ((ArrayList) rowData.get(rowIndex)).get(columnIndex);
    }
}
