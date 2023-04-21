package com.electionpredictor.ui.table;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

/**
 * Model used for data
 * 
 * @author Niels Stchedroff
 */
public class DataTableModel implements TableModel {

    public static DecimalFormat NUMBER_FORMATTING = new DecimalFormat("0.000");

    /**
	 * The headers
	 */
    private String[] mColumnHeaders = null;

    /**
	 * The data
	 */
    private final List<String[]> mRows;

    /**
	 * Build from data
	 * 
	 * @param data
	 *            the data in the form of a list of arrays
	 */
    public DataTableModel(final ArrayList<String[]> data) {
        mColumnHeaders = data.get(0);
        mRows = data.subList(1, data.size());
    }

    /**
	 * Constructor
	 * 
	 * @param table
	 *            The table of data
	 */
    public DataTableModel(final DisplayableAsATable table) {
        mColumnHeaders = table.getHeaders();
        mRows = table.getData();
    }

    /**
	 * @see javax.swing.table.TableModel#addTableModelListener(javax.swing.event.TableModelListener)
	 */
    @Override
    public void addTableModelListener(final TableModelListener l) {
    }

    /**
	 * @see javax.swing.table.TableModel#getColumnClass(int)
	 */
    @Override
    public Class<?> getColumnClass(final int columnIndex) {
        return String.class;
    }

    /**
	 * @see javax.swing.table.TableModel#getColumnCount()
	 */
    @Override
    public int getColumnCount() {
        return getColumnHeaders().length;
    }

    /**
	 * @see javax.swing.table.TableModel#getColumnName(int)
	 */
    @Override
    public String getColumnName(final int columnIndex) {
        return getColumnHeaders()[columnIndex];
    }

    /**
	 * @see javax.swing.table.TableModel#getRowCount()
	 */
    @Override
    public int getRowCount() {
        if (mRows != null) {
            return mRows.size();
        }
        return 0;
    }

    /**
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 */
    @Override
    public Object getValueAt(final int rowIndex, final int columnIndex) {
        if (mRows != null) {
            if (columnIndex < mColumnHeaders.length) {
                return mRows.get(rowIndex)[columnIndex];
            }
        }
        return "";
    }

    /**
	 * @see javax.swing.table.TableModel#isCellEditable(int, int)
	 */
    @Override
    public boolean isCellEditable(final int rowIndex, final int columnIndex) {
        return false;
    }

    /**
	 * @see javax.swing.table.TableModel#removeTableModelListener(javax.swing.event.TableModelListener)
	 */
    @Override
    public void removeTableModelListener(final TableModelListener l) {
    }

    /**
	 * @see javax.swing.table.TableModel#setValueAt(java.lang.Object, int, int)
	 */
    @Override
    public void setValueAt(final Object value, final int rowIndex, final int columnIndex) {
        throw new IllegalArgumentException("setValueAt is not implemented");
    }

    /**
	 * @return the columnHeaders
	 */
    private String[] getColumnHeaders() {
        return mColumnHeaders;
    }
}
