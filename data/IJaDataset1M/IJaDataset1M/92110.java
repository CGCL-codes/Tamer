package eu.ict.persist.RFID.impl;

import java.util.Vector;
import javax.swing.table.AbstractTableModel;

/**
 * @author Elizabeth
 *
 */
public class DataMonitorTable extends AbstractTableModel {

    private Vector<Vector> data;

    private String[] columnNames = { "DPI", "Tag Number", "Wakeup Unit", "Symbolic Location" };

    public DataMonitorTable() {
        super();
        this.data = new Vector<Vector>();
    }

    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public int getRowCount() {
        return data.size();
    }

    public String getColumnName(int col) {
        return columnNames[col];
    }

    public Object getValueAt(int row, int col) {
        Vector rowVector = (Vector) data.get(row);
        return rowVector.get(col);
    }

    public Class getColumnClass(int c) {
        return String.class;
    }

    public boolean isCellEditable(int row, int col) {
        return false;
    }

    public void setValueAt(Object value, int row, int col) {
        Vector rowVector = (Vector) data.get(row);
        rowVector.set(col, value);
        fireTableCellUpdated(row, col);
    }

    public void addRow(Vector row) {
        this.data.add(row);
        for (int i = 0; i < row.size(); i++) {
            System.out.println(row.get(i));
        }
        this.fireTableDataChanged();
    }

    public void removeRow(int row) {
        this.data.remove(row);
        this.fireTableDataChanged();
    }
}
