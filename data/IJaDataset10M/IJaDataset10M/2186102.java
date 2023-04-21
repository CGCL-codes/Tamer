package gui.main;

import javax.swing.table.AbstractTableModel;
import java.util.LinkedList;
import java.util.List;

/**
 * todo write javadoc
 */
public final class DHTStatisticsTableModel extends AbstractTableModel {

    private static String[] columnIdentifiers = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24" };

    private List<int[]> data = new LinkedList<int[]>();

    public int getRowCount() {
        return data.size();
    }

    public int getColumnCount() {
        return columnIdentifiers.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnIdentifiers[column];
    }

    @Override
    public Class<?> getColumnClass(int column) {
        if (column < 24) {
            return Integer.class;
        } else {
            throw new AssertionError(column);
        }
    }

    public Object getValueAt(int row, int column) {
        if (column < 24) {
            return data.get(row)[column];
        } else {
            throw new AssertionError(column);
        }
    }

    public void addRow(int[] row) {
        data.add(row);
        fireTableRowsInserted(data.size() - 1, data.size() - 1);
    }
}
