package org.microemu.midp.examples.simpledemo;

import javax.microedition.lcdui.DateField;

public class DateFieldPanel extends BaseExamplesForm {

    public DateFieldPanel() {
        super("DateField");
        append(new DateField("Time", DateField.TIME));
        append(new DateField("Date & time", DateField.DATE_TIME));
    }
}
