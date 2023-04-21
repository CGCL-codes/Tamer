package org.eclipse.swt.nebula.widgets.compositetable.day;

/**
 * Abstract class CalendarableItemEventHandler.  An abstract class defining
 * the API for objects that implement strategy pattern services such as
 * insert, edit, and delete from a DayEditor object.
 * 
 * @since 3.2
 */
public abstract class CalendarableItemEventHandler {

    /**
	 * Process this CalenderableItemEvent, please.
	 * 
	 * @param e
	 *            The CalendarableItemEvent to process.
	 */
    public void handleRequest(CalendarableItemEvent e) {
    }

    /**
	 * This CalendarableItemEvent has been handled.
	 * 
	 * @param e
	 *            The CalendarableItemEvent that was processed, including the
	 *            results of processing the event.
	 */
    public void requestHandled(CalendarableItemEvent e) {
    }
}
