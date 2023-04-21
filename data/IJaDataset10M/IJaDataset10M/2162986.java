package org.webical.web.component.calendar;

import java.util.Arrays;
import java.util.GregorianCalendar;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.webical.web.action.AddEventAction;
import org.webical.web.action.DaySelectedAction;
import org.webical.web.action.IAction;
import org.webical.web.component.AbstractBasePanel;
import org.webical.web.component.calendar.model.EventsModel;

public abstract class WeekDayPanel extends AbstractBasePanel {

    private static final long serialVersionUID = 1L;

    private static final String DAY_LINK_MARKUP_ID = "dayLink";

    private static final String DAY_LABEL_MARKUP_ID = "dayLabel";

    private static final String EVENT_ITEM_MARKUP_ID = "eventItem";

    /** The {@code EventsModel} to use */
    private EventsModel eventsModel;

    /** The date for the day this panel is representing */
    private GregorianCalendar dayDate;

    /** Contains the actions this panel can handle */
    @SuppressWarnings("unchecked")
    protected static Class[] PANELACTIONS = new Class[] {};

    private Link dayLink, addEventLink;

    private EventsListView dayEventsListView;

    public WeekDayPanel(String markupId, EventsModel eventsModel) {
        super(markupId, WeekDayPanel.class);
        this.eventsModel = eventsModel;
        this.dayDate = new GregorianCalendar();
        this.dayDate.setTime(eventsModel.getStartDate());
    }

    public void setupCommonComponents() {
    }

    public void setupAccessibleComponents() {
        dayLink = new Link(DAY_LINK_MARKUP_ID) {

            private static final long serialVersionUID = 1L;

            @Override
            public void onClick() {
                WeekDayPanel.this.onAction(new DaySelectedAction(dayDate));
            }
        };
        dayLink.add(new Label(DAY_LABEL_MARKUP_ID, String.valueOf(dayDate.get(GregorianCalendar.DAY_OF_MONTH))));
        add(dayLink);
        addEventLink = new Link("addEventLink") {

            private static final long serialVersionUID = 1L;

            @Override
            public void onClick() {
                WeekDayPanel.this.onAction(new AddEventAction(dayDate));
            }
        };
        addEventLink.setVisible(CalendarPanel.enableAddEvent);
        add(addEventLink);
        dayEventsListView = new EventsListView(EVENT_ITEM_MARKUP_ID, eventsModel, dayDate) {

            private static final long serialVersionUID = 1L;

            @Override
            public void onAction(IAction action) {
                if (Arrays.asList(MonthDayPanel.PANELACTIONS).contains(action.getClass())) {
                } else {
                    WeekDayPanel.this.onAction(action);
                }
            }
        };
        dayEventsListView.showEndTime(false);
        add(dayEventsListView);
    }

    public void setupNonAccessibleComponents() {
    }

    public abstract void onAction(IAction action);
}
