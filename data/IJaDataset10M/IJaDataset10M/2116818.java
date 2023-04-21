package com.leclercb.taskunifier.gui.commons.values;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import org.jdesktop.swingx.renderer.StringValue;
import com.leclercb.taskunifier.gui.main.Main;

public class StringValueCalendar implements StringValue {

    public static final StringValueCalendar INSTANCE_DATE = new StringValueCalendar(false);

    public static final StringValueCalendar INSTANCE_DATE_TIME = new StringValueCalendar(true);

    private DateFormat formatter;

    private StringValueCalendar(boolean showTime) {
        boolean showDayOfWeek = Main.getSettings().getBooleanProperty("date.show_day_of_week");
        String dateFormat = Main.getSettings().getStringProperty("date.date_format");
        String timeFormat = Main.getSettings().getStringProperty("date.time_format");
        String format = "";
        if (showDayOfWeek) format += "E ";
        if (showTime) format += dateFormat + " " + timeFormat; else format += dateFormat;
        this.formatter = new SimpleDateFormat(format);
    }

    @Override
    public String getString(Object value) {
        if (value == null || !(value instanceof Calendar)) return " ";
        return this.formatter.format(((Calendar) value).getTime());
    }
}
