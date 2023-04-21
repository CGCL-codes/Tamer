package models;

import java.util.ArrayList;
import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

/**
 *
 * @author Scheinecker Thomas
 */
public class SimpleListModel implements ListModel {

    private ArrayList<String> list;

    private ArrayList<ListDataListener> listeners;

    public SimpleListModel() {
        list = new ArrayList<String>();
        listeners = new ArrayList<ListDataListener>();
    }

    public boolean addElement(String s) {
        if (!list.contains(s)) {
            boolean val = list.add(s);
            fireListDataListeners();
            return val;
        }
        return false;
    }

    public int getSize() {
        return list.size();
    }

    public String getElementAt(int index) {
        return list.get(index);
    }

    public boolean containsElement(String s) {
        return list.contains(s);
    }

    public boolean removeElement(String s) {
        boolean removed = list.remove(s);
        fireListDataListeners();
        return removed;
    }

    public String removeElement(int i) {
        String val = list.remove(i);
        fireListDataListeners();
        return val;
    }

    public String[] removeElements(int[] indices) {
        ArrayList<String> values = new ArrayList<String>();
        for (int i : indices) {
            values.add(removeElement(i));
        }
        fireListDataListeners();
        return values.toArray(new String[] {});
    }

    public void addListDataListener(ListDataListener l) {
        listeners.add(l);
    }

    public void removeListDataListener(ListDataListener l) {
        listeners.remove(l);
    }

    private void fireListDataListenersAdded(int index) {
        for (ListDataListener l : listeners) {
            l.intervalAdded(new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, index, index));
        }
    }

    private void fireListDataListenersRemoved(int index) {
        for (ListDataListener l : listeners) {
            l.intervalRemoved(new ListDataEvent(this, ListDataEvent.INTERVAL_REMOVED, index, index));
        }
    }

    private void fireListDataListeners() {
        for (ListDataListener l : listeners) {
            l.contentsChanged(new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, 0, getSize() - 1));
        }
    }

    public String getListEntries() {
        int size = getSize();
        if (size == 0) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        sb.append(getElementAt(0));
        for (int i = 1; i < size; i++) {
            sb.append(';');
            sb.append(getElementAt(i));
        }
        return sb.toString();
    }
}
