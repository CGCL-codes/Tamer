package gui;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 * 
 * @author JPD
 * A simple class that simplifies the GUI code in XMLBans
 *
 */
@SuppressWarnings("serial")
public class OnlineListTable extends JTable {

    public OnlineListTable(String[][] listData, String[] columnNames) {
        DefaultTableModel tableModel = new DefaultTableModel(listData, columnNames);
        this.setModel(tableModel);
        this.setAutoCreateRowSorter(true);
    }

    public boolean isCellEditable(int row, int column) {
        return false;
    }
}
