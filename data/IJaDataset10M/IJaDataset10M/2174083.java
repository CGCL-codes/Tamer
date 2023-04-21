package org.kineticsystem.commons.data.model.swing.adapters;

import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataListener;
import org.kineticsystem.commons.data.model.ActiveList;
import org.kineticsystem.commons.data.model.ActiveListEvent;
import org.kineticsystem.commons.data.model.ActiveListListener;

/**
 * This is an adapter to adapt the an <tt>ActiveList<tt> to a
 * <tt>ComboBoxModel</tt>.
 * @author Giovanni Remigi
 * @version $Revision: 145 $
 */
public class ComboBoxModelAdapter implements ComboBoxModel, ActiveListListener {

    /** The adaptee. */
    protected ActiveList<?> adaptee;

    /**
     * Component used to transform <tt>ActiveListEvent</tt> events to
     * <tt>ListDataEvent</tt> events. It receives events from the
     * <tt>DataList</tt> adaptee, transforms and sends them to all registered
     * <tt>ListDataListener</tt> listeners.
     */
    private ListDataListenerDispatcher dispatcher;

    /** The selected item. */
    private Object selected;

    /** Default constructor. */
    public ComboBoxModelAdapter(ActiveList<?> adaptee) {
        this.adaptee = adaptee;
        init();
    }

    /** Initializing method. */
    private void init() {
        if (adaptee == null) {
            throw new NullPointerException("Adaptee cannot be null!");
        }
        if (!adaptee.isEmpty()) {
            selected = adaptee.get(0);
        }
        dispatcher = new ListDataListenerDispatcher(this);
        this.adaptee.addActiveListListener(dispatcher);
    }

    /** {@inheritDoc} */
    public void addListDataListener(ListDataListener listener) {
        dispatcher.addListDataListener(listener);
    }

    /** {@inheritDoc} */
    public void removeListDataListener(ListDataListener listener) {
        dispatcher.removeListDataListener(listener);
    }

    /** {@inheritDoc} */
    public Object getElementAt(int index) {
        Object obj = adaptee.get(index);
        return obj;
    }

    /** {@inheritDoc} */
    public int getSize() {
        return adaptee.size();
    }

    /** {@inheritDoc} */
    public Object getSelectedItem() {
        return selected;
    }

    /** {@inheritDoc} */
    public void setSelectedItem(Object anItem) {
        selected = anItem;
    }

    /** 
     * When the model has changed, the selection must change consequently.
     * @param event The event thrown by the model.
     */
    public void contentsChanged(ActiveListEvent event) {
        if (adaptee.isEmpty()) {
            selected = null;
        } else if (!adaptee.contains(selected)) {
            selected = adaptee.get(0);
        }
    }
}
