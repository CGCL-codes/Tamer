package com.beanview.echo.model;

import java.util.HashSet;
import nextapp.echo2.app.event.ChangeListener;
import nextapp.echo2.app.event.ListDataListener;
import nextapp.echo2.app.list.ListModel;
import nextapp.echo2.app.list.ListSelectionModel;
import com.beanview.model.ArrayModel;

public class EchoArrayModel extends ArrayModel implements ListModel, ListSelectionModel {

    private static final long serialVersionUID = 1L;

    public EchoArrayModel(Object[] in, Object selected, boolean nullable) {
        super(in, selected, nullable);
    }

    HashSet<ListDataListener> listeners = new HashSet<ListDataListener>();

    HashSet<ChangeListener> change_listeners = new HashSet<ChangeListener>();

    public void addListDataListener(ListDataListener arg0) {
        listeners.add(arg0);
    }

    public void removeListDataListener(ListDataListener arg0) {
        listeners.remove(arg0);
    }

    public Object get(int arg0) {
        return this.getElementAt(arg0);
    }

    public int size() {
        return this.getSize();
    }

    public void addChangeListener(ChangeListener arg0) {
        change_listeners.add(arg0);
    }

    public void removeChangeListener(ChangeListener arg0) {
        change_listeners.remove(arg0);
    }

    public void clearSelection() {
        this.setSelectedItem(null);
        selectedIndices.clear();
    }

    public void setSelectedItem(Object arg) {
        super.setSelectedItem(arg);
        selectedIndices.clear();
        for (int i = 0; i < this.getSize(); i++) {
            if (arg == this.get(i)) selectedIndices.add(i);
        }
    }

    HashSet<Integer> selectedIndices = new HashSet<Integer>();

    public int getMaxSelectedIndex() {
        int max = -1;
        for (Integer x : selectedIndices) {
            if (x > max) max = x;
        }
        return max;
    }

    public int getMinSelectedIndex() {
        int min = Integer.MAX_VALUE;
        for (Integer x : selectedIndices) {
            if (x < min) min = x;
        }
        return min;
    }

    int selectionMode = ListSelectionModel.SINGLE_SELECTION;

    public int getSelectionMode() {
        return selectionMode;
    }

    public boolean isSelectedIndex(int arg0) {
        return selectedIndices.contains(arg0);
    }

    public boolean isSelectionEmpty() {
        return selectedIndices.isEmpty();
    }

    public void setSelectedIndex(int index, boolean selected) {
        if (selected) selectedIndices.add(index); else selectedIndices.remove(index);
    }

    public void setSelectionMode(int arg0) {
        selectionMode = arg0;
    }
}
