package org.plazmaforge.framework.client.swing.gui;

import javax.swing.*;

/**
 * @author Oleh Hapon
 * Date: 18.01.2004
 * Time: 14:32:32
 * $Id: SortedListModel.java,v 1.2 2010/04/28 06:36:10 ohapon Exp $
 */
public class SortedListModel extends DefaultListModel {

    public SortedListModel() {
        super();
    }

    public void add(int index, Object obj) {
        addElement(obj);
    }

    public void insertElementAt(int index, Object obj) {
        addElement(obj);
    }

    public void addElement(Object obj) {
        super.add(getIndexInList(obj), obj);
    }

    public Object remove(int index) {
        Object obj = get(index);
        removeElement(obj);
        return obj;
    }

    public void removeElementAt(int index) {
        removeElement(get(index));
    }

    public void removeRange(int fromIndex, int toIndex) {
        for (int i = fromIndex; i <= toIndex; ++i) {
            remove(i);
        }
    }

    protected int getIndexInList(Object obj) {
        final int limit = getSize();
        final String objStr = obj.toString();
        for (int i = 0; i < limit; ++i) {
            if (objStr.compareToIgnoreCase(get(i).toString()) <= 0) {
                return i;
            }
        }
        return limit;
    }

    public void fireDataChanged() {
        fireContentsChanged(this, 0, getSize());
    }

    public void fireDataAdded() {
        int size = getSize();
        fireIntervalAdded(this, 0, size);
    }

    public void fireDataRemoved() {
        fireIntervalRemoved(this, 0, getSize());
    }
}
