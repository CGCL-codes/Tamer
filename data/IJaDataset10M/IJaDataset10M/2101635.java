package com.groovytagger.ui.frames.model;

import com.groovytagger.utils.StaticObj;
import javax.swing.table.AbstractTableModel;

public class FileTableModel extends AbstractTableModel {

    public static int[] columnWidth = { 160, 100, 100, 35, 120, 40, 60, 200 };

    private String[] columnNames = { "File Name", StaticObj.RBUNDLE.getString("table.header.artist"), StaticObj.RBUNDLE.getString("table.header.album"), StaticObj.RBUNDLE.getString("table.header.track"), StaticObj.RBUNDLE.getString("table.header.title"), StaticObj.RBUNDLE.getString("table.header.year"), StaticObj.RBUNDLE.getString("table.header.gen"), "File Path" };

    private Object[][] data = { { " ", " ", " ", " ", " ", " ", " ", " " } };

    public FileTableModel(Object[][] data, String[] columnNames) {
        this.data = data;
        this.columnNames = columnNames;
    }

    public FileTableModel(Object[][] data) {
        this.data = data;
    }

    public FileTableModel() {
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
        if (col > 0 && col < 7) {
            return true;
        } else {
            return false;
        }
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
