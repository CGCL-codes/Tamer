package org.jcrpg.threed.jme.geometryinstancing;

import java.util.ArrayList;

public class QuickOrderedListLong {

    private ArrayList<Long> listOrdering = new ArrayList<Long>();

    public ArrayList<Object> list = new ArrayList<Object>();

    public static long timeCounter = 0;

    public int countRec = 0;

    public int getNearestIndex(long orderingValue) {
        countRec = 0;
        return getNearestIndexPriv(orderingValue, 0, listOrdering.size() - 1);
    }

    private int getNearestIndexPriv(long orderingValue, int from, int to) {
        countRec++;
        if (countRec > 1000) System.out.println("-- " + orderingValue + " " + from + " - " + to);
        if (from == to) return from;
        int center = (from + to) / 2;
        long val = listOrdering.get(center);
        if (val == orderingValue) return center;
        if (val < orderingValue) {
            if (from + 1 == to) {
                return to;
            }
            return getNearestIndexPriv(orderingValue, center, to);
        }
        if (from + 1 == to) {
            return from;
        }
        return getNearestIndexPriv(orderingValue, from, center);
    }

    public void addElement(long orderingValue, Object value) {
        long time = System.currentTimeMillis();
        int index = 0;
        if (list.size() == 0) {
        } else {
            index = getNearestIndex(orderingValue);
        }
        listOrdering.add(index, orderingValue);
        list.add(index, value);
        timeCounter += System.currentTimeMillis() - time;
    }

    public void removeElement(Object value) {
        int index = 0;
        if (list.size() == 0) {
        } else {
            index = list.indexOf(value);
            listOrdering.remove(index);
            list.remove(index);
        }
    }

    public Object removeElementWithEqualOrBiggerOrderingValue(long orderingValue) {
        long time = System.currentTimeMillis();
        int index = 0;
        if (list.size() == 0) {
            timeCounter += System.currentTimeMillis() - time;
            return null;
        } else {
            index = getNearestIndex(orderingValue);
        }
        long order = listOrdering.get(index);
        if (order >= orderingValue) {
            listOrdering.remove(index);
            timeCounter += System.currentTimeMillis() - time;
            return list.remove(index);
        }
        timeCounter += System.currentTimeMillis() - time;
        return null;
    }

    public Object removeElementWithEqualOrderingValue(long orderingValue) {
        long time = System.currentTimeMillis();
        int index = 0;
        if (list.size() == 0) {
            timeCounter += System.currentTimeMillis() - time;
            return null;
        } else {
            index = getNearestIndex(orderingValue);
        }
        long order = listOrdering.get(index);
        if (order == orderingValue) {
            listOrdering.remove(index);
            timeCounter += System.currentTimeMillis() - time;
            return list.remove(index);
        }
        timeCounter += System.currentTimeMillis() - time;
        return null;
    }

    public Object getElementWithEqualOrderingValue(long orderingValue) {
        long time = System.currentTimeMillis();
        int index = 0;
        if (list.size() == 0) {
            timeCounter += System.currentTimeMillis() - time;
            return null;
        } else {
            index = getNearestIndex(orderingValue);
        }
        long order = listOrdering.get(index);
        if (order == orderingValue) {
            timeCounter += System.currentTimeMillis() - time;
            return list.get(index);
        }
        timeCounter += System.currentTimeMillis() - time;
        return null;
    }

    public void clear() {
        listOrdering.clear();
        list.clear();
    }
}
