package eu.future.earth.gwt.client.date.week;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;
import eu.future.earth.gwt.client.FtrGwtDateCss;
import eu.future.earth.gwt.client.date.DateEvent;
import eu.future.earth.gwt.client.date.DateEvent.DateEventActions;
import eu.future.earth.gwt.client.date.DateEventListener;
import eu.future.earth.gwt.client.date.DateRenderer;
import eu.future.earth.gwt.client.date.EventPanel;
import eu.future.earth.gwt.client.date.HasDateEventHandlers;
import eu.future.earth.gwt.client.date.week.whole.DayPanelParent;

public class DayEventpanel<T> extends VerticalPanel implements DayEventElement<T>, ClickHandler, HasDateEventHandlers<T> {

    final VerticalPanel dayEvent = new VerticalPanel();

    final HTML addDayEvent = new HTML();

    private DateRenderer<T> renderer = null;

    private DayPanelParent<T> parent = null;

    private DayHelper<T> theDay = new DayHelper<T>();

    private ArrayList<EventPanel<? extends T>> events = new ArrayList<EventPanel<? extends T>>();

    public DayEventpanel(DateRenderer<T> newRenderer, Date newDate, DayPanelParent<T> newParent) {
        super();
        theDay.setRenderer(newRenderer);
        theDay.setTime(newDate);
        parent = newParent;
        renderer = newRenderer;
        super.add(dayEvent);
        super.add(addDayEvent);
        dayEvent.setStyleName(FtrGwtDateCss.DAY_HEADER_EVENTS_ADD);
        super.setStyleName(FtrGwtDateCss.DAY_HEADER_EVENTS);
        addDayEvent.setStyleName(FtrGwtDateCss.DAY_HEADER_EVENTS_ADD);
        dayEvent.add(addDayEvent);
        addDayEvent.setHTML("&nbsp;");
        addDayEvent.setWidth("100%");
        addDayEvent.addClickHandler(this);
    }

    public void onClick(ClickEvent event) {
        if (event.getSource() == addDayEvent) {
            final Calendar helper = new GregorianCalendar();
            helper.setTime(theDay.getTime());
            helper.set(Calendar.HOUR_OF_DAY, 0);
            helper.set(Calendar.MINUTE, 0);
            helper.set(Calendar.SECOND, 0);
            final Date start = helper.getTime();
            renderer.createNewAfterClick(start, parent);
        }
    }

    public void addEvent(EventPanel<? extends T> newEvent, boolean isPartOfBatch) {
        removeEvent(newEvent.getValue());
        newEvent.addStyleName(FtrGwtDateCss.DND_GETTING_STARTED_LABEL);
        if (wide > 0) {
            newEvent.setWidth(wide);
        }
        events.add(newEvent);
        paintEvent(newEvent);
        repaint();
    }

    private void paintEvent(EventPanel<? extends T> newEvent) {
        dayEvent.remove(addDayEvent);
        dayEvent.add(newEvent);
        newEvent.repaintTime();
        dayEvent.add(addDayEvent);
    }

    public int neededHeight() {
        return (events.size() + 1) * (renderer.getEventTopHeight() + renderer.getEventBottomHeight());
    }

    public void clearEvents() {
        for (int i = 0; i < events.size(); i++) {
            final EventPanel<? extends T> ev = events.get(i);
            dayEvent.remove(ev);
        }
        events.clear();
        repaint();
    }

    public void destroy() {
        if (getParent() != null) {
            removeFromParent();
        }
    }

    public void setDay(Calendar newDay) {
        theDay.setTime(newDay.getTime());
        theDay.setFirstDayOfWeek(newDay.getFirstDayOfWeek());
    }

    public boolean isDay(T newEvent) {
        if (!renderer.isWholeDayEvent(newEvent)) {
            return false;
        }
        return theDay.isDay(newEvent);
    }

    public EventPanel<? extends T> removeEvent(T event) {
        return removeEvent(event, true);
    }

    public EventPanel<? extends T> removeEvent(T event, boolean repaint) {
        for (int i = 0; i < events.size(); i++) {
            final EventPanel<? extends T> ev = events.get(i);
            if (renderer.equal(ev.getValue(), event)) {
                dayEvent.remove(ev);
                events.remove(i);
                if (repaint) {
                    repaint();
                }
                return ev;
            }
        }
        return null;
    }

    private void repaint() {
        parent.repaint();
    }

    public void updateEvent(EventPanel<? extends T> newEvent) {
        removeEvent(newEvent.getValue(), false);
        addEvent(newEvent, false);
    }

    public void addEventByDrop(EventPanel<T> newEvent) {
        removeEvent(newEvent.getValue());
        final Calendar helper = new GregorianCalendar();
        newEvent.addStyleName(FtrGwtDateCss.DND_GETTING_STARTED_LABEL);
        {
            helper.setTime(newEvent.getStart());
            helper.set(Calendar.YEAR, theDay.get(Calendar.YEAR));
            helper.set(Calendar.MONTH, theDay.get(Calendar.MONTH));
            helper.set(Calendar.DAY_OF_MONTH, theDay.get(Calendar.DAY_OF_MONTH));
            newEvent.setStart(helper.getTime());
        }
        events.add(newEvent);
        paintEvent(newEvent);
        if (wide > 0) {
            newEvent.setWidth(wide);
        }
        DateEvent.fire(this, DateEventActions.DRAG_DROP, newEvent.getValue());
        repaint();
    }

    public HandlerRegistration addDateEventHandler(DateEventListener<? extends T> handler) {
        return addHandler(handler, DateEvent.getType());
    }

    private int wide = -1;

    public void setElementWide(int newWide) {
        if (newWide > 12) {
            wide = (newWide - 12);
        }
        if (wide > 3) {
            this.setWidth(wide - 2 + "px");
        }
        for (int i = 0; i < events.size(); i++) {
            final EventPanel<? extends T> ev = events.get(i);
            if (wide > 0) {
                ev.setWidth(wide);
            }
        }
    }

    public void repaintEvents() {
    }

    public void notifyParentOfUpdate(DateEventActions action, T data) {
        DateEvent.fire(this, action, data);
    }
}
