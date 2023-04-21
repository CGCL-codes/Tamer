package com.avantilearning.ui;

import com.avantilearning.util.Item;
import com.sun.lwuit.events.DataChangedListener;
import com.sun.lwuit.events.SelectionListener;
import com.sun.lwuit.list.ListModel;
import java.util.Vector;

/**
 * Filter proxy for Item list.  Based on the filter criteria, a Vector
 * containing a subset of the underlying list is created.
 * @author Allan
 */
public class ItemListFilterListModel implements ListModel, DataChangedListener {

    private ListModel underlying;

    private Vector filter;

    private Vector listeners = new Vector();

    public ItemListFilterListModel(ListModel underlying) {
        this.underlying = underlying;
        underlying.addDataChangedListener(this);
    }

    private int getFilterOffset(int index) {
        if (filter == null) {
            return index;
        }
        if (filter.size() > index) {
            return ((Integer) filter.elementAt(index)).intValue();
        }
        return -1;
    }

    private int getUnderlyingOffset(int index) {
        if (filter == null) {
            return index;
        }
        return filter.indexOf(new Integer(index));
    }

    /**
    * Generate a subset of the underlying list based on the input string
    * @param str the tag to use as a filter
    */
    public void filter(String str) {
        filter = new Vector();
        str = str.toUpperCase();
        for (int i = 0; i < underlying.getSize(); i++) {
            Item item = (Item) underlying.getItemAt(i);
            if (item.hasTag(str) || str.equals("")) {
                filter.addElement(new Integer(i));
            }
        }
        dataChanged(DataChangedListener.CHANGED, -1);
    }

    public Object getItemAt(int index) {
        return (Item) underlying.getItemAt(getFilterOffset(index));
    }

    public int getSize() {
        if (filter == null) {
            return underlying.getSize();
        }
        return filter.size();
    }

    public int getSelectedIndex() {
        return Math.max(0, getUnderlyingOffset(underlying.getSelectedIndex()));
    }

    public void setSelectedIndex(int index) {
        underlying.setSelectedIndex(getFilterOffset(index));
    }

    public void addDataChangedListener(DataChangedListener l) {
        listeners.addElement(l);
    }

    public void removeDataChangedListener(DataChangedListener l) {
        listeners.removeElement(l);
    }

    public void addSelectionListener(SelectionListener l) {
        underlying.addSelectionListener(l);
    }

    public void removeSelectionListener(SelectionListener l) {
        underlying.removeSelectionListener(l);
    }

    public void addItem(Object item) {
        underlying.addItem(item);
    }

    public void removeItem(int index) {
        underlying.removeItem(index);
    }

    public void dataChanged(int type, int index) {
        if (index > -1) {
            index = getUnderlyingOffset(index);
            if (index < 0) {
                return;
            }
        }
        for (int iter = 0; iter < listeners.size(); iter++) {
            ((DataChangedListener) listeners.elementAt(iter)).dataChanged(type, index);
        }
    }
}
