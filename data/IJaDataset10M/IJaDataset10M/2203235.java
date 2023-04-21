package eu.ict.persist.RFID.server.impl;

import java.util.Vector;
import javax.swing.table.AbstractTableModel;

/**
 * @author Elizabeth
 *
 */
public class TagRegTable extends AbstractTableModel {

    private Vector<Vector> data;

    private String[] columnNames = { "Tag Number", "DPI", "Password" };

    public TagRegTable() {
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

    public void changePassword(String tagNumber, String password) {
        boolean found = false;
        for (int i = 0; i < data.size(); i++) {
            String tempTag = (String) this.getValueAt(i, 0);
            if (tempTag.equalsIgnoreCase(tagNumber)) {
                this.setValueAt(password, i, 2);
                found = true;
            }
        }
        if (!found) {
            Vector<String> row = new Vector<String>();
            row.add(tagNumber);
            row.add("");
            row.add(password);
            this.addRow(row);
        }
    }

    public void changeDPI(String tagNumber, String dpi) {
        for (int i = 0; i < data.size(); i++) {
            String tempTag = (String) this.getValueAt(i, 0);
            if (tempTag.equalsIgnoreCase(tagNumber)) {
                this.setValueAt(dpi, i, 1);
            }
        }
    }

    public boolean containsTag(String tagNumber) {
        for (int i = 0; i < data.size(); i++) {
            String tempTag = (String) this.getValueAt(i, 0);
            if (tempTag.equalsIgnoreCase(tagNumber)) {
                return true;
            }
        }
        return false;
    }
}
