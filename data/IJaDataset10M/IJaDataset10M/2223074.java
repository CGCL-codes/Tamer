package toxTree.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import javax.swing.table.AbstractTableModel;

/**
 * {@link AbstractTableModel} for {@link Hashtable}.
 * @author Nina Jeliazkova
 *
 */
public class HashtableModel extends AbstractTableModel {

    /**
	 * 
	 */
    private static final long serialVersionUID = 7658905911850280047L;

    protected String[] columnNames = { "ID", "SMARTS" };

    protected Hashtable table;

    protected ArrayList keys;

    protected boolean[] enabled;

    boolean includeTranslated = true;

    public HashtableModel(Hashtable table) {
        this(table, true);
    }

    public HashtableModel(Hashtable table, boolean includeTranslated) {
        super();
        keys = new ArrayList();
        enabled = null;
        this.includeTranslated = includeTranslated;
        setTable(table);
    }

    public int getColumnCount() {
        return 2;
    }

    public int getRowCount() {
        return keys.size();
    }

    public Object getValueAt(int row, int col) {
        Object key = keys.get(row);
        if (key == null) return "NA";
        if (includeTranslated) switch(col) {
            case 0:
                return key;
            case 1:
                {
                    Object o = table.get(key);
                    if (o == null) return "NA"; else return o;
                }
            default:
                return "";
        } else switch(col) {
            case 1:
                {
                    Object o = table.get(key);
                    if (o == null) return "NA"; else return o;
                }
            case 0:
                return new Boolean(enabled[row]);
            default:
                return "";
        }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (includeTranslated) switch(columnIndex) {
            case 1:
                {
                    Object key = keys.get(rowIndex);
                    Object o = table.get(key);
                    table.put(key, aValue);
                    break;
                }
            default:
        } else switch(columnIndex) {
            case 0:
                {
                    enabled[rowIndex] = ((Boolean) aValue).booleanValue();
                    break;
                }
        }
        ;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        if (includeTranslated) return columnIndex == 1; else return columnIndex == 0;
    }

    public Hashtable getTable() {
        if (enabled != null) for (int i = 0; i < enabled.length; i++) if (!enabled[i]) {
            table.remove(keys.get(i));
        }
        setTable(table);
        return table;
    }

    public void setTable(Hashtable table) {
        this.table = table;
        keys.clear();
        if (table != null) {
            Enumeration e = table.keys();
            while (e.hasMoreElements()) {
                Object key = e.nextElement();
                if (accept(key)) keys.add(key);
            }
            enabled = new boolean[keys.size()];
            for (int i = 0; i < enabled.length; i++) enabled[i] = true;
        }
        Collections.sort(keys);
        fireTableStructureChanged();
    }

    @Override
    public String getColumnName(int arg0) {
        return columnNames[arg0];
    }

    @Override
    public Class getColumnClass(int columnIndex) {
        if (!includeTranslated && (columnIndex == 0)) return Boolean.class; else return super.getColumnClass(columnIndex);
    }

    protected boolean accept(Object key) {
        return true;
    }
}
