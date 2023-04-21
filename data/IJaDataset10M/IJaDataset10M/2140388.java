package org.jfree.report.modules.misc.tablemodel;

import java.util.ArrayList;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

public class JoiningTableModel extends AbstractTableModel {

    private static class TablePosition {

        private TableModel tableModel;

        private String prefix;

        private int tableOffset;

        private int columnOffset;

        public TablePosition(final TableModel tableModel, final String prefix) {
            if (tableModel == null) {
                throw new NullPointerException("Model must not be null");
            }
            if (prefix == null) {
                throw new NullPointerException("Prefix must not be null.");
            }
            this.tableModel = tableModel;
            this.prefix = prefix;
        }

        public void updateOffsets(final int tableOffset, final int columnOffset) {
            this.tableOffset = tableOffset;
            this.columnOffset = columnOffset;
        }

        public String getPrefix() {
            return prefix;
        }

        public int getColumnOffset() {
            return columnOffset;
        }

        public TableModel getTableModel() {
            return tableModel;
        }

        public int getTableOffset() {
            return tableOffset;
        }
    }

    private class TableChangeHandler implements TableModelListener {

        public TableChangeHandler() {
        }

        /**
     * This fine grain notification tells listeners the exact range of cells, rows, or
     * columns that changed.
     */
        public void tableChanged(final TableModelEvent e) {
            if (e.getType() == TableModelEvent.HEADER_ROW) {
                updateStructure();
            } else if (e.getType() == TableModelEvent.INSERT || e.getType() == TableModelEvent.DELETE) {
                updateRowCount();
            } else {
                updateData();
            }
        }
    }

    private String[] columnNames;

    private Class[] columnTypes;

    private ArrayList models;

    private TableChangeHandler changeHandler;

    private int rowCount;

    public static final String TABLE_PREFIX_COLUMN = "TablePrefix";

    public JoiningTableModel() {
        models = new ArrayList();
        changeHandler = new TableChangeHandler();
    }

    public synchronized void addTableModel(final String prefix, final TableModel model) {
        models.add(new TablePosition(model, prefix));
        model.addTableModelListener(changeHandler);
        updateStructure();
    }

    public synchronized void removeTableModel(final TableModel model) {
        for (int i = 0; i < models.size(); i++) {
            final TablePosition position = (TablePosition) models.get(i);
            if (position.getTableModel() == model) {
                models.remove(model);
                model.removeTableModelListener(changeHandler);
                updateStructure();
                return;
            }
        }
        return;
    }

    public synchronized int getTableModelCount() {
        return models.size();
    }

    public synchronized TableModel getTableModel(final int pos) {
        final TablePosition position = (TablePosition) models.get(pos);
        return position.getTableModel();
    }

    protected synchronized void updateStructure() {
        final ArrayList columnNames = new ArrayList();
        final ArrayList columnTypes = new ArrayList();
        int rowOffset = 0;
        int columnOffset = 1;
        columnNames.add(TABLE_PREFIX_COLUMN);
        columnTypes.add(String.class);
        for (int i = 0; i < models.size(); i++) {
            final TablePosition pos = (TablePosition) models.get(i);
            pos.updateOffsets(rowOffset, columnOffset);
            final TableModel tableModel = pos.getTableModel();
            rowOffset += tableModel.getRowCount();
            columnOffset += tableModel.getColumnCount();
            for (int c = 0; c < tableModel.getColumnCount(); c++) {
                columnNames.add(pos.getPrefix() + "." + tableModel.getColumnName(c));
                columnTypes.add(tableModel.getColumnClass(c));
            }
        }
        this.columnNames = (String[]) columnNames.toArray(new String[columnNames.size()]);
        this.columnTypes = (Class[]) columnTypes.toArray(new Class[columnTypes.size()]);
        this.rowCount = rowOffset;
        fireTableStructureChanged();
    }

    protected synchronized void updateRowCount() {
        int rowOffset = 0;
        int columnOffset = 1;
        for (int i = 0; i < models.size(); i++) {
            final TablePosition model = (TablePosition) models.get(i);
            model.updateOffsets(rowOffset, columnOffset);
            rowOffset += model.getTableModel().getRowCount();
            columnOffset += model.getTableModel().getColumnCount();
        }
        fireTableStructureChanged();
    }

    protected void updateData() {
        fireTableDataChanged();
    }

    /**
   * Returns <code>Object.class</code> regardless of <code>columnIndex</code>.
   *
   * @param columnIndex the column being queried
   * @return the Object.class
   */
    public synchronized Class getColumnClass(final int columnIndex) {
        return columnTypes[columnIndex];
    }

    /**
   * Returns a default name for the column using spreadsheet conventions: A, B, C, ... Z,
   * AA, AB, etc.  If <code>column</code> cannot be found, returns an empty string.
   *
   * @param column the column being queried
   * @return a string containing the default name of <code>column</code>
   */
    public synchronized String getColumnName(final int column) {
        return columnNames[column];
    }

    /**
   * Returns false. JFreeReport does not like changing cells.
   *
   * @param rowIndex    the row being queried
   * @param columnIndex the column being queried
   * @return false
   */
    public final boolean isCellEditable(final int rowIndex, final int columnIndex) {
        return false;
    }

    /**
   * Returns the number of columns managed by the data source object. A <B>JTable</B> uses
   * this method to determine how many columns it should create and display on
   * initialization.
   *
   * @return the number or columns in the model
   *
   * @see #getRowCount
   */
    public synchronized int getColumnCount() {
        return columnNames.length;
    }

    /**
   * Returns the number of records managed by the data source object. A <B>JTable</B> uses
   * this method to determine how many rows it should create and display.  This method
   * should be quick, as it is call by <B>JTable</B> quite frequently.
   *
   * @return the number or rows in the model
   *
   * @see #getColumnCount
   */
    public synchronized int getRowCount() {
        return rowCount;
    }

    /**
   * Returns an attribute value for the cell at <I>columnIndex</I> and <I>rowIndex</I>.
   *
   * @param	rowIndex	the row whose value is to be looked up
   * @param	columnIndex the column whose value is to be looked up
   * @return	the value Object at the specified cell
   */
    public synchronized Object getValueAt(final int rowIndex, final int columnIndex) {
        final TablePosition pos = getTableModelForRow(rowIndex);
        if (pos == null) {
            return null;
        }
        if (columnIndex == 0) {
            return pos.getPrefix();
        }
        final int columnOffset = pos.getColumnOffset();
        if (columnIndex < columnOffset) {
            return null;
        }
        final TableModel tableModel = pos.getTableModel();
        if (columnIndex >= (columnOffset + tableModel.getColumnCount())) {
            return null;
        }
        return tableModel.getValueAt(rowIndex - pos.getTableOffset(), columnIndex - columnOffset);
    }

    private TablePosition getTableModelForRow(final int row) {
        for (int i = 0; i < models.size(); i++) {
            final TablePosition pos = (TablePosition) models.get(i);
            final int maxRow = pos.getTableOffset() + pos.getTableModel().getRowCount();
            if (row < maxRow) {
                return pos;
            }
        }
        return null;
    }
}
