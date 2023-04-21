package ggc.gui.calendar;

import ggc.data.calendar.CalendarEvent;
import ggc.data.calendar.CalendarListener;
import ggc.data.calendar.CalendarModel;
import javax.swing.*;
import java.awt.*;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Vector;

public class CalendarPane extends JPanel {

    private MonthPanel mPanel;

    private YearPanel yPanel;

    private DayPanel dPanel;

    CalendarModel cModel;

    Vector<CalendarListener> listeners = new Vector<CalendarListener>();

    public CalendarPane() {
        cModel = new CalendarModel(new GregorianCalendar(), this);
        mPanel = new MonthPanel(cModel);
        yPanel = new YearPanel(cModel);
        setLayout(new BorderLayout());
        Box a = Box.createHorizontalBox();
        a.add(mPanel);
        a.add(yPanel);
        add(a, BorderLayout.NORTH);
        dPanel = new DayPanel(cModel);
        add(dPanel, BorderLayout.CENTER);
        this.addCalendarListener(dPanel);
        this.addCalendarListener(mPanel);
        this.addCalendarListener(yPanel);
    }

    public Date getSelectedDate() {
        return cModel.getDate();
    }

    public void addCalendarListener(CalendarListener l) {
        listeners.addElement(l);
    }

    public void removeCalendarListener(CalendarListener l) {
        listeners.removeElement(l);
    }

    public void notifyListeners(int event) {
        notifyListeners(new CalendarEvent(new GregorianCalendar(cModel.getYear(), cModel.getMonth(), cModel.getDay()), event));
    }

    public void notifyListeners(CalendarEvent e) {
        for (int i = 0; i < listeners.size(); i++) {
            CalendarListener l = listeners.elementAt(i);
            l.dateHasChanged(e);
        }
    }
}
