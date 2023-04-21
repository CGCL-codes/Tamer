package com.ek.mitapp.ui.table;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.swing.table.AbstractTableModel;
import org.apache.log4j.Logger;

/**
 * An abstract table model for holding mitigation data.
 * <p>
 * Id: $Id: $
 *
 * @author Dave Irwin (dirwin@ekmail.com)
 */
public abstract class AbstractDataTableModel<T extends AbstractDataTableRow> extends AbstractTableModel implements JExcelTableModel<T>, PropertyChangeListener {

    /**
	 * Define the root logger.
	 */
    private static Logger logger = Logger.getLogger(AbstractDataTableModel.class.getName());

    /**
	 * Keep track of the rows.
	 */
    private List<T> rows = new CopyOnWriteArrayList<T>();

    /**
	 * Default constructor.
	 */
    public AbstractDataTableModel() {
        super();
    }

    /**
	 * @return The column identifiers
	 */
    protected abstract Vector<String> getColumnIdentifiers();

    /**
	 * Add a row to the table model.
	 * 
	 * @param row Row to add
	 */
    public void addRow(T row) {
        rows.add(row);
        row.addPropertyChangeListener(this);
        fireTableDataChanged();
    }

    /**
	 * Get a row from the table model. This method will return null if no matching row is found.
	 * 
	 * @param rowIndex Row index
	 * @return Corresponding row
	 */
    public T getRow(int rowIndex) {
        return rows.get(rowIndex);
    }

    /**
	 * Get a row from the table model. This method will return null if no matching row is found.
	 * 
	 * @param rowId The row ID
	 * @return Corresponding row
	 */
    public T getRow(Object rowId) {
        for (T row : rows) {
            if (row.getRowId().equals(rowId)) {
                return row;
            }
        }
        return null;
    }

    /**
	 * Get the list of rows in the table model.
	 *
	 * @return A <code>List</code> of current table rows
	 */
    public List<T> getRows() {
        return rows;
    }

    /**
	 * Attempt to remove a row from the table model.
	 * 
	 * @param rowId Row ID of row to remove
	 */
    public void removeRow(Object rowId) {
        logger.debug("Deleting row with id " + rowId);
        T row = getRow(rowId);
        if (row != null) {
            row.removePropertyChangeListener(this);
            rows.remove(row);
            fireTableDataChanged();
        }
    }

    /**
	 * Attempt to remove all the row from the table model.
	 */
    public synchronized void removeAllRows() {
        logger.debug("Deleting all rows");
        for (T row : rows) {
            if (row != null) {
                row.removePropertyChangeListener(this);
                rows.remove(row);
            }
        }
        fireTableDataChanged();
    }

    /**
	 * @see javax.swing.table.TableModel#getColumnCount()
	 */
    public int getColumnCount() {
        return getColumnIdentifiers().size();
    }

    /**
	 * @see javax.swing.table.TableModel#getRowCount()
	 */
    public int getRowCount() {
        return rows.size();
    }

    /**
	 * @see javax.swing.table.TableModel#getColumnName(int)
	 */
    @Override
    public String getColumnName(int col) {
        return getColumnIdentifiers().get(col);
    }

    /**
	 * JTable uses this method to determine the default renderer/
	 * editor for each cell.  If we didn't implement this method,
	 * then the last column would contain text ("true"/"false"),
	 * rather than a check box.
	 * 
	 * @see javax.swing.table.TableModel#getColumnClass(int)
	 */
    @Override
    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

    /**
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 */
    public abstract Object getValueAt(int rowIndex, int colIndex);

    /**
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
    public void propertyChange(PropertyChangeEvent pce) {
    }
}
