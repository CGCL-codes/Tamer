package com.groovytagger.ui.frames.model;

import com.groovytagger.utils.StaticObj;
import javax.swing.table.AbstractTableModel;

public class PatternTableModel extends AbstractTableModel {

    public static int[] columnWidth = { 120 };

    private String[] columnNames = { StaticObj.RBUNDLE.getString("track.label.pattern") };

    private Object[][] data = { { " " } };

    public PatternTableModel(Object[][] data) {
        this.data = data;
    }

    public PatternTableModel() {
    }

    public int getColumnCount() {
        return columnNames.length;
    }

    public int getRowCount() {
        return data.length;
    }

    public String getColumnName(int col) {
        return columnNames[col];
    }

    public Object getValueAt(int row, int col) {
        return data[row][col];
    }

    public Class getColumnClass(int columnIndex) {
        return getValueAt(0, columnIndex).getClass();
    }

    public boolean isCellEditable(int row, int col) {
        return true;
    }

    public void setValueAt(Object value, int row, int col) {
        data[row][col] = value;
    }

    public void setData(Object[][] data) {
        this.data = data;
    }

    public Object[][] getData() {
        return data;
    }
}
