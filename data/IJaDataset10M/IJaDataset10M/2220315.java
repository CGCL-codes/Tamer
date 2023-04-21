package eu.keep.gui.common;

import javax.swing.table.AbstractTableModel;

public class InfoTableModel extends AbstractTableModel {

    private Object[] colNames;

    private Object[][] data;

    public InfoTableModel(Object[] colNames, Object[][] data) {
        this.colNames = colNames;
        this.data = data;
    }

    @Override
    public String getColumnName(int col) {
        return this.colNames[col].toString();
    }

    @Override
    public int getRowCount() {
        return this.data.length;
    }

    @Override
    public int getColumnCount() {
        return this.colNames.length;
    }

    @Override
    public Object getValueAt(int row, int col) {
        return this.data[row][col];
    }
}
