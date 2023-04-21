package org.japura.examples.calendarfield;

import java.awt.Component;
import java.net.URL;
import java.util.GregorianCalendar;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import org.japura.examples.AbstractExample;
import org.japura.gui.calendar.CalendarField;
import org.japura.gui.model.DateDocument;
import org.japura.util.date.DateSeparator;

public class Example3 extends AbstractExample {

    @Override
    protected Component buildExampleComponent() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(80, 0, 0, 0));
        Class<?> clss = Example3.class;
        URL urlImage = clss.getResource("/resources/images/find.png");
        DateDocument dd = new DateDocument(Locale.ENGLISH, DateSeparator.SLASH);
        CalendarField cf = new CalendarField(dd);
        cf.setCalendarButtonIcon(urlImage);
        GregorianCalendar gc = new GregorianCalendar();
        gc.set(GregorianCalendar.YEAR, 2011);
        gc.set(GregorianCalendar.MONTH, 0);
        gc.set(GregorianCalendar.DAY_OF_MONTH, 20);
        cf.getDateDocument().setDate(gc.getTimeInMillis());
        panel.add(cf);
        return panel;
    }

    public static void main(String[] args) {
        Example3 example = new Example3();
        example.runExample();
    }
}
