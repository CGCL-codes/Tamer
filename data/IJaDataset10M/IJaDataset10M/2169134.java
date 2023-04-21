package com.potix.zul.html;

import com.potix.zul.html.event.ListDataListener;

/**
 * This interface defines the methods components like {@link Listbox}
 * use to get the value of each cell and the length of the list.
 *
 * @author <a href="mailto:tomyeh@potix.com">tomyeh@potix.com</a>
 * @see Listbox
 * @see ListitemRenderer
 */
public interface ListModel {

    /** Returns the value at the specified index.
	 */
    public Object getElementAt(int index);

    /** Returns the length of the list.
	 */
    public int getSize();

    /** Adds a listener to the list that's notified each time a change
	 * to the data model occurs. 
	 */
    public void addListDataListener(ListDataListener l);

    /** Removes a listener from the list that's notified each time
     * a change to the data model occurs. 
     */
    public void removeListDataListener(ListDataListener l);
}
