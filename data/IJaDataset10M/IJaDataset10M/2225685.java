package reader;

import java.util.Vector;
import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
public class MirrorTableModel extends AbstractTableModel {

    private String[] cols = new String[1];

    private Vector<Vector<String>> data;

    public MirrorTableModel(Vector<Vector<String>> d) {
        super();
        cols[0] = Main.res.getString("tableheader");
        data = d;
    }

    public int getColumnCount() {
        return 1;
    }

    public int getRowCount() {
        return data.size();
    }

    @Override
    public String getColumnName(final int col) {
        return cols[col];
    }

    public Object getValueAt(final int row, final int col) {
        return data.get(row).get(0);
    }

    @Override
    public Class<?> getColumnClass(final int col) {
        return getValueAt(0, col).getClass();
    }

    @Override
    public boolean isCellEditable(final int row, final int col) {
        return false;
    }

    @Override
    public void setValueAt(final Object value, final int row, final int col) {
        data.get(row).set(0, (String) value);
        fireTableCellUpdated(row, 0);
    }

    public Vector<Vector<String>> getData() {
        return data;
    }

    public void setData(final Vector<Vector<String>> newData) {
        data.clear();
        for (Vector<String> i : newData) data.add(i);
        fireTableDataChanged();
    }
}
