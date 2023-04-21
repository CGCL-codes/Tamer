package jp.jparc.apps.iTuning2;

import javax.swing.table.*;

/** class to handle the display kinetic energy calculation variable info table */
public class kValidTableModel extends AbstractTableModel {

    private String[] columnNames;

    private Object[][] data;

    ;

    public kValidTableModel() {
    }

    public kValidTableModel(String[] columnNames, Object[][] data) {
        this.columnNames = columnNames;
        this.data = data;
    }

    public int getColumnCount() {
        return columnNames.length;
    }

    public int getRowCount() {
        return data.length;
    }

    @Override
    public String getColumnName(int col) {
        return columnNames[col];
    }

    public Object getValueAt(int row, int col) {
        return data[row][col];
    }

    @Override
    public Class getColumnClass(int c) {
        Class colClass = null;
        if (c == 0) colClass = String.class; else colClass = Boolean.class;
        return colClass;
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        if (col == 0) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void setValueAt(Object value, int row, int col) {
        data[row][col] = value;
        fireTableCellUpdated(row, col);
    }
}
