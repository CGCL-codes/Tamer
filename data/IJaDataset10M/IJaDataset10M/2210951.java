package checker.gui.table;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

/**
 * Data model for the license conflicts
 * to be represented in a JTable
 *  
 * @author Veli-Jussi Raitila
 * 
 */
public class ConflictTableModel extends AbstractTableModel {

    private ArrayList<ConflictTableRow> data;

    private String[] columnNames = { "License 1", "Conflict", "License 2" };

    public ConflictTableModel() {
        data = new ArrayList<ConflictTableRow>();
    }

    public int getColumnCount() {
        return columnNames.length;
    }

    public int getRowCount() {
        return data.size();
    }

    public String getColumnName(int col) {
        return columnNames[col];
    }

    public Object getValueAt(int row, int col) {
        ConflictTableRow r = data.get(row);
        Object value;
        switch(col) {
            case 0:
                value = r.getLicense1();
                break;
            case 1:
                value = r.getConflictType();
                break;
            case 2:
                value = r.getLicense2();
                break;
            default:
                value = null;
                break;
        }
        return value;
    }

    public boolean isCellEditable(int row, int col) {
        return false;
    }

    public void addRow(ConflictTableRow r) {
        data.add(r);
    }
}
