package missionevolver.gui.dialog;

import javax.swing.table.DefaultTableModel;

public class HistoryTable extends DefaultTableModel {

    static final int NUM_COLUMNS = 4;

    String[] _colNames = { "Rev", "Time", "Author", "Comment" };

    public int getColumnCount() {
        return NUM_COLUMNS;
    }

    public String getColumnName(int columnIndex) {
        return _colNames[columnIndex];
    }

    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

    public boolean isCellEditable(int row, int column) {
        return false;
    }
}
