package latex_tab;

import javax.swing.table.DefaultTableModel;

public class RowHeadersTableModel extends DefaultTableModel {

    public RowHeadersTableModel(int rowCount, int columnCount) {
        super(rowCount, columnCount);
    }

    public boolean isCellEditable(int row, int column) {
        return false;
    }

    public Object getValueAt(int row, int column) {
        return row + 1;
    }
}
