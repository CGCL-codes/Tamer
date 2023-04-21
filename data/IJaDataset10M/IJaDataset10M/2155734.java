package org.modss.facilitator.ui.matrix;

import org.modss.facilitator.model.v1.Cell;
import org.modss.facilitator.model.v1.ModelFactory;
import org.modss.facilitator.model.v1.MutableCell;
import org.modss.facilitator.model.v1.BaseCriteria;
import org.modss.facilitator.shared.window.*;
import org.modss.facilitator.util.collection.matrix.*;
import org.modss.facilitator.util.collection.list.*;
import org.swzoo.log2.core.*;
import java.util.*;
import javax.swing.table.*;
import javax.swing.event.*;

/**
 * An adapter for the main portion of the matrix table.
 * Since the JTable design does not adequately support spreadsheet style
 * tables (the JTable focuses too much on columns), a new user interface
 * component has been built which uses this component to manage the main
 * portion of the table.
 *
 * @see RowModelAdapter
 */
public class MainModelAdapter extends AbstractTableModel {

    /** Criteria list */
    NotificationList _cri = null;

    /** Matrix */
    ContentMutableMatrix _matrix = null;

    /**
     * Constructor.
     */
    public MainModelAdapter() {
    }

    /**
     * Set/Reset the real model.
     */
    public void setModel(ContentMutableMatrix matrix, NotificationList criteria) {
        LogTools.trace(logger, 25, "MainModelAdapter.setModel()");
        _matrix = matrix;
        _cri = criteria;
        LogTools.trace(logger, 25, "MainModelAdapter.setModel() - fireTableStructureChanged()");
        fireTableStructureChanged();
    }

    /**
     * Provide the column count.
     *
     * @return the number of columns in the matrix.  Equivalent to the number
     * of criteria.
     */
    public int getColumnCount() {
        int count = (_matrix == null) ? 0 : _matrix.getColumnCount();
        LogTools.trace(logger, 25, "MainModelAdapter.getColumnCount()=" + count);
        return count;
    }

    /**
     * Provide the row count.
     *
     * @return the number of rows in the matrix.  Equivalent to the number
     * of alternatives.
     */
    public int getRowCount() {
        int count = (_matrix == null) ? 0 : _matrix.getRowCount();
        LogTools.trace(logger, 25, "MainModelAdapter.getRowCount()=" + count);
        return count;
    }

    /**
     * Get class of objects within the specified column.
     * For the current implementation <b>Double</b> is the class for all columns.
     * 
     * @param col the column index.
     * @return the class.
     */
    public Class getColumnClass(int col) {
        return Double.class;
    }

    /**
     * Provide column name.
     * This comes from the base criteria represented by the column index.
     *
     * @param col the column index.
     * @return the heading name for the column.
     */
    public String getColumnName(int col) {
        String name;
        if (_cri == null) {
            name = null;
        } else {
            BaseCriteria cri = (BaseCriteria) _cri.elementAt(col);
            if (cri == null) {
                LogTools.warn(logger, "MainModelAdapter.getColumnName(" + col + ") No entry in the criteria list.  This should not happen");
                name = null;
            } else {
                name = cri.getLongDescription();
            }
        }
        LogTools.trace(logger, 25, "MainModelAdapter.getColumnName(" + col + ")=" + name);
        return name;
    }

    /**
     * Provide value at given location.
     *
     * @param row the row index [0..n]  of the requested item.
     * @param col the column index [0..n]  of the requested item.
     */
    public Object getValueAt(int row, int col) {
        LogTools.trace(logger, 25, "MainModelAdapter.getValue(" + row + "," + col + ")");
        Object o;
        if (row >= getRowCount() || col >= getColumnCount() || row < 0 || col < 0) {
            LogTools.trace(logger, 25, "MainModelAdapter.getValue(row=" + row + ",col=" + col + ") - Out of bounds request (rows=" + getRowCount() + ",cols=" + getColumnCount() + ").");
            return null;
        }
        Cell cell = (Cell) _matrix.get(new MatrixLocation(row, col));
        if (cell == null) return null;
        return new Double(cell.getValue());
    }

    /**
     * Set the value.  This will occur after the cell has been edited.
     *
     * @param o the object containing the new value.
     * @param row the row index.
     * @param col the column index.
     */
    public void setValueAt(Object o, int row, int col) {
        LogTools.trace(logger, 25, "MainModelAdapter.setValueAt(" + o + "," + row + "," + col + ")");
        if (o == null) {
            LogTools.trace(logger, 25, "MainModelAdapter.setValueAt(" + o + "," + row + "," + col + ")");
            return;
        }
        if (!(o instanceof Double)) {
            LogTools.warn(logger, "MainModelAdapter.setValueAt(" + o.toString() + "," + row + "," + col + ") - Object is not a Double!");
            return;
        }
        Double d = (Double) o;
        MutableCell cell = (MutableCell) _matrix.get(new MatrixLocation(row, col));
        if (cell != null && cell.getValue() == d.doubleValue()) {
            LogTools.trace(logger, 25, "MainModelAdapter.setValueAt(" + o.toString() + "," + row + "," + col + ") - Value has not changed; bailing...");
            return;
        }
        cell = ModelFactory.createCell(d.doubleValue());
        _matrix.set(new MatrixLocation(row, col), cell);
    }

    /**
     * Editable status of this cell.
     * Currently all cells are editable.
     *
     * @param row the row index.
     * @param col the column index.
     * @return true if the cell is editable.
     */
    public boolean isCellEditable(int row, int col) {
        return true;
    }

    /** Logger. */
    private static final Logger logger = LogFactory.getLogger();
}
