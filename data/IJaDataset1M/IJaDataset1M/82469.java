package net.spike.mdb.gui;

import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 * @author Arthur
 */
public class KillTableModel extends AbstractTableModel {

    private final List<KillEntry> data;

    /**
	 * @param list
	 */
    public KillTableModel(List<KillEntry> list) {
        super();
        this.data = list;
    }

    public int getRowCount() {
        return data.size();
    }

    public int getColumnCount() {
        return 2;
    }

    public Object getValueAt(int row, int column) {
        if (column == 0) return data.get(row).isChecked(); else return data.get(row).getSubject();
    }

    public boolean isCellEditable(int row, int column) {
        return column == 0;
    }

    public void setValueAt(Object value, int row, int column) {
        if (column == 0) data.get(row).setChecked((Boolean) value); else data.get(row).setSubject((String) value);
    }
}
