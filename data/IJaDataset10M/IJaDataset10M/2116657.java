package org.webical.web.action;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.webical.Calendar;

/**
 * Action generated when a Calendar is selected
 *
 * @author Mattijs Hoitink
 *
 */
public class CalendarSelectedAction implements IAction {

    private static final long serialVersionUID = 1L;

    private Calendar selectedCalendar;

    private AjaxRequestTarget ajaxRequestTarget;

    /**
	 * Constructor
	 * @param calendar The selected Calendar
	 * @param ajaxRequestTarget The Ajax target
	 */
    public CalendarSelectedAction(Calendar calendar, AjaxRequestTarget ajaxRequestTarget) {
        this.selectedCalendar = calendar;
        this.ajaxRequestTarget = ajaxRequestTarget;
    }

    /**
	 * Returns the selected Calendar
	 * @return The selected Calendar
	 */
    public Calendar getSelectedCalendar() {
        return selectedCalendar;
    }

    /**
	 * Returns the AjaxRequestTarget
	 * @return The Ajax target
	 */
    public AjaxRequestTarget getAjaxRequestTarget() {
        return ajaxRequestTarget;
    }
}
