package org.galaxy.gpf.event;

import java.util.EventListener;

/**
 * The listener interface for receiving cursor change events.
 * The class that is interested in processing a cursor change
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addCursorChangeListener<code> method. When
 * the cursor change event occurs, that object's appropriate
 * method is invoked.
 * 
 * @see CursorChangeEvent
 * @author Cheng Liang
 * @version 1.0.0
 */
public interface CursorChangeListener extends EventListener {

    /**
	 * Cursor changed.
	 * 
	 * @param e the event
	 */
    public void cursorChanged(CursorChangeEvent e);
}
