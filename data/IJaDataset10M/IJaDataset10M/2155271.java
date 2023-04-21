package extensions;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Raed Shomali
 */
public class COINCOMOFixedTable extends JTable {

    int editableColumnIndex = 0;

    public COINCOMOFixedTable(DefaultTableModel model) {
        super(model);
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        if (col != editableColumnIndex) {
            return false;
        } else {
            return true;
        }
    }
}
