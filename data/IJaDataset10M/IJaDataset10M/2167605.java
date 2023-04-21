package org.ufacekit.ui.swing.jface.viewers.internal.swt.events;

import org.ufacekit.ui.swing.jface.viewers.internal.swt.internal.SWTEventObject;
import org.ufacekit.ui.swing.jface.viewers.internal.swt.widgets.Event;
import org.ufacekit.ui.swing.jface.viewers.internal.swt.widgets.Widget;

/**
 * This is the super class for all typed event classes provided
 * by SWT. Typed events contain particular information which is
 * applicable to the event occurrence.
 *
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 */
public class TypedEvent extends SWTEventObject {

    /**
	 * the widget that issued the event
	 */
    public Widget widget;

    /**
	 * the time that the event occurred.
	 *
	 * NOTE: This field is an unsigned integer and should
	 * be AND'ed with 0xFFFFFFFFL so that it can be treated
	 * as a signed long.
	 */
    public int time;

    /**
	 * a field for application use
	 */
    public Object data;

    /**
	 *
	 */
    static final long serialVersionUID = 3257285846578377524L;

    /**
 * Constructs a new instance of this class.
 *
 * @param object the object that fired the event
 */
    public TypedEvent(Object object) {
        super(object);
    }

    /**
 * Constructs a new instance of this class based on the
 * information in the argument.
 *
 * @param e the low level event to initialize the receiver with
 */
    public TypedEvent(Event e) {
        super(e.widget);
        this.widget = e.widget;
        this.time = e.time;
        this.data = e.data;
    }

    /**
 * Returns the name of the event. This is the name of
 * the class without the package name.
 *
 * @return the name of the event
 */
    String getName() {
        String string = getClass().getName();
        int index = string.lastIndexOf('.');
        if (index == -1) return string;
        return string.substring(index + 1, string.length());
    }

    /**
 * Returns a string containing a concise, human-readable
 * description of the receiver.
 *
 * @return a string representation of the event
 */
    public String toString() {
        return getName() + "{" + widget + " time=" + time + " data=" + data + "}";
    }
}
