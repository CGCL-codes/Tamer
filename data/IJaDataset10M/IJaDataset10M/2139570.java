package org.eclipse.swt.events;

import org.eclipse.swt.widgets.Event;

/**
 * Instances of this class are sent as a result of
 * trees being expanded and collapsed.
 *
 * @see TreeListener
 */
public final class TreeEvent extends SelectionEvent {

    static final long serialVersionUID = 3257282548009677109L;

    /**
 * Constructs a new instance of this class based on the
 * information in the given untyped event.
 *
 * @param e the untyped event containing the information
 */
    public TreeEvent(Event e) {
        super(e);
    }
}
