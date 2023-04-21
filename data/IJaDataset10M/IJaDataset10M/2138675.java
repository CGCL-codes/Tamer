package org.ufacekit.ui.databinding.events.observable;

import org.eclipse.core.databinding.observable.IObservablesListener;
import org.eclipse.core.databinding.observable.ObservableEvent;

/**
 * Fired event.
 *
 */
public class FiredEvent extends ObservableEvent {

    private static final long serialVersionUID = 2305345286999701156L;

    static final Object TYPE = new Object();

    public FiredEvent(IObservableEvent source) {
        super(source);
    }

    /**
	 * Returns the observable event from which this event originated.
	 * 
	 * @return returns the observable eventfrom which this event originated
	 */
    public IObservableEvent getObservableEvent() {
        return (IObservableEvent) source;
    }

    protected void dispatch(IObservablesListener listener) {
        ((IFiredEventistener) listener).handleFiredEvent(this);
    }

    protected Object getListenerType() {
        return TYPE;
    }
}
