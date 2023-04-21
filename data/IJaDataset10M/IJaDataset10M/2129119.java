package voji.ui;

import java.util.*;
import javax.swing.*;

/**
 * This is a list that allows the user to change the order of its items.
 * Each item must have an integer ID.
 * The new order of these IDs can be retrieved from this list.
 * There are simple methods for changing the order of the items.
 * These methods should be called by the Dialog/Frame that contains this list.
 */
public class JOrderList extends JList {

    /**
     * Creates a new, empty JOrderList instance
     */
    public JOrderList() {
        setModel(new OrderListModel());
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    /**
     * Creates a new JOrderList instance with the given items
     */
    public JOrderList(Vector items) {
        this();
        setItems(items);
    }

    /**
     * Sets the items of this list.
     * The argument has to be a Vector of Vectors with 2 elements:
     * <UL>
     * <LI>an Integer containing and unique id
     * <LI>the Object which will be shown in the list
     * </UL>
     *
     * @param items the Vector of Vector of [Integer,Object]
     *              where to take the items from.
     */
    public void setItems(Vector items) {
        ((OrderListModel) getModel()).setItems(items);
    }

    /**
     * Returns the IDs in their current order
     *
     * @return an Array of the IDs in the same order
     *         as their Strings in the list.
     */
    public int[] getOrder() {
        return ((OrderListModel) getModel()).getOrder();
    }

    /**
     * Moves the current item one position up
     */
    public void moveUp() {
        ((OrderListModel) getModel()).moveItem(getSelectedIndex(), getSelectedIndex() - 1);
    }

    /**
     * Moves the current item one position down
     */
    public void moveDown() {
        ((OrderListModel) getModel()).moveItem(getSelectedIndex(), getSelectedIndex() + 1);
    }

    /**
     * Moves the current item to the top of this list
     */
    public void moveTop() {
        ((OrderListModel) getModel()).moveItem(getSelectedIndex(), 0);
    }

    /**
     * Moves the current item to the bottom of this list
     */
    public void moveBottom() {
        ((OrderListModel) getModel()).moveItem(getSelectedIndex(), getModel().getSize() - 1);
    }

    /**
     * This is the list model for OrderLists
     */
    protected class OrderListModel extends AbstractListModel {

        /**
	 * The IDs
	 */
        protected int[] ids = null;

        /**
	 * The shown items
	 */
        protected Object[] values = null;

        /**
	 * Constructs an OrderListModel with empty data
	 */
        public OrderListModel() {
        }

        /**
	 * @return the length of the list
	 */
        public int getSize() {
            return ids == null ? 0 : ids.length;
        }

        /**
	 * @param index the index whose value has to be retrieved
	 * @return the value at the specified index
	 */
        public Object getElementAt(int index) {
            return values == null ? null : values[index];
        }

        /**
	 * Sets the items of this model.
	 * The argument has to be a Vector of Vectors with 2 elements:
	 * <UL>
	 * <LI>an Integer containing and unique id
	 * <LI>the Object which will be shown in the list
	 * </UL>
	 *
	 * @param items the Vector of Vector of [Integer,Object]
	 *              where to take the items from.
	 */
        public void setItems(Vector items) {
            ids = new int[items.size()];
            values = new Object[items.size()];
            for (int i = 0; i < items.size(); i++) {
                Vector v = (Vector) items.get(i);
                ids[i] = ((Number) v.get(0)).intValue();
                values[i] = v.get(1);
            }
            fireContentsChanged(this, 0, getSize() - 1);
        }

        /**
	 * Returns the IDs in their current order
	 *
	 * @return an Array of the IDs in the same order
	 *         as their Strings in the list.
	 */
        public int[] getOrder() {
            return (int[]) ids.clone();
        }

        /**
	 * Switches the positions of two items
	 *
	 * @param index1
	 * @param index2 the two indices of these items
	 */
        protected void switchItems(int index1, int index2) {
            if (index1 >= 0 && index1 < getSize() && index2 >= 0 && index2 < getSize() && index1 != index2) {
                int tmpID = ids[index1];
                ids[index1] = ids[index2];
                ids[index2] = tmpID;
                Object tmpValue = values[index1];
                values[index1] = values[index2];
                values[index2] = tmpValue;
                ListSelectionModel m = getSelectionModel();
                boolean tmp1 = m.isSelectedIndex(index1);
                boolean tmp2 = m.isSelectedIndex(index2);
                if (!tmp1) m.removeIndexInterval(index2, index2);
                if (!tmp2) m.removeIndexInterval(index1, index1);
                if (tmp1) m.addSelectionInterval(index2, index2);
                if (tmp2) m.addSelectionInterval(index1, index1);
            }
        }

        /**
	 * Moves an item
	 *
	 * @param from the index of the item to move
	 * @param to the index where to move the item
	 */
        public void moveItem(int from, int to) {
            if (from >= 0 && from < getSize() && to >= 0 && to < getSize() && from != to) {
                for (int i = (to - from) / Math.abs(to - from); from != to; ) switchItems(from, from += i);
                fireContentsChanged(this, from, to);
            }
        }
    }
}
