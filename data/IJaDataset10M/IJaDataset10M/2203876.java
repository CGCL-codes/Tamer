package org.jfree.report;

import javax.swing.table.TableModel;

/**
 * Creation-Date: 19.02.2006, 17:00:10
 *
 * @author Thomas Morgner
 */
public class TableReportData implements ReportData {

    private TableModel tableModel;

    private int cursor;

    private int rowMax;

    private int rowMin;

    private int cursorMaxPosition;

    public TableReportData(final TableModel tableModel) {
        this(tableModel, 0, tableModel.getRowCount());
    }

    public TableReportData(final TableModel tableModel, final int start, final int length) {
        this.tableModel = tableModel;
        this.rowMax = start + length;
        this.rowMin = start;
        this.cursorMaxPosition = length;
    }

    public int getColumnCount() throws DataSourceException {
        return tableModel.getColumnCount();
    }

    public boolean isReadable() throws DataSourceException {
        return cursor > 0 && cursor <= cursorMaxPosition;
    }

    public boolean setCursorPosition(final int row) throws DataSourceException {
        if (row > cursorMaxPosition) {
            return false;
        } else if (row < 0) {
            return false;
        }
        cursor = row;
        return true;
    }

    /**
   * This operation checks, whether a call to next will be likely to succeed. If
   * there is a next data row, this should return true.
   *
   * @return
   * @throws org.jfree.report.DataSourceException
   *
   */
    public boolean isAdvanceable() throws DataSourceException {
        return cursor < cursorMaxPosition;
    }

    public String getColumnName(final int column) throws DataSourceException {
        return tableModel.getColumnName(column);
    }

    public Object get(final int column) throws DataSourceException {
        if (isReadable() == false) {
            throw new DataSourceException("Unable to read from datasource");
        }
        return tableModel.getValueAt(cursor - 1, column);
    }

    public boolean next() throws DataSourceException {
        if (cursor >= cursorMaxPosition) {
            return false;
        }
        cursor += 1;
        return true;
    }

    public void close() throws DataSourceException {
    }

    public int getCursorPosition() throws DataSourceException {
        return cursor;
    }
}
