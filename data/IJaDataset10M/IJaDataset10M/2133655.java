package org.webical.web.action;

import java.util.Calendar;
import java.util.GregorianCalendar;
import org.apache.wicket.ajax.AjaxRequestTarget;

/**
 * Action for selecting a day.
 *
 * @author Mattijs Hoitink
 */
public class DaySelectedAction implements IAction {

    private static final long serialVersionUID = 1L;

    /**
	 * The date of the selected day.
	 */
    private GregorianCalendar daySelected;

    /**
	 * Constructor.
	 * @param daySelected The date of the selected day
	 */
    public DaySelectedAction(Calendar daySelected) {
        this.daySelected = (GregorianCalendar) daySelected;
    }

    /**
	 * Gets the date for the selected day.
	 * @return The date for the selected day
	 */
    public Calendar getDaySelected() {
        return daySelected;
    }

    @Override
    public String toString() {
        return "DaySelectedAction";
    }

    public AjaxRequestTarget getAjaxRequestTarget() {
        return null;
    }
}
