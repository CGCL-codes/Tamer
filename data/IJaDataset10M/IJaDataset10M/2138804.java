package com.loribel.commons.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import com.loribel.commons.abstraction.GB_HomologueFilter;
import com.loribel.commons.abstraction.GB_IndexOwner;
import com.loribel.commons.abstraction.GB_ObjectFilter;

/**
 * Tools for GB_IndexOwner.
 *
 * @author Gregory Borelli
 */
public final class GB_IndexOwnerTools {

    /**
     * Inner class Comparator
     */
    public static class MyComparatorByIndex implements Comparator {

        public MyComparatorByIndex() {
            super();
        }

        public int compare(Object a_item1, Object a_item2) {
            if (a_item1 == null) {
                if (a_item2 == null) {
                    return 0;
                }
                return 1;
            }
            if (a_item2 == null) {
                return -1;
            }
            int l_index1 = ((GB_IndexOwner) a_item1).getIndex();
            int l_index2 = ((GB_IndexOwner) a_item2).getIndex();
            return (l_index1 - l_index2);
        }
    }

    /**
     * Inner class Filter.
     */
    public static class MyFilterByIndex implements GB_ObjectFilter {

        private int index;

        public MyFilterByIndex(int a_index) {
            index = a_index;
        }

        public boolean accept(Object a_object) {
            if (a_object == null) {
                return false;
            }
            int l_index = ((GB_IndexOwner) a_object).getIndex();
            return index == l_index;
        }
    }

    /**
     * Inner class Filter.
     */
    public static class MyHomologueByIndex implements GB_HomologueFilter {

        public MyHomologueByIndex() {
        }

        public boolean isHomologue(Object a_value1, Object a_value2) {
            return isEqualsByIndex(a_value1, a_value2);
        }
    }

    public static Comparator COMPARATOR = new MyComparatorByIndex();

    /**
     * Compare two lists of GB_IndexOwner elements returning elements in a_list2
     * that are not in a_list1.
     * 
     * @param a_list1 a list of GB_IndexOwner 
     * @param a_list2 a list of GB_IndexOwner
     */
    public static GB_IndexOwner[] additionDelta(Collection a_list1, Collection a_list2) {
        List retour = new ArrayList();
        for (Iterator it = a_list2.iterator(); it.hasNext(); ) {
            GB_IndexOwner l_item = (GB_IndexOwner) it.next();
            int l_index = l_item.getIndex();
            if (firstWithIndex(a_list1, l_index) == null) {
                retour.add(l_item);
            }
        }
        return (GB_IndexOwner[]) retour.toArray(new GB_IndexOwner[retour.size()]);
    }

    /**
     * Compare two lists of GB_IndexOwner elements returning elements in a_list2
     * that are also in a_list1.
     * 
     * @param a_list1 a list of GB_IndexOwner 
     * @param a_list2 a list of GB_IndexOwner
     */
    public static GB_IndexOwner[] commonDelta(Collection a_list1, Collection a_list2) {
        List retour = new ArrayList();
        for (Iterator it = a_list1.iterator(); it.hasNext(); ) {
            GB_IndexOwner l_item = (GB_IndexOwner) it.next();
            int l_index = l_item.getIndex();
            if (firstWithIndex(a_list2, l_index) != null) {
                retour.add(l_item);
            }
        }
        return (GB_IndexOwner[]) retour.toArray(new GB_IndexOwner[retour.size()]);
    }

    /**
     * Method to filter a list of items by index
     */
    public static void filterByIndex(Collection a_items, int a_index) {
        if (a_items == null) {
            return;
        }
        Iterator it = a_items.iterator();
        while (it.hasNext()) {
            GB_IndexOwner l_item = (GB_IndexOwner) it.next();
            if (!isIndexOwner(l_item, a_index)) {
                it.remove();
            }
        }
    }

    /**
     * Returns first item with index = a_index.
     * Returns null if a_index not found.
     * @param a_items a collection of GB_IndexOwner
     */
    public static GB_IndexOwner firstWithIndex(Collection a_items, int a_index) {
        if (a_items == null) {
            return null;
        }
        for (Iterator it = a_items.iterator(); it.hasNext(); ) {
            GB_IndexOwner l_item = (GB_IndexOwner) it.next();
            if (isIndexOwner(l_item, a_index)) {
                return l_item;
            }
        }
        return null;
    }

    /**
     * Returns first item with index = a_index.
     * Returns null if a_index not found.
     * @param a_items an array of GB_IndexOwner
     */
    public static GB_IndexOwner firstWithIndex(Object[] a_items, int a_index) {
        int len = CTools.getSize(a_items);
        for (int i = 0; i < len; i++) {
            GB_IndexOwner l_item = (GB_IndexOwner) a_items[i];
            if (isIndexOwner(l_item, a_index)) {
                return l_item;
            }
        }
        return null;
    }

    /**
     * Method to filter a list of items by index.
     */
    public static List getFilterByIndex(Collection a_items, int a_index) {
        if (a_items == null) {
            return null;
        }
        List retour = new ArrayList();
        GB_IndexOwner o = null;
        Iterator it = a_items.iterator();
        while (it.hasNext()) {
            o = (GB_IndexOwner) it.next();
            if (isIndexOwner(o, a_index)) {
                retour.add(o);
            }
        }
        if (retour.size() == 0) {
            return null;
        }
        return retour;
    }

    /**
     * Method to filter an array of items by Index.
     */
    public static Object[] getFilterByIndex(Object[] a_items, int a_index, Object[] a_returnArray) {
        if (a_items == null) {
            return a_returnArray;
        }
        List retour = new ArrayList();
        GB_IndexOwner l_item;
        int len = CTools.getSize(a_items);
        for (int i = 0; i < len; i++) {
            l_item = (GB_IndexOwner) a_items[i];
            if (isIndexOwner(l_item, a_index)) {
                retour.add(l_item);
            }
        }
        if (retour.size() == 0) {
            return a_returnArray;
        }
        return retour.toArray(a_returnArray);
    }

    /**
     * Returns the indexs of the items.
     */
    public static int[] getIndexs(Collection a_items) {
        if (a_items == null) {
            return new int[0];
        }
        int len = CTools.getSize(a_items);
        int i = 0;
        int[] retour = new int[len];
        for (Iterator it = a_items.iterator(); it.hasNext(); ) {
            GB_IndexOwner l_item = (GB_IndexOwner) it.next();
            if (l_item != null) {
                retour[i] = l_item.getIndex();
            }
            i++;
        }
        return retour;
    }

    /**
     * Returns the indexs of the items.
     */
    public static int[] getIndexs(Object[] a_items) {
        if (a_items == null) {
            return new int[0];
        }
        int len = CTools.getSize(a_items);
        int[] retour = new int[len];
        for (int i = 0; i < len; i++) {
            GB_IndexOwner l_item = (GB_IndexOwner) a_items[i];
            if (l_item != null) {
                retour[i] = l_item.getIndex();
            }
        }
        return retour;
    }

    /**
     * Returns the index of item a_item.
     */
    public static boolean isEqualsByIndex(Object a_item1, Object a_item2) {
        if ((a_item1 == null) || (a_item2 == null)) {
            return (a_item1 == a_item2);
        }
        int l_value1 = ((GB_IndexOwner) a_item1).getIndex();
        int l_value2 = ((GB_IndexOwner) a_item2).getIndex();
        return (l_value1 == l_value2);
    }

    /**
     * Returns true if a_item has index a_index.  
     */
    public static boolean isIndexOwner(GB_IndexOwner a_item, int a_index) {
        if (a_item == null) {
            return false;
        }
        int l_index = a_item.getIndex();
        return (a_index == l_index);
    }

    /**
     * Adds an item to a map indexed by index.
     */
    public static void putByIndex(Map a_map, GB_IndexOwner a_item) {
        if (a_item == null) {
            return;
        }
        a_map.put(new Long(a_item.getIndex()), a_item);
    }

    /**
     * Adds items to a map indexed by index.
     */
    public static void putByIndexs(Map a_map, Collection a_items) {
        if (a_items == null) {
            return;
        }
        for (Iterator it = a_items.iterator(); it.hasNext(); ) {
            GB_IndexOwner l_item = (GB_IndexOwner) it.next();
            putByIndex(a_map, l_item);
        }
    }

    /**
     * Adds items to a map indexed by index.
     */
    public static void putByIndexs(Map a_map, Object[] a_items) {
        if (a_items == null) {
            return;
        }
        int len = CTools.getSize(a_items);
        for (int i = 0; i < len; i++) {
            GB_IndexOwner l_item = (GB_IndexOwner) a_items[i];
            putByIndex(a_map, l_item);
        }
    }

    /**
     * Sorts a list of GB_IndexOwner.
     * 
     * @param a_items
     */
    public static void sortByIndex(List a_items) {
        Collections.sort(a_items, COMPARATOR);
    }

    /**
     * Sorts an array of GB_IndexOwner.
     * 
     * @param a_items
     */
    public static void sortByIndex(Object[] a_items) {
        Arrays.sort(a_items, COMPARATOR);
    }

    /**
     * Compare two lists of GB_IndexOwner elements returning elements in a_list1
     * that are not in a_list2.
     * 
     * @param a_list1 a list of GB_IndexOwner 
     * @param a_list2 a list of GB_IndexOwner
     */
    public static GB_IndexOwner[] substractionDelta(Collection a_list1, Collection a_list2) {
        return additionDelta(a_list2, a_list1);
    }
}
