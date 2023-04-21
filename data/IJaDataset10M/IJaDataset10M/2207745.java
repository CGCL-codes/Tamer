package com.loribel.commons.swing.tools;

import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import com.loribel.commons.util.CTools;
import com.loribel.commons.util.collection.GB_IntegerArray;

/**
 * Tools.
 */
public final class GB_ListSelectionTools {

    /**
     * Select some items into a list.
     *
     * @param a_model
     * @param a_selectionModel
     * @param a_selectedItems
     */
    public static void addSelection(ListModel a_model, ListSelectionModel a_selectionModel, Object[] a_selectedItems) {
        if (a_selectedItems == null) {
            return;
        }
        int[] l_indexes = getIndexes(a_model, a_selectedItems);
        int len = l_indexes.length;
        for (int i = 0; i < len; i++) {
            int l_index = l_indexes[i];
            a_selectionModel.addSelectionInterval(l_index, l_index);
        }
    }

    /**
     * Returns the first index of the item into the list model.
     * Returns -1 if item is not found into the listModel.
     */
    public static int getIndex(ListModel a_model, Object a_item) {
        if (a_model == null) {
            return -1;
        }
        int len = a_model.getSize();
        for (int i = 0; i < len; i++) {
            if (a_item.equals(a_model.getElementAt(i))) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Returns an array of indexs of the items into the list model.
     * Returns -1 into array if an item is not found into the listModel.
     */
    public static int[] getIndexes(ListModel a_model, Object[] a_items) {
        int len = CTools.getSize(a_items);
        int[] retour = new int[len];
        for (int i = 0; i < len; i++) {
            retour[i] = getIndex(a_model, a_items[i]);
        }
        return retour;
    }

    /**
     * Returns an array with all the selected inndex.
     *
     * @param a_model
     * @param a_selectionModel
     */
    public static int[] getSelectedIndexes(ListModel a_model, ListSelectionModel a_selectionModel) {
        int len = a_model.getSize();
        return getSelectedIndexes(len, a_selectionModel);
    }

    /**
     * Returns an array with all the selected inndex.
     *
     * @param a_model
     * @param a_selectionModel
     */
    public static int[] getSelectedIndexes(int a_nbItems, ListSelectionModel a_selectionModel) {
        GB_IntegerArray retour = new GB_IntegerArray(a_nbItems);
        for (int i = 0; i < a_nbItems; i++) {
            if (a_selectionModel.isSelectedIndex(i)) {
                retour.add(i);
            }
        }
        return retour.toArray();
    }

    /**
     * Returns an array with all the selected items.
     */
    public static Object[] getSelectedItems(ListModel a_model, ListSelectionModel a_selectionModel) {
        int[] l_indexes = getSelectedIndexes(a_model, a_selectionModel);
        if (l_indexes == null) {
            return null;
        }
        int len = l_indexes.length;
        Object[] retour = new Object[len];
        for (int i = 0; i < len; i++) {
            int l_index = l_indexes[i];
            retour[i] = a_model.getElementAt(l_index);
        }
        return retour;
    }

    /**
     * Inverse the selection.
     *
     * @param a_model
     * @param a_selectionModel
     */
    public static void inverseSelection(ListModel a_model, ListSelectionModel a_selectionModel) {
        int[] l_selected = getSelectedIndexes(a_model, a_selectionModel);
        selectAll(a_model, a_selectionModel);
        if (l_selected != null) {
            int len = l_selected.length;
            for (int i = 0; i < len; i++) {
                int l_index = l_selected[i];
                a_selectionModel.removeSelectionInterval(l_index, l_index);
            }
        }
    }

    /**
     * Select all items.
     *
     * @param a_model
     * @param a_selectionModel
     */
    public static void selectAll(ListModel a_model, ListSelectionModel a_selectionModel) {
        int l_min = 0;
        int l_max = a_model.getSize() - 1;
        if (l_max == -1) {
            l_min = -1;
        }
        a_selectionModel.setSelectionInterval(l_min, l_max);
    }

    private GB_ListSelectionTools() {
    }
}
