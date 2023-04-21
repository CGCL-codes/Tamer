package artem.finance.gui.bankexporter;

import javax.swing.table.AbstractTableModel;

/**
 *
 * @author bia
 */
public class PorTableModel extends AbstractTableModel {

    Object[][] data = new Object[200][5];

    String[] titles = new String[] { "����", "�����", "�����", "����������", "�����" };

    boolean[] canEdit = new boolean[] { false, false, false, false, true };

    Class[] types = new Class[] { String.class, String.class, String.class, String.class, Boolean.class };

    @Override
    public Class getColumnClass(int column) {
        if ((column < 5) && (column >= 0)) {
            return types[column];
        }
        return null;
    }

    @Override
    public String getColumnName(int col_number) {
        return titles[col_number];
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        if ((column < 5) & (column >= 0)) {
            return canEdit[column];
        }
        return false;
    }

    public int getRowCount() {
        return data.length;
    }

    public int getColumnCount() {
        return titles.length;
    }

    public Object getValueAt(int row, int column) {
        if ((row < this.getRowCount()) && (row >= 0) && (column < 5) && (column >= 0)) {
            return data[row][column];
        }
        return null;
    }

    @Override
    public void setValueAt(Object value, int row, int column) {
        if ((row < this.getRowCount()) && (row >= 0) && (column < 5)) {
            data[row][column] = value;
        }
    }

    public void setRowCount(int count) {
        data = new Object[count][5];
    }
}
