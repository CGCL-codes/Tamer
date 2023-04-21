package frost.gui.model;

import javax.swing.table.*;

public class AttachmentTableModel extends DefaultTableModel {

    public AttachmentTableModel() {
        super();
    }

    public boolean isCellEditable(int row, int col) {
        return false;
    }
}
