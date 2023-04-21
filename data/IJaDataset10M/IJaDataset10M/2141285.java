package org.webical.web.action;

import java.util.Calendar;
import java.util.GregorianCalendar;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.webical.Event;

/**
 * Action for editing an event
 *
 * @author Mattijs Hoitink
 */
public class EditEventAction implements IAction {

    private static final long serialVersionUID = 1L;

    private Event selectedEvent;

    private GregorianCalendar selectedEventDate;

    public EditEventAction(Event selectedEvent, Calendar selectedEventDate) {
        this.selectedEvent = selectedEvent;
        this.selectedEventDate = (GregorianCalendar) selectedEventDate;
    }

    public Event getSelectedEvent() {
        return selectedEvent;
    }

    public Calendar getSelectedEventDate() {
        return selectedEventDate;
    }

    @Override
    public String toString() {
        return "EditEventAction: selectedEvent = " + selectedEvent + ", selectedEventDate = " + selectedEventDate.getTime();
    }

    public AjaxRequestTarget getAjaxRequestTarget() {
        return null;
    }
}
