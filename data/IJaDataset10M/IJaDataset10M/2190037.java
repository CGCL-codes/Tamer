package org.mbari.swing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EventListener;
import java.util.List;
import javax.swing.ListModel;
import javax.swing.MutableComboBoxModel;
import javax.swing.event.EventListenerList;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

/**
 * <p>TODO Get Paul to describe this class.</p>
 *
 * @author  : $Author: hohonuuli $
 * @version : $Revision: 332 $
 *
 * <p><font color="ff3333">TODO Explain the history of this file
 * <ul>
 *   <li>Why no longer extended from DefaultComboBoxModel</li>
 *   <li>Originally not really mutable</li>
 *   <li>Why using both item and element nomeclature</li>
 * </ul>
 * </font> </p>
 *
 * <p><font size="-1" color="#336699"><a href="http://www.mbari.org">
 * The Monterey Bay Aquarium Research Institute (MBARI)</a> provides this
 * documentation and code &quot;as is&quot;, with no warranty, express or
 * implied, of its quality or consistency. It is provided without support and
 * without obligation on the part of MBARI to assist in its use, correction,
 * modification, or enhancement. This information should not be published or
 * distributed to third parties without specific written permission from
 * MBARI.</font></p><br>
 *
 * <font size="-1" color="#336699">Copyright 2003 MBARI.<br>
 * MBARI Proprietary Information. All rights reserved.</font><br>
 *
 */
public class StringComboBoxModel implements MutableComboBoxModel, ListModel {

    /** <!-- Field description --> */
    public static final int BEGINS = 0;

    /** <!-- Field description --> */
    public static final int CONTAINS = 1;

    /** <!-- Field description --> */
    public static final int MATCHES = 2;

    /**
	 * @uml.property  name="compareCriteria"
	 */
    protected int compareCriteria;

    /**
	 * @uml.property  name="items"
	 * @uml.associationEnd  multiplicity="(0 -1)" elementType="java.lang.String"
	 */
    protected List items;

    /**
	 * @uml.property  name="listenerList"
	 * @uml.associationEnd  
	 */
    protected EventListenerList listenerList = new EventListenerList();

    /**
	 * @uml.property  name="maxItemLength"
	 */
    protected int maxItemLength;

    /**
	 * @uml.property  name="selectedItem"
	 */
    protected String selectedItem;

    /**
     * Constructs ...
     *
     */
    protected StringComboBoxModel() {
        this(new ArrayList());
    }

    /**
     * Constructs ...
     *
     *
     * @param items
     */
    public StringComboBoxModel(List items) {
        this(items, BEGINS);
    }

    /**
     * Constructs ...
     *
     *
     * @param items
     */
    public StringComboBoxModel(String[] items) {
        this(items, BEGINS);
    }

    /**
     * Constructs ...
     *
     *
     * @param items
     * @param compareCriteria
     */
    public StringComboBoxModel(List items, int compareCriteria) {
        setItems(items);
        setCompareCriteria(compareCriteria);
    }

    /**
     * Constructs ...
     *
     *
     * @param items
     * @param compareCriteria
     */
    public StringComboBoxModel(String[] items, int compareCriteria) {
        setItems(items);
        setCompareCriteria(compareCriteria);
    }

    /**
     * <p><!-- Method description --></p>
     *
     *
     * @param item
     */
    public void addElement(Object item) {
        checkItemType(item);
        items.add(item);
        int size = items.size();
        fireIntervalAdded(this, size - 1, size - 1);
        if ((size == 1) && (selectedItem == null) && (item != null)) {
            setSelectedItem(item);
        }
    }

    /**
     * <p><!-- Method description --></p>
     *
     *
     * @param item
     */
    public void addElement(String item) {
        checkItemType(item);
        items.add(item);
        int size = items.size();
        fireIntervalAdded(this, size, size);
    }

    /**
     * <p><!-- Method description --></p>
     *
     *
     * @param listener
     */
    public void addListDataListener(ListDataListener listener) {
        listenerList.add(ListDataListener.class, listener);
    }

    /**
     * <p><!-- Method description --></p>
     *
     *
     * @param item
     *
     * @throws ClassCastException
     */
    private void checkItemType(Object item) throws ClassCastException {
        if (!(item instanceof String)) {
            throw new ClassCastException("StringComboBoxModel only support String elements.");
        }
    }

    /**
     * <p><!-- Method description --></p>
     *
     *
     * @param value
     * @param lookFor
     *
     * @return
     */
    private boolean compare(String value, String lookFor) {
        boolean result = false;
        if (compareCriteria == BEGINS) {
            result = value.equals(lookFor);
        } else if (compareCriteria == CONTAINS) {
            result = (value.indexOf(lookFor) != -1);
        } else if (compareCriteria == MATCHES) {
            result = value.startsWith(lookFor);
        }
        return result;
    }

    /**
     * <p><!-- Method description --></p>
     *
     *
     * @param source
     * @param index_0
     * @param index_1
     */
    protected void fireContentsChanged(Object source, int index_0, int index_1) {
        Object[] listeners = listenerList.getListenerList();
        ListDataEvent event = null;
        for (int i = listeners.length - 2; 0 <= i; i -= 2) {
            if (listeners[i] == ListDataListener.class) {
                if (event == null) {
                    event = new ListDataEvent(source, ListDataEvent.CONTENTS_CHANGED, index_0, index_1);
                }
                ((ListDataListener) listeners[i + 1]).contentsChanged(event);
            }
        }
    }

    /**
     * <p><!-- Method description --></p>
     *
     *
     * @param source
     * @param index_0
     * @param index_1
     */
    protected void fireIntervalAdded(Object source, int index_0, int index_1) {
        Object[] listeners = listenerList.getListenerList();
        ListDataEvent event = null;
        for (int i = listeners.length - 2; 0 <= i; i -= 2) {
            if (listeners[i] == ListDataListener.class) {
                if (event == null) {
                    event = new ListDataEvent(source, ListDataEvent.INTERVAL_ADDED, index_0, index_1);
                }
                ((ListDataListener) listeners[i + 1]).intervalAdded(event);
            }
        }
    }

    /**
     * <p><!-- Method description --></p>
     *
     *
     * @param source
     * @param index_0
     * @param index_1
     */
    protected void fireIntervalRemoved(Object source, int index_0, int index_1) {
        Object[] listeners = listenerList.getListenerList();
        ListDataEvent event = null;
        for (int i = listeners.length - 2; 0 <= i; i -= 2) {
            if (listeners[i] == ListDataListener.class) {
                if (event == null) {
                    event = new ListDataEvent(source, ListDataEvent.INTERVAL_REMOVED, index_0, index_1);
                }
                ((ListDataListener) listeners[i + 1]).intervalRemoved(event);
            }
        }
    }

    /**
     * <p><!-- Method description --></p>
     *
     *
     * @param index
     *
     * @return
     */
    public Object getElementAt(int index) {
        return items.get(index);
    }

    /**
     * <p><!-- Method description --></p>
     *
     *
     * @param lookFor
     *
     * @return
     */
    public int getItemIndex(String lookFor) {
        return getItemIndex(lookFor, 0);
    }

    /**
     * <p><!-- Method description --></p>
     *
     *
     * @param lookFor
     * @param beginIndex
     *
     * @return
     */
    public int getItemIndex(String lookFor, int beginIndex) {
        int index = -1;
        if (lookFor != null) {
            boolean match = false;
            int size = items.size();
            for (index = beginIndex + 1; index < size; index++) {
                if (compare((String) items.get(index), lookFor) == true) {
                    match = true;
                    break;
                }
            }
            if (!match) {
                for (index = 0; index < beginIndex; index++) {
                    if (compare((String) items.get(index), lookFor)) {
                        match = true;
                        break;
                    }
                }
            }
            if (!match) {
                index = beginIndex;
            }
        }
        return index;
    }

    /**
     * <p><!-- Method description --></p>
     *
     *
     * @return
     */
    public ListDataListener[] getListDataListeners() {
        return (ListDataListener[]) listenerList.getListeners(ListDataListener.class);
    }

    /**
     * <p><!-- Method description --></p>
     *
     *
     * @param listenerType
     *
     * @return
     */
    public EventListener[] getListeners(Class listenerType) {
        return listenerList.getListeners(listenerType);
    }

    /**
     * <p><!-- Method description --></p>
     *
     *
     * @return
     */
    public int getMaxLength() {
        if (maxItemLength == 0) {
            for (int i = 0; i < items.size(); i++) {
                maxItemLength = Math.max(maxItemLength, ((String) items.get(i)).length());
            }
        }
        return maxItemLength;
    }

    /**
     * <p><!-- Method description --></p>
     *
     *
     * @return
     */
    public Object getSelectedItem() {
        return selectedItem;
    }

    /**
     * <p><!-- Method description --></p>
     *
     *
     * @return
     */
    public int getSize() {
        return items.size();
    }

    /**
     * <p><!-- Method description --></p>
     *
     *
     * @param item
     * @param index
     */
    public void insertElementAt(Object item, int index) {
        checkItemType(item);
        items.add(index, item);
        fireIntervalAdded(this, index, index);
    }

    /**
     * <p><!-- Method description --></p>
     *
     */
    public void removeAllElements() {
        if (0 < items.size()) {
            int beginIndex = 0;
            int endIndex = items.size() - 1;
            items.clear();
            selectedItem = null;
            fireIntervalRemoved(this, beginIndex, endIndex);
        }
    }

    /**
     * <p><!-- Method description --></p>
     *
     *
     * @param item
     */
    public void removeElement(Object item) {
        checkItemType(item);
        int itemIndex = items.indexOf(item);
        if (itemIndex != -1) {
            removeElementAt(itemIndex);
        }
    }

    /**
     * <p><!-- Method description --></p>
     *
     *
     * @param index
     */
    public void removeElementAt(int index) {
        if (getElementAt(index) == selectedItem) {
            if (index == 0) {
                setSelectedItem((items.size() == 1) ? null : getElementAt(index + 1));
            } else {
                setSelectedItem(getElementAt(index - 1));
            }
        }
        items.remove(index);
        fireIntervalRemoved(this, index, index);
    }

    /**
     * <p><!-- Method description --></p>
     *
     *
     * @param listener
     */
    public void removeListDataListener(ListDataListener listener) {
        listenerList.remove(ListDataListener.class, listener);
    }

    /**
	 * <p><!-- Method description --></p>
	 * @param  compareCriteria
	 * @uml.property  name="compareCriteria"
	 */
    public void setCompareCriteria(int compareCriteria) {
        if ((compareCriteria < BEGINS) || (compareCriteria > MATCHES)) {
            this.compareCriteria = BEGINS;
        } else {
            this.compareCriteria = compareCriteria;
        }
    }

    /**
	 * <p><!-- Method description --></p>
	 * @param  items
	 * @uml.property  name="items"
	 */
    public void setItems(List items) {
        for (int i = 0; i < items.size(); i++) {
            checkItemType(items.get(i));
        }
        this.items = items;
        maxItemLength = 0;
        if (0 < items.size()) {
            fireIntervalAdded(this, 0, items.size() - 1);
        }
    }

    /**
     * <p><!-- Method description --></p>
     *
     *
     * @param strings
     */
    public void setItems(String[] strings) {
        this.items = Arrays.asList(strings);
        maxItemLength = 0;
        if (0 < items.size()) {
            fireIntervalAdded(this, 0, items.size() - 1);
        }
    }

    /**
     * <p><!-- Method description --></p>
     *
     *
     * @param item
     */
    public void setSelectedItem(Object item) {
        checkItemType(item);
        if (((selectedItem != null) && !selectedItem.equals(item)) || ((selectedItem == null) && (item != null))) {
            selectedItem = (String) item;
            fireContentsChanged(this, -1, -1);
        }
    }
}
