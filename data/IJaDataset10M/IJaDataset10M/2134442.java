package uk.ac.lkl.common.ui;

import uk.ac.lkl.common.util.event.UpdateListenable;
import uk.ac.lkl.common.util.event.UpdateListener;
import uk.ac.lkl.common.util.event.SourcedUpdateSupport;
import uk.ac.lkl.common.util.value.NumericValue;

/**
 * A typed ordinate that notifies interested parties when it changes.
 * 
 * This class overrides the setValue() method of the TypedOrdinate class and
 * notifies change listeners if appropriate.
 * 
 * @author $Author: darren.pearce $
 * @version $Revision: 1359 $
 * @version $Date: 2008-10-22 15:22:40 -0400 (Wed, 22 Oct 2008) $
 * 
 */
public class NotifyingOrdinate<N extends NumericValue<N>> extends TypedOrdinate<N> implements UpdateListenable<NotifyingOrdinate<N>> {

    /**
     * The ChangeSupport instance used to manage change listeners.
     * 
     */
    private SourcedUpdateSupport<NotifyingOrdinate<N>> updateSupport = new SourcedUpdateSupport<NotifyingOrdinate<N>>(this);

    /**
     * Create a new instance with the given value.
     * 
     * @param value
     *            the value
     * 
     */
    public NotifyingOrdinate(N value) {
        super(value);
    }

    /**
     * Set the value of this ordinate.
     * 
     * This compares the instances using .equals(). If the new value is
     * different to the current value, the current value is updated and a change
     * event fired.
     * 
     * @param value
     *            the new value
     * 
     */
    public void setValue(N value) {
        if (this.value == null) {
            if (value == null) return;
        } else if (this.value.equals(value)) return;
        this.value = value;
        updateSupport.fireObjectUpdated();
    }

    /**
     * Add an update listener to this instance.
     * 
     * @param listener
     *            the update listener to add
     * 
     */
    public void addUpdateListener(UpdateListener<NotifyingOrdinate<N>> listener) {
        updateSupport.addListener(listener);
    }

    /**
     * Remove a change listener from this instance.
     * 
     * @param listener
     *            the listener to remove
     * 
     */
    public void removeUpdateListener(UpdateListener<NotifyingOrdinate<N>> listener) {
        updateSupport.removeListener(listener);
    }
}
