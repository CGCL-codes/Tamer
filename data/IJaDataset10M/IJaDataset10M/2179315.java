package ebiCRM.table.models;

import ebiNeutrinoSDK.EBIPGFactory;
import ebiNeutrinoSDK.utils.EBIConstant;
import javax.swing.table.AbstractTableModel;

public class MyTableModelActivityTask extends AbstractTableModel {

    public String[] columnNames = { EBIPGFactory.getLANG("EBI_LANG_CLOSED"), EBIPGFactory.getLANG("EBI_LANG_NAME"), EBIPGFactory.getLANG("EBI_LANG_ADDED"), EBIPGFactory.getLANG("EBI_LANG_ADDED_FROM") };

    public Object[][] data = { { EBIConstant.ICON_SUCCESS, EBIPGFactory.getLANG("EBI_LANG_PLEASE_SELECT"), "", "" } };

    public Object[] getRow(int row) {
        if (row < 0 || row > data.length - 1) {
            return null;
        }
        return data[row];
    }

    public void setRow(int row, Object[] rowData) {
        if (row < 0 || row > data.length - 1) {
            return;
        }
        data[row] = rowData;
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

    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

    public boolean isCellEditable(int row, int col) {
        return false;
    }

    public void setValueAt(Object value, int row, int col) {
        if (data[0][col] instanceof Integer && !(value instanceof Integer)) {
            try {
                data[row][col] = new Integer(value.toString());
                fireTableCellUpdated(row, col);
            } catch (NumberFormatException e) {
            }
        } else {
            data[row][col] = value;
            fireTableCellUpdated(row, col);
        }
    }
}
