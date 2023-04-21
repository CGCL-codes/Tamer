package gui;

import javax.swing.table.DefaultTableModel;

/**
 *
 * @author oscar
 */
public class MyModel extends DefaultTableModel {

    public boolean isCellEditable(int row, int column) {
        return false;
    }
}
