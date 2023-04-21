package org.sapient_platypus.utils.swing.windowlist;

import java.awt.Frame;
import java.util.WeakHashMap;
import javax.swing.Action;
import javax.swing.ListModel;
import javax.swing.event.EventListenerList;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

/**
 * List of {@link Action}s used to open windows from a {@link WindowList}.
 * Suitable for use with {@link org.sapient_platypus.utils.swing.modelmenu.ModelMenu}, and
 * {@link org.sapient_platypus.utils.swing.modelmenu.ModelMenuBar}.
 * Produced by calling {@link WindowList#getActionList()}.
 * @author Nicholas Daley
 */
final class WindowActionList implements ListModel {

    /**
     * A map associating windows with {@link WindowAction}s.  Used to cache actions for opening windows.
     */
    private static final WeakHashMap windowSwitchActions = new WeakHashMap();

    /**
     * The {@link WindowList} that this action list is based on.
     */
    private final WindowList windowList;

    /**
     * Tracks {@link ListDataListener}s for this list.
     */
    private final EventListenerList listenerSupport = new EventListenerList();

    /**
     * Constructor.  Creates the action list from a given {@link WindowList}.
     * @param windowList ({@link #windowList}) The window list this list is based on.
     */
    public WindowActionList(final WindowList windowList) {
        windowList.addListDataListener(new ListDataListener() {

            public void intervalAdded(final ListDataEvent e) {
                final ListDataListener[] listeners = getListDataListeners();
                final ListDataEvent newE = new ListDataEvent(this, e.getType(), e.getIndex0(), e.getIndex1());
                for (int i = 0; i < listeners.length; i++) {
                    listeners[i].intervalAdded(newE);
                }
            }

            public void intervalRemoved(final ListDataEvent e) {
                final ListDataListener[] listeners = getListDataListeners();
                final ListDataEvent newE = new ListDataEvent(this, e.getType(), e.getIndex0(), e.getIndex1());
                for (int i = 0; i < listeners.length; i++) {
                    listeners[i].intervalRemoved(newE);
                }
            }

            public void contentsChanged(final ListDataEvent e) {
                final ListDataListener[] listeners = getListDataListeners();
                final ListDataEvent newE = new ListDataEvent(this, e.getType(), e.getIndex0(), e.getIndex1());
                for (int i = 0; i < listeners.length; i++) {
                    listeners[i].contentsChanged(newE);
                }
            }
        });
        this.windowList = windowList;
    }

    /**
     * {@inheritDoc}
     */
    public int getSize() {
        return windowList.getSize();
    }

    /**
     * {@inheritDoc}
     */
    public Object getElementAt(final int index) {
        final Frame w = windowList.getWindow(index);
        Action a = (Action) windowSwitchActions.get(w);
        if (a == null) {
            a = new WindowAction(w);
            windowSwitchActions.put(w, a);
        }
        return a;
    }

    /**
     * Gets an action based on an index.
     * @see #getElementAt(int)
     * @param index Index of the action to get.
     * @return The action.
     */
    public Action getAction(final int index) {
        return (Action) getElementAt(index);
    }

    /**
     * {@inheritDoc}
     */
    public void addListDataListener(final ListDataListener l) {
        listenerSupport.add(ListDataListener.class, l);
    }

    /**
     * {@inheritDoc}
     */
    public void removeListDataListener(final ListDataListener l) {
        listenerSupport.add(ListDataListener.class, l);
    }

    /**
     * @return The {@link ListDataListener}s registered with this list.
     */
    public ListDataListener[] getListDataListeners() {
        return (ListDataListener[]) listenerSupport.getListeners(ListDataListener.class);
    }
}
