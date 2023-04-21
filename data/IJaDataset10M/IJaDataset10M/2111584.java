package org.rjam.alert.businesshours;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import org.rjam.xml.Token;

public class Schedule implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String TOKEN_SCHEDULE = "Schedule";

    public static final String[] DAY_OF_WEEK = new String[] { "Sunday", "Monday", "Tuesday", "Wednesday", "Thusday", "Friday", "Saturday" };

    public static final String TOKEN_NAME = "Name";

    private String name;

    private Day[] days = { new Day(new BusinessHours(0, 0, 23, 59)), new Day(new BusinessHours(0, 0, 23, 59)), new Day(new BusinessHours(0, 0, 23, 59)), new Day(new BusinessHours(0, 0, 23, 59)), new Day(new BusinessHours(0, 0, 23, 59)), new Day(new BusinessHours(0, 0, 23, 59)), new Day(new BusinessHours(0, 0, 23, 59)) };

    public Schedule() {
        super();
    }

    public Schedule(String name) {
        this();
        setName(name);
    }

    public Schedule(String name, Day[] hours) {
        this(name);
        setHours(hours);
    }

    public void setBusinessHours(int dow, Day day) {
        days[dow] = day;
    }

    public Day getBusinessHours(int dow) {
        return days[dow];
    }

    public Day getBusinessHours(long date) {
        return getBusinessHours(new Date(date));
    }

    public Day getBusinessHours(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return getBusinessHours(cal);
    }

    public Day getBusinessHours(Calendar date) {
        return getBusinessHours(date.get(Calendar.DAY_OF_WEEK) - 1);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Day[] getHours() {
        return days;
    }

    public void setHours(Day[] hours) {
        this.days = hours;
    }

    public String toString() {
        StringBuilder ret = new StringBuilder("Schedule(" + name + ") ");
        for (int idx = 0; idx < days.length; idx++) {
            if (idx > 0) {
                ret.append(',');
            }
            ret.append(days[idx].toString());
        }
        return ret.toString();
    }

    public Token toXml() {
        Token ret = new Token(TOKEN_SCHEDULE);
        ret.addChild(new Token(TOKEN_NAME, getName()));
        for (int idx = 0; idx < days.length; idx++) {
            Token dow = new Token(DAY_OF_WEEK[idx]);
            ret.addChild(dow);
            days[idx].toXml(dow);
        }
        return ret;
    }

    public void fromXml(Token token) {
        Token tmp = token.getChild(TOKEN_NAME);
        if (tmp != null) {
            setName(tmp.getValue());
        }
        days = new Day[DAY_OF_WEEK.length];
        for (int idx = 0; idx < days.length; idx++) {
            days[idx] = new Day();
            Token dow = token.getChild(DAY_OF_WEEK[idx]);
            if (dow != null) {
                days[idx].fromXml(dow);
            }
        }
    }
}
