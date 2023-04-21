package org.webical.web.component.calendar;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.webical.Calendar;
import org.webical.Event;
import org.webical.manager.CalendarManager;
import org.webical.manager.EventManager;
import org.webical.manager.WebicalException;
import org.webical.web.action.FormFinishedAction;
import org.webical.web.action.IAction;
import org.webical.web.action.ShowSettingsAction;
import org.webical.web.app.WebicalWebAplicationException;
import org.webical.web.component.AbstractBasePanel;
import org.webical.web.component.behavior.FormComponentValidationStyleBehavior;
import org.webical.web.component.settings.SettingsPanelsPanel;
import org.webical.web.event.ExtensionPoint;

/**
 * Creates an add/edit for for a Calendar depending on whether a Calendar is provided or null
 *
 * @author Ivo van Dongen
 * @author Mattijs Hoitink
 */
public abstract class CalendarFormPanel extends AbstractBasePanel {

    private static final long serialVersionUID = 1L;

    private static Log log = LogFactory.getLog(CalendarFormPanel.class);

    public static final String FORM_EXTENSIONS_MARKUP_ID = "formExtensions";

    private static final String CALENDAR_ADD_EDIT_FORM_MARKUP_ID = "calendarAddEditForm";

    /** The calendar to edit */
    private Calendar calendar;

    /** Calendar manager instantiated by Spring */
    @SpringBean(name = "calendarManager")
    private CalendarManager calendarManager;

    /** Event Manager instantiatd by Spring */
    @SpringBean(name = "eventManager")
    private EventManager eventManager;

    private String oldUrl;

    /**
	 * Constructor
	 * @param markupId the id used in the markup
	 * @param calendar the Calendar to be edited or null when a new caledar is edited
	 */
    public CalendarFormPanel(String markupId, Calendar calendar) {
        super(markupId, CalendarFormPanel.class);
        this.calendar = calendar;
        if (this.calendar != null) {
            this.oldUrl = calendar.getUrl();
        }
    }

    public void setupCommonComponents() {
        ArrayList<String> calendarTypes;
        try {
            calendarTypes = new ArrayList<String>(calendarManager.getAvailableCalendarTypes());
        } catch (WebicalException e) {
            throw new WebicalWebAplicationException(e);
        }
        CalendarForm calendarForm = new CalendarForm(CALENDAR_ADD_EDIT_FORM_MARKUP_ID, calendar, calendarTypes) {

            private static final long serialVersionUID = 1L;

            @Override
            protected void onDiscard() {
                CalendarFormPanel.this.onAction(new FormFinishedAction(null));
            }

            @Override
            protected void persistCalendar(Calendar storeCalendar) {
                try {
                    List<Event> events = null;
                    boolean urlChanged;
                    if (oldUrl != null && oldUrl.equals(storeCalendar.getUrl())) {
                        urlChanged = false;
                        events = eventManager.getAllEvents(storeCalendar);
                    } else {
                        urlChanged = true;
                    }
                    if (events != null && !urlChanged) {
                        log.debug("Storing calendar with events.");
                        calendarManager.storeCalendar(storeCalendar, events);
                    } else {
                        log.debug("Storing calendar without events.");
                        calendarManager.storeCalendar(storeCalendar);
                    }
                    if (log.isDebugEnabled()) {
                        log.debug("Calendar saved: " + storeCalendar.getName() + " for user: " + storeCalendar.getUser().getUserId());
                    }
                    CalendarFormPanel.this.onAction(new ShowSettingsAction(SettingsPanelsPanel.CALENDAR_SETTINGS_TAB_INDEX));
                } catch (WebicalException e) {
                    error("Could not store the calendar");
                    throw new WebicalWebAplicationException("Calendar could not be saved: " + storeCalendar.getName() + " for user: " + storeCalendar.getUser().getUserId(), e);
                }
            }
        };
        calendarForm.add(new FormComponentValidationStyleBehavior());
        RepeatingView extensionPoint = new RepeatingView(FORM_EXTENSIONS_MARKUP_ID);
        getExtensionPoints().put(FORM_EXTENSIONS_MARKUP_ID, new ExtensionPoint(extensionPoint, new CompoundPropertyModel(this.calendar)));
        calendarForm.add(extensionPoint);
        addOrReplace(calendarForm);
    }

    public void setupAccessibleComponents() {
    }

    public void setupNonAccessibleComponents() {
    }

    /**
	 * Notify the parent the user is finished with the form
	 * @param target The Ajax target of the panel
	 */
    public abstract void onAction(IAction action);

    /**
	 * Used by Spring to set the CalendarManager
	 * @param calendarManager a CalendarManager
	 */
    public void setCalendarManager(CalendarManager calendarManager) {
        this.calendarManager = calendarManager;
    }

    /**
	 * Used by Spring to set the EventManager
	 * @param eventManager an EventManager
	 */
    public void setEventManager(EventManager eventManager) {
        this.eventManager = eventManager;
    }
}
