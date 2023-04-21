package com.bradrydzewski.gwt.calendar.client;

import java.util.Date;
import com.bradrydzewski.gwt.calendar.client.event.DaySelectionEvent;
import com.bradrydzewski.gwt.calendar.client.event.DaySelectionHandler;
import com.bradrydzewski.gwt.calendar.client.event.HasDaySelectionHandlers;
import com.bradrydzewski.gwt.calendar.client.event.HasWeekSelectionHandlers;
import com.bradrydzewski.gwt.calendar.client.event.WeekSelectionEvent;
import com.bradrydzewski.gwt.calendar.client.event.WeekSelectionHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Label;

/**
 * Abstract base class defining the operations to render a calendar and
 * user-input dispatching methods. <p/> <p></p>Subclasses will provide the
 * details of rendering the calendar to visualize by day (Day View), monthly
 * (month view), agenda (list view) and the logic implementing the user-input
 * event processing.
 *
 * @author Brad Rydzewski
 */
public abstract class CalendarView implements HasSettings, HasWeekSelectionHandlers<Date>, HasDaySelectionHandlers<Date> {

    /**
     * Calendar widget bound to the view.
     *
     * @see CalendarWidget
     */
    protected CalendarWidget calendarWidget = null;

    /**
     * Number of days the calendar should display at a given time, 3 by
     * default.
     */
    private int displayedDays = 3;

    /**
     * Attaches this view to the provided {@link CalendarWidget}.
     *
     * @param calendarWidget The interactive widget containing the calendar
     */
    public void attach(CalendarWidget calendarWidget) {
        this.calendarWidget = calendarWidget;
    }

    /**
     * Detaches this view from the currently associated {@link CalendarWidget}.
     * TODO: The CalendarWidget might still have a reference to this
     * CalendarView, is that correct??
     */
    public void detatch() {
        calendarWidget = null;
    }

    /**
     * Returns the CSS style name of this calendar view.
     *
     * @return The CSS style that should be used when rendering this calendar
     *         view
     */
    public abstract String getStyleName();

    public void doSizing() {
    }

    public abstract void doLayout();

    /**
     * Returns the configured number of days the calendar should display at a
     * given time.
     *
     * @return The number of days this calendar view should display at a given
     *         time
     */
    public int getDisplayedDays() {
        return displayedDays;
    }

    /**
     * Sets the configured number of days the calendar should display at a given
     * time.
     *
     * @param displayedDays The number of days this calendar view should display
     *                      at a given time
     */
    public void setDisplayedDays(int displayedDays) {
        this.displayedDays = displayedDays;
    }

    public abstract void onDoubleClick(Element element, Event event);

    public abstract void onSingleClick(Element element, Event event);

    public abstract void onMouseOver(Element element, Event event);

    /**
     * Processes user {@link com.google.gwt.event.dom.client.KeyCodes.KEY_DELETE}
     * keystrokes. The <code>CalendarView</code> implementation is empty so that
     * subclasses are not forced to implement it if no specific logic is needed
     * for {@link com.google.gwt.event.dom.client.KeyCodes.KEY_DELETE}
     * keystrokes.
     */
    public void onDeleteKeyPressed() {
    }

    /**
     * Processes user {@link com.google.gwt.event.dom.client.KeyCodes.KEY_UP}
     * keystrokes. The <code>CalendarView</code> implementation is empty so that
     * subclasses are not forced to implement it if no specific logic is needed
     * for {@link com.google.gwt.event.dom.client.KeyCodes.KEY_UP} keystrokes.
     */
    public void onUpArrowKeyPressed() {
    }

    /**
     * Processes user {@link com.google.gwt.event.dom.client.KeyCodes.KEY_DOWN}
     * keystrokes. The <code>CalendarView</code> implementation is empty so that
     * subclasses are not forced to implement it if no specific logic is needed
     * for {@link com.google.gwt.event.dom.client.KeyCodes.KEY_DOWN}
     * keystrokes.
     */
    public void onDownArrowKeyPressed() {
    }

    /**
     * Processes user {@link com.google.gwt.event.dom.client.KeyCodes.KEY_LEFT}
     * keystrokes. The <code>CalendarView</code> implementation is empty so that
     * subclasses are not forced to implement it if no specific logic is needed
     * for {@link com.google.gwt.event.dom.client.KeyCodes.KEY_LEFT}
     * keystrokes.
     */
    public void onLeftArrowKeyPressed() {
    }

    /**
     * Processes user {@link com.google.gwt.event.dom.client.KeyCodes.KEY_RIGHT}
     * keystrokes. The <code>CalendarView</code> implementation is empty so that
     * subclasses are not forced to implement it if no specific logic is needed
     * for {@link com.google.gwt.event.dom.client.KeyCodes.KEY_RIGHT}
     * keystrokes.
     */
    public void onRightArrowKeyPressed() {
    }

    public abstract void onAppointmentSelected(Appointment appt);

    public final void selectAppointment(Appointment appt) {
        calendarWidget.setSelectedAppointment(appt, true);
    }

    public final void selectNextAppointment() {
        calendarWidget.selectNextAppointment();
    }

    public final void selectPreviousAppointment() {
        calendarWidget.selectPreviousAppointment();
    }

    public final void updateAppointment(Appointment toAppt) {
        calendarWidget.fireUpdateEvent(toAppt);
    }

    public final void deleteAppointment(Appointment appt) {
        calendarWidget.fireDeleteEvent(appt);
    }

    public final void openAppointment(Appointment appt) {
        calendarWidget.fireOpenEvent(appt);
    }

    public final void createAppointment(Appointment appt) {
        createAppointment(appt.getStart(), appt.getEnd());
    }

    public final void createAppointment(Date start, Date end) {
        calendarWidget.fireTimeBlockClickEvent(start);
    }

    public void scrollToHour(int hour) {
    }

    public CalendarSettings getSettings() {
        return calendarWidget.getSettings();
    }

    public void setSettings(CalendarSettings settings) {
        calendarWidget.setSettings(settings);
    }

    protected void addDayClickHandler(final Label dayLabel, final Date day) {
        dayLabel.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                fireSelectedDay(day);
            }
        });
    }

    protected void addWeekClickHandler(final Label weekLabel, final Date day) {
        weekLabel.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                fireSelectedWeek(day);
            }
        });
    }

    protected void fireSelectedDay(final Date day) {
        DaySelectionEvent.fire(this, day);
    }

    protected void fireSelectedWeek(final Date day) {
        WeekSelectionEvent.fire(this, day);
    }

    public HandlerRegistration addWeekSelectionHandler(WeekSelectionHandler<Date> handler) {
        return calendarWidget.addHandler(handler, WeekSelectionEvent.getType());
    }

    public HandlerRegistration addDaySelectionHandler(DaySelectionHandler<Date> handler) {
        return calendarWidget.addHandler(handler, DaySelectionEvent.getType());
    }

    public void fireEvent(GwtEvent<?> event) {
        calendarWidget.fireEvent(event);
    }
}
