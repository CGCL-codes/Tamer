package gov.sns.apps.wireanalysis;

import javax.swing.table.AbstractTableModel;
import javax.swing.*;
import java.util.*;

public class DataTableModel extends AbstractTableModel {

    /**  the PV handle names */
    private String[] columnNames;

    /** number of devices in the seqence + header + trailer for plot buttons */
    private int nRows;

    /** number of table columns */
    private int nColumns;

    private ArrayList dataArray;

    /** Container for the JButtons */
    private ArrayList jButtons;

    /** Container for row labels */
    private ArrayList rowNames;

    /** Container for BPM agents **/
    private ArrayList agents;

    /** constructor */
    public DataTableModel(String[] colNames, int numRows) {
        columnNames = colNames;
        nRows = numRows;
        nColumns = colNames.length;
        dataArray = new ArrayList();
        rowNames = new ArrayList(nRows);
        jButtons = new ArrayList(nRows);
        agents = new ArrayList(nRows);
    }

    public void addJButton(int rowNumber, JButton button) {
        jButtons.add(rowNumber, button);
    }

    /** method to add a row name */
    public void addRowName(String name, int row) {
        rowNames.add(row, name);
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        if (col < 1) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public String getColumnName(int col) {
        return columnNames[col];
    }

    public int getRowCount() {
        return dataArray.size();
    }

    public int getColumnCount() {
        return nColumns;
    }

    public Object getValueAt(int row, int col) {
        return ((ArrayList) dataArray.get(row)).get(col);
    }

    @Override
    public void setValueAt(Object value, int row, int col) {
        ArrayList data = (ArrayList) dataArray.get(row);
        data.set(col, value);
    }

    public void setTableData(int rows, ArrayList data, ArrayList agentlist) {
        nRows = rows;
        dataArray = data;
        fireTableDataChanged();
    }

    public void addTableData(ArrayList data) {
        dataArray.add(data);
    }

    public void updateCell(int row, int col) {
        fireTableCellUpdated(row, col);
    }

    @Override
    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

    public void clearAllData() {
        dataArray.clear();
        fireTableDataChanged();
    }
}
