package komiix.swing.table;

import java.awt.Point;
import java.io.File;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author erwinke
 */
public class KomIIxTableModel extends AbstractTableModel {

    private Vector<String> headers = new Vector<String>();

    private Hashtable<Point, Object> lookup = new Hashtable<Point, Object>();

    public static final int INPUT_INDEX = 0;

    public static final int OUTPUT_INDEX = 1;

    public static final int PROFILE_INDEX = 2;

    public static final int DEL_ROW_INDEX = 3;

    public static final int ADD_ROW_INDEX = 4;

    private final JTable table;

    public KomIIxTableModel(JTable table) {
        super();
        this.table = table;
        headers.add("Input");
        headers.add("Output");
        headers.add("Profile");
        headers.add("");
        headers.add("");
    }

    @Override
    public String getColumnName(int column) {
        return headers.get(column);
    }

    @Override
    public int getRowCount() {
        return lookup.size() / getColumnCount() + 1;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }

    @Override
    public int getColumnCount() {
        return headers.size();
    }

    @Override
    public Object getValueAt(int row, int column) {
        Object ret = lookup.get(new Point(row, column));
        switch(column) {
            case INPUT_INDEX:
                if (ret != null) {
                    return ((File) ret).getName();
                }
                break;
            case OUTPUT_INDEX:
                if (ret != null) {
                    return ((File) ret).getName();
                }
                break;
            case PROFILE_INDEX:
                return (String) ret;
            case DEL_ROW_INDEX:
                return "";
            case ADD_ROW_INDEX:
                return "";
            default:
                throw new IllegalArgumentException("Invalid column setting");
        }
        return "";
    }

    @Override
    public void setValueAt(Object value, int row, int column) {
        if ((row < 0) || (column < 0) || (row > getRowCount())) {
            throw new IllegalArgumentException("Invalid row/column setting");
        }
        switch(column) {
            case INPUT_INDEX:
                if (value != null) {
                    lookup.put(new Point(row, column), new File(value.toString()));
                    lookup.put(new Point(row, OUTPUT_INDEX), new File(value.toString()).getParentFile());
                    fireTableDataChanged();
                }
                break;
            case OUTPUT_INDEX:
                if (value != null) {
                    lookup.put(new Point(row, column), new File(value.toString()));
                }
                break;
            case PROFILE_INDEX:
                if (value != null) {
                    lookup.put(new Point(row, column), value);
                }
                break;
            case DEL_ROW_INDEX:
                break;
            case ADD_ROW_INDEX:
                break;
            default:
                throw new IllegalArgumentException("Invalid column setting");
        }
    }

    public void addRow() {
        int r = getRowCount() + 1;
        lookup.put(new Point(r, 0), new File(""));
        lookup.put(new Point(r, 1), new File(""));
        lookup.put(new Point(r, 2), "");
        lookup.put(new Point(r, 3), "");
        lookup.put(new Point(r, 4), "");
        fireTableDataChanged();
    }
}
