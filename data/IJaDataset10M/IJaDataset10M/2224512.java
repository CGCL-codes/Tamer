package org.webical.web.action;

import java.util.Calendar;
import java.util.GregorianCalendar;
import org.apache.wicket.ajax.AjaxRequestTarget;

/**
 * Action for adding an Event
 *
 * @author Mattijs Hoitink
 *
 */
public class AddEventAction implements IAction {

    private static final long serialVersionUID = 1L;

    /**
	 * The date of the Event
	 */
    private GregorianCalendar eventDate;

    /**
	 * Constructor.
	 * @param eventDate The date of the Event
	 */
    public AddEventAction(Calendar eventDate) {
        this.eventDate = (GregorianCalendar) eventDate;
    }

    /**
	 * Gets the date of the Event.
	 * @return The date of the Event
	 */
    public GregorianCalendar getEventDate() {
        return eventDate;
    }

    @Override
    public String toString() {
        return "AddEventAction: eventDate = " + eventDate;
    }

    public AjaxRequestTarget getAjaxRequestTarget() {
        return null;
    }
}
