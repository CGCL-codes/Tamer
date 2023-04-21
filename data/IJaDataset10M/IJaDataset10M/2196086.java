package org.nakedobjects.nos.client.dnd.view.calendar;

import org.nakedobjects.nos.client.dnd.Canvas;
import org.nakedobjects.nos.client.dnd.Click;
import org.nakedobjects.nos.client.dnd.Toolkit;
import org.nakedobjects.nos.client.dnd.drawing.Color;
import org.nakedobjects.nos.client.dnd.drawing.Text;

public class NamedCalendarBorderTab implements CalendarBorderTab {

    final Text style = Toolkit.getText("normal");

    final Color color = Toolkit.getColor("black");

    private final String name;

    private final CalendarDisplay calendarDisplay;

    public NamedCalendarBorderTab(String name, CalendarDisplay calendarDisplay) {
        this.name = name;
        this.calendarDisplay = calendarDisplay;
    }

    public boolean select(Click click, CalendarTemplate calendar) {
        calendar.setCalendarDisplay(calendarDisplay);
        return true;
    }

    public void draw(Canvas tabcanvas, int width) {
        tabcanvas.drawText(name, 5, 15, color, style);
    }
}
