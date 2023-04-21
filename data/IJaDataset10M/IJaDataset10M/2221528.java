package sc123ide;

import java.util.EventListener;
import java.util.EventObject;
import java.util.TreeMap;
import javax.swing.event.EventListenerList;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

public class SymbolTable implements TableModel {

    private static final long serialVersionUID = 1954522589883082667L;

    public class SymbolTableEvent extends EventObject {

        private static final long serialVersionUID = 2198558044518961532L;

        public static final int ADD = 0;

        public static final int MODIFY = 1;

        public static final int REMOVE = 2;

        private int type;

        private String key;

        public SymbolTableEvent(SymbolTable src, int t, String k) {
            super(src);
            type = t;
            key = k;
        }

        public int getType() {
            return type;
        }

        public String getKey() {
            return key;
        }
    }

    public interface SymbolTableListener extends EventListener {

        public void symbolTableChanged(SymbolTableEvent eo);
    }

    private TreeMap<String, Integer> symbolMap = new TreeMap<String, Integer>();

    private EventListenerList listenerList = new EventListenerList();

    public Integer put(String k, Integer v) {
        boolean contains = symbolMap.containsKey(k);
        Integer ans = symbolMap.put(k, v);
        int insPoint = symbolMap.headMap(k).size();
        if (contains) {
            if (v != ans) {
                fireSymbolTableChanged(new SymbolTableEvent(this, SymbolTableEvent.MODIFY, k));
                fireTableModelChanged(new TableModelEvent(this, insPoint, insPoint, TableModelEvent.ALL_COLUMNS, TableModelEvent.UPDATE));
            }
        } else {
            fireSymbolTableChanged(new SymbolTableEvent(this, SymbolTableEvent.ADD, k));
            fireTableModelChanged(new TableModelEvent(this, insPoint, insPoint, TableModelEvent.ALL_COLUMNS, TableModelEvent.INSERT));
        }
        return ans;
    }

    public int get(String k) {
        Integer v = symbolMap.get(k);
        if (v == null) return -1;
        return v;
    }

    public Integer remove(String k) {
        int remPoint = symbolMap.headMap(k).size();
        Integer ans = symbolMap.remove(k);
        fireSymbolTableChanged(new SymbolTableEvent(this, SymbolTableEvent.REMOVE, k));
        fireTableModelChanged(new TableModelEvent(this, remPoint, remPoint, TableModelEvent.ALL_COLUMNS, TableModelEvent.DELETE));
        return ans;
    }

    public String[] keysArray() {
        return symbolMap.navigableKeySet().toArray(new String[symbolMap.size()]);
    }

    public int size() {
        return symbolMap.size();
    }

    public void addTableModelListener(TableModelListener l) {
        listenerList.add(TableModelListener.class, l);
    }

    public void addSymbolTableListener(SymbolTableListener l) {
        listenerList.add(SymbolTableListener.class, l);
    }

    public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }

    public int getColumnCount() {
        return 4;
    }

    public String getColumnName(int columnIndex) {
        switch(columnIndex) {
            case 0:
                return "Symbol Table";
            case 1:
                return "Symbol";
            case 2:
                return "Value";
            case 3:
                return "";
            default:
                assert (false);
                return "";
        }
    }

    public int getRowCount() {
        return symbolMap.size();
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        if ((columnIndex == 1) || (columnIndex == 2)) {
            String[] keys = symbolMap.navigableKeySet().toArray(new String[symbolMap.size()]);
            if (columnIndex == 1) return keys[rowIndex]; else return String.format("0x%04X", symbolMap.get(keys[rowIndex]));
        }
        return "";
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    public void removeTableModelListener(TableModelListener l) {
        listenerList.remove(TableModelListener.class, l);
    }

    public void removeSymbolTableListenser(SymbolTableListener l) {
        listenerList.remove(SymbolTableListener.class, l);
    }

    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        assert (false);
    }

    private void fireTableModelChanged(TableModelEvent tme) {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == TableModelListener.class) {
                ((TableModelListener) listeners[i + 1]).tableChanged(tme);
            }
        }
    }

    private void fireSymbolTableChanged(SymbolTableEvent tme) {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == SymbolTableListener.class) {
                ((SymbolTableListener) listeners[i + 1]).symbolTableChanged(tme);
            }
        }
    }
}
