package com.softaspects.jsf.component.listmodel;

import com.softaspects.jsf.component.base.SelectionModel;
import com.softaspects.jsf.component.listmodel.Interval;

public interface ListSelectionModel extends SelectionModel {

    public static final int SELECTION_MIN_VALUE = 1;

    public static final int SELECTION_SINGLE = SELECTION_MIN_VALUE;

    public static final int SELECTION_SINGLE_INTERVAL = SELECTION_MIN_VALUE + 1;

    public static final int SELECTION_MULTIPLE_INTERVAL = SELECTION_MIN_VALUE + 2;

    public static final int SELECTION_MAX_VALUE = SELECTION_MIN_VALUE + 2;

    /**
	 * Set the current selection mode.
	 *
	 * @param aType selection mode
	 */
    public void setSelectionType(int aType);

    /**
	 * Set the current selection mode.
	 *
	 * @param aType selection mode
	 */
    public void setSelectionType(String aType);

    /**
     * Get the current selection mode.
     *
     * @return selection mode
     */
    public String getSelectionType();

    /**
	 * Get the current selection mode.
	 *
	 * @return selection mode
	 */
    public int getSelectionTypeCode();

    /**
	 * Change the selection to be into Interval.
	 *
	 * @param aInterval interval
	 */
    public void setSelectionInterval(Interval aInterval);

    /**
	 * Change the selection to be the set union of the current selection
	 * and the indices between index0 and index1 inclusive.
	 *
	 * @param aInterval interval
	 */
    public void addSelectionInterval(Interval aInterval);

    /**
	 * Change the selection to be the set difference of the current selection
	 * and the indices between index0 and index1 inclusive.
	 *
	 * @param aInterval interval
	 */
    public void removeSelectionInterval(Interval aInterval);

    /**
	 * Set row with the specified index to be selected
	 *
	 * @param aIndex index
	 */
    public void select(int aIndex);

    /**
	 * Set row with the specified index to be unselected
	 *
	 * @param aIndex index
	 */
    public void deselect(int aIndex);

    /**
	 * Get current selection intrevals
	 *
	 * @return intrevals
	 */
    public Interval[] getSelectionIntervals();

    /**
	 * Get first index of selection
	 *
	 * @return first index of selection
	 */
    public int getFirstSelection();

    /**
	 * Get last index of selection
	 *
	 * @return last index of selection
	 */
    public int getLastSelection();

    /**
	 * Get the number of selected items
	 *
	 * @return number of selected items
	 */
    public int getSelectionCount();

    /**
	 * Get indexes of selected items
	 *
	 * @return indexes of selected items
	 */
    public int[] getSelectionIndexes();

    /**
	 * Get the flag indicating that the specified index is selected.
	 *
	 * @param aIndex index
	 * @return is index selected
	 */
    public boolean isSelected(int aIndex);

    /**
	 * Change the selection to the empty set.
	 */
    public void clear();

    /**
	 * Get the flag indicating that selection is empty.
	 *
	 * @return is selection empty
	 */
    public boolean isEmpty();

    /**
	 * Get the flag indicating that selection was changed
	 *
	 * @return is selection was changed
	 */
    public boolean isSelectionChanged();

    /**
	 *
	 */
    public void setSelectionChangedNotify();
}
