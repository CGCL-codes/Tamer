package net.maizegenetics.stats.GLM;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: PeterLocal
 * Date: Sep 22, 2006
 * Time: 10:11:53 AM
 * To change this template use File | Settings | File Templates.
 */
public class FTestTableModel extends AbstractTableModel {

    String[] columnNames = { "hypothesis", "error", "# permutations" };

    ArrayList data = new ArrayList();

    public int getRowCount() {
        return data.size();
    }

    public int getColumnCount() {
        return 3;
    }

    public Object getValueAt(int i, int i1) {
        return ((Object[]) data.get(i))[i1];
    }

    public String getColumnName(int i) {
        if (i < 3) return columnNames[i];
        return "";
    }

    public boolean isCellEditable(int i, int i1) {
        if ((i1 == 2) && this.getValueAt(i, 1).toString().equals("Residual")) return true;
        return false;
    }

    public void setValueAt(Object o, int i, int i1) {
        ((Object[]) data.get(i))[i1] = o;
        fireTableCellUpdated(i, i1);
    }

    void addRow(Object hypothesis, Object error, Object permutations) {
        data.add(new Object[] { hypothesis, error, permutations });
        int nrow = data.size() - 1;
        fireTableRowsInserted(0, nrow);
    }

    void removeRow(int row) {
        if (row >= 0 & row < data.size()) {
            data.remove(row);
            fireTableRowsDeleted(row, row);
        }
    }
}
