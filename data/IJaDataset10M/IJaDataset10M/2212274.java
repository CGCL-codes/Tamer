package org.wam.core.event;

import org.wam.core.WamDocument;
import org.wam.core.WamElement;

/**
 * An event caused by user interaction
 */
public class UserEvent extends WamEvent<Void> {

    private final WamDocument theDocument;

    private final WamElement theElement;

    private boolean isCanceled;

    /**
	 * Creates a user event
	 * 
	 * @param type The event type that this event is an instance of
	 * @param doc The document that the event occurred in
	 * @param element The deepest-level element that the event occurred in
	 */
    public UserEvent(WamEventType<Void> type, WamDocument doc, WamElement element) {
        super(type, null);
        theDocument = doc;
        theElement = element;
    }

    /**
	 * @return The document in which this event occurred
	 */
    public WamDocument getDocument() {
        return theDocument;
    }

    /**
	 * @return The deepest-level element that the event occurred in
	 */
    public WamElement getElement() {
        return theElement;
    }

    /**
	 * @return Whether this event has been canceled
	 */
    public boolean isCanceled() {
        return isCanceled;
    }

    /**
	 * Cancels this event so that it will not be propagated to any more elements. This method should
	 * be called when a listener knows what the event was intended for by the user and has the
	 * capability to perform the intended action. In this case, the event should not be propagated
	 * further because its purpose is complete and any further action would be undesirable.
	 */
    public void cancel() {
        isCanceled = true;
    }
}
