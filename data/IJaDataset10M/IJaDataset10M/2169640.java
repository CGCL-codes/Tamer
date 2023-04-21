package myFileUtils;

import javax.swing.table.DefaultTableModel;

/**
 *
 * @author lntrung
 */
public class MyTableModel extends DefaultTableModel {

    public boolean bFlag = false;

    public MyTableModel(String[] col) {
        for (int i = 0; i < col.length; i++) {
            this.addColumn(col[i]);
        }
    }

    public void SetFlag(boolean value) {
        bFlag = value;
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        return bFlag;
    }

    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex == 1) {
            return getValueAt(0, columnIndex).getClass();
        } else {
            return super.getColumnClass(columnIndex);
        }
    }
}
