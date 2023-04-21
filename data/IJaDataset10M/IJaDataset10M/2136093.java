package com.loribel.commons.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import com.loribel.commons.abstraction.GB_CodeOwner;
import com.loribel.commons.abstraction.GB_HomologueFilter;
import com.loribel.commons.abstraction.GB_ObjectFilter;
import com.loribel.commons.util.comparator.GB_ComparatorTools;
import com.loribel.commons.util.comparator.GB_StringComparatorAbstract;

/**
 * Tools for CodeOwner.
 * 
 * Attention, cette classe est une copie de GB_IdOwnerTools.
 * Ne pas ajouter de m�thode � cette classe.
 * Le faire dans GB_IdOwnerTools et ensuite dupliquer en changeant id par code.
 *
 * @author Gregory Borelli
 */
public final class GB_CodeOwnerTools {

    /**
     * Inner class Comparator
     */
    public static class MyComparatorByCode extends GB_StringComparatorAbstract {

        public MyComparatorByCode() {
            super();
        }

        public MyComparatorByCode(boolean a_flagAscending, boolean a_flagIgnoreCase) {
            super(a_flagAscending, a_flagIgnoreCase);
        }

        public String getStringValue(Object a_item) {
            if (a_item == null) {
                return null;
            }
            return ((GB_CodeOwner) a_item).getCode();
        }
    }

    /**
     * Inner class Filter.
     */
    public static class MyFilterByCode implements GB_ObjectFilter {

        private String code;

        public MyFilterByCode(String a_code) {
            code = a_code;
        }

        public boolean accept(Object a_object) {
            if (a_object == null) {
                return false;
            }
            String l_id = ((GB_CodeOwner) a_object).getCode();
            return GB_EqualsTools.equalsObject(l_id, code);
        }
    }

    /**
     * Inner class Filter.
     */
    public static class MyFilterByCodes implements GB_ObjectFilter {

        private Collection ids;

        public MyFilterByCodes(Collection a_ids) {
            ids = a_ids;
        }

        public MyFilterByCodes(String[] a_ids) {
            ids = CTools.toList(a_ids);
        }

        public boolean accept(Object a_object) {
            if (a_object == null) {
                return false;
            }
            String l_id = ((GB_CodeOwner) a_object).getCode();
            return (ids.contains(l_id));
        }
    }

    /**
     * Inner class Filter.
     */
    public static class MyHomologueByCode implements GB_HomologueFilter {

        public MyHomologueByCode() {
        }

        public boolean isHomologue(Object a_value1, Object a_value2) {
            return isEqualsByCode(a_value1, a_value2);
        }
    }

    /**
     * Compare two lists of GB_CodeOwner elements returning elements in a_list2
     * that are not in a_list1.
     * 
     * @param a_list1 a list of GB_CodeOwner 
     * @param a_list2 a list of GB_CodeOwner
     */
    public static GB_CodeOwner[] additionDelta(Collection a_list1, Collection a_list2) {
        List retour = new ArrayList();
        for (Iterator it = a_list2.iterator(); it.hasNext(); ) {
            GB_CodeOwner l_item = (GB_CodeOwner) it.next();
            String l_id = l_item.getCode();
            if (getFirstWithCode(a_list1, l_id) == null) {
                retour.add(l_item);
            }
        }
        return (GB_CodeOwner[]) retour.toArray(new GB_CodeOwner[retour.size()]);
    }

    /**
     * Compare two lists of GB_CodeOwner elements returning elements in a_list2
     * that are also in a_list1.
     * 
     * @param a_list1 a list of GB_CodeOwner 
     * @param a_list2 a list of GB_CodeOwner
     */
    public static GB_CodeOwner[] commonDelta(Collection a_list1, Collection a_list2) {
        List retour = new ArrayList();
        for (Iterator it = a_list1.iterator(); it.hasNext(); ) {
            GB_CodeOwner l_item = (GB_CodeOwner) it.next();
            String l_id = l_item.getCode();
            if (getFirstWithCode(a_list2, l_id) != null) {
                retour.add(l_item);
            }
        }
        return (GB_CodeOwner[]) retour.toArray(new GB_CodeOwner[retour.size()]);
    }

    /**
     * Method to filter a list of items by code
     */
    public static void filterByCode(Collection a_items, String a_code) {
        if (a_items == null) {
            return;
        }
        Iterator it = a_items.iterator();
        while (it.hasNext()) {
            GB_CodeOwner l_item = (GB_CodeOwner) it.next();
            if (!isCodeOwner(l_item, a_code)) {
                it.remove();
            }
        }
    }

    /**
     * Returns the code of item a_item.
     * Throws ClassCastException if a_item is not a GB_CodeOwner.
     */
    public static String getCode(Object a_item) {
        if (a_item == null) {
            return null;
        }
        return ((GB_CodeOwner) a_item).getCode();
    }

    /**
     * Returns the ids of the items.
     */
    public static String[] getCodes(Collection a_items) {
        if (a_items == null) {
            return new String[0];
        }
        int len = CTools.getSize(a_items);
        int i = 0;
        String[] retour = new String[len];
        for (Iterator it = a_items.iterator(); it.hasNext(); ) {
            GB_CodeOwner l_item = (GB_CodeOwner) it.next();
            if (l_item != null) {
                retour[i] = l_item.getCode();
            }
            i++;
        }
        return retour;
    }

    /**
     * Returns the ids of the items.
     */
    public static String[] getCodes(Object[] a_items) {
        if (a_items == null) {
            return new String[0];
        }
        int len = CTools.getSize(a_items);
        String[] retour = new String[len];
        for (int i = 0; i < len; i++) {
            GB_CodeOwner l_item = (GB_CodeOwner) a_items[i];
            if (l_item != null) {
                retour[i] = l_item.getCode();
            }
        }
        return retour;
    }

    /**
     * Returns the code of item a_item only if a_item is a GB_CodeOwner.
     * Returns null otherwise.
     */
    public static String getCodeSafe(Object a_item) {
        if (a_item == null) {
            return null;
        }
        if (a_item instanceof GB_CodeOwner) {
            return ((GB_CodeOwner) a_item).getCode();
        }
        return null;
    }

    /**
     * Method to filter a list of items by code
     */
    public static List getFilterByCode(Collection a_items, String a_code) {
        if (a_items == null) {
            return null;
        }
        GB_ObjectFilter l_filter = new MyFilterByCode(a_code);
        return GB_ObjectFilterTools.getFilter(a_items, l_filter);
    }

    /**
     * Method to filter a list of items by code
     */
    public static Object[] getFilterByCode(Object[] a_items, String a_code, Object[] a_return) {
        if (a_items == null) {
            return null;
        }
        GB_ObjectFilter l_filter = new MyFilterByCode(a_code);
        return GB_ObjectFilterTools.getFilter(a_items, l_filter, a_return);
    }

    /**
     * Method to filter a list of items by code
     */
    public static List getFilterByCodes(Collection a_items, String[] a_ids) {
        if (a_items == null) {
            return null;
        }
        GB_ObjectFilter l_filter = new MyFilterByCodes(a_ids);
        return GB_ObjectFilterTools.getFilter(a_items, l_filter);
    }

    /**
     * Method to filter a list of items by code
     */
    public static Object[] getFilterByCodes(Object[] a_items, String[] a_ids, Object[] a_return) {
        if (a_items == null) {
            return null;
        }
        GB_ObjectFilter l_filter = new MyFilterByCodes(a_ids);
        return GB_ObjectFilterTools.getFilter(a_items, l_filter, a_return);
    }

    /**
     * Returns first item with code = a_code.
     * Returns null if a_code not found.
     * @param a_items a collection of GB_CodeOwner
     */
    public static GB_CodeOwner getFirstWithCode(Collection a_items, String a_code) {
        if (a_items == null) {
            return null;
        }
        for (Iterator it = a_items.iterator(); it.hasNext(); ) {
            GB_CodeOwner l_item = (GB_CodeOwner) it.next();
            if (isCodeOwner(l_item, a_code)) {
                return l_item;
            }
        }
        return null;
    }

    /**
     * Returns first item with code = a_code.
     * Returns null if a_code not found.
     * @param a_items an array of GB_CodeOwner
     */
    public static GB_CodeOwner getFirstWithCode(Object[] a_items, String a_code) {
        int len = CTools.getSize(a_items);
        for (int i = 0; i < len; i++) {
            GB_CodeOwner l_item = (GB_CodeOwner) a_items[i];
            if (isCodeOwner(l_item, a_code)) {
                return l_item;
            }
        }
        return null;
    }

    /**
     * Returns the ids of the items.
     */
    public static String[] getSingleCodes(Collection a_items, boolean a_order, boolean a_ascending, boolean a_ignoreCase) {
        if (a_items == null) {
            return new String[0];
        }
        int i = 0;
        List retour = new ArrayList();
        for (Iterator it = a_items.iterator(); it.hasNext(); ) {
            GB_CodeOwner l_item = (GB_CodeOwner) it.next();
            if (l_item != null) {
                CTools.addSingle(retour, l_item.getCode());
            }
            i++;
        }
        if (a_order) {
            GB_ComparatorTools.sortStrings(retour, a_ascending, a_ignoreCase);
        }
        return CTools.toArrayOfString(retour);
    }

    /**
     * Returns true if a_item has code a_code.  
     */
    public static boolean isCodeOwner(GB_CodeOwner a_item, String a_code) {
        if (a_item == null) {
            return false;
        }
        String l_id = a_item.getCode();
        if (a_code == null) {
            return (l_id == null);
        } else {
            return a_code.equals(l_id);
        }
    }

    /**
     * Returns true if a_item has code contained into a_ids.  
     */
    public static boolean isCodeOwner(GB_CodeOwner a_item, String[] a_ids) {
        if (a_item == null) {
            return false;
        }
        String l_id = a_item.getCode();
        return GB_ArrayTools.contains(a_ids, l_id);
    }

    /**
     * Returns the code of item a_item.
     */
    public static boolean isEqualsByCode(Object a_item1, Object a_item2) {
        String l_value1 = getCode(a_item1);
        String l_value2 = getCode(a_item2);
        return GB_EqualsTools.equalsObject(l_value1, l_value2);
    }

    /**
     * Adds an item to a map indexed by code.
     */
    public static void putByCode(Map a_map, GB_CodeOwner a_item) {
        if (a_item == null) {
            return;
        }
        a_map.put(a_item.getCode(), a_item);
    }

    /**
     * Adds items to a map indexed by code.
     */
    public static void putByCodes(Map a_map, Collection a_items) {
        if (a_items == null) {
            return;
        }
        for (Iterator it = a_items.iterator(); it.hasNext(); ) {
            GB_CodeOwner l_item = (GB_CodeOwner) it.next();
            putByCode(a_map, l_item);
        }
    }

    /**
     * Adds items to a map indexed by code.
     */
    public static void putByCodes(Map a_map, Object[] a_items) {
        if (a_items == null) {
            return;
        }
        int len = CTools.getSize(a_items);
        for (int i = 0; i < len; i++) {
            GB_CodeOwner l_item = (GB_CodeOwner) a_items[i];
            putByCode(a_map, l_item);
        }
    }

    /**
     * Remove the CodeOwnerOwners with code a_code.
     * Returns the items count removed.
     */
    public static int removeByCodeOwner(Collection a_items, String a_code) {
        if (a_items == null) {
            return 0;
        }
        int retour = 0;
        for (Iterator it = a_items.iterator(); it.hasNext(); ) {
            GB_CodeOwner l_item = (GB_CodeOwner) it.next();
            if (isCodeOwner(l_item, a_code)) {
                it.remove();
                retour++;
            }
        }
        return retour;
    }

    /**
     * Remove the CodeOwnerOwners with code contained into a_ids.
     * Returns the items count removed.
     */
    public static int removeByCodeOwners(Collection a_items, String[] a_ids) {
        if (a_items == null) {
            return 0;
        }
        int retour = 0;
        for (Iterator it = a_items.iterator(); it.hasNext(); ) {
            GB_CodeOwner l_item = (GB_CodeOwner) it.next();
            if (isCodeOwner(l_item, a_ids)) {
                it.remove();
                retour++;
            }
        }
        return retour;
    }

    public static void sortByCodes(List a_items, boolean a_flagAscending, boolean a_flagIgnoreCase) {
        Comparator l_comparator = new MyComparatorByCode(a_flagAscending, a_flagIgnoreCase);
        GB_ComparatorTools.sort(a_items, l_comparator, true, false);
    }

    public static void sortByCodes(Object[] a_items, boolean a_flagAscending, boolean a_flagIgnoreCase) {
        Comparator l_comparator = new MyComparatorByCode(a_flagAscending, a_flagIgnoreCase);
        GB_ComparatorTools.sort(a_items, l_comparator, true, false);
    }

    /**
     * Compare two lists of GB_CodeOwner elements returning elements in a_list1
     * that are not in a_list2.
     * 
     * @param a_list1 a list of GB_CodeOwner 
     * @param a_list2 a list of GB_CodeOwner
     */
    public static GB_CodeOwner[] substractionDelta(Collection a_list1, Collection a_list2) {
        return additionDelta(a_list2, a_list1);
    }
}
