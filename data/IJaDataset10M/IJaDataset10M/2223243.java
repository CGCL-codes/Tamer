package com.margiel.rozkladowka.model;

import java.util.Map;
import java.util.TreeMap;
import com.margiel.rozkladowka.costs.Day;

public class Stop {

    private String name;

    private String link;

    private Map<Number, String[]> hours;

    private Map<Number, String[]> hoursHoliday;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Stop(String name, String link) {
        super();
        this.name = name;
        this.link = link;
        this.hours = new TreeMap<Number, String[]>();
        this.hoursHoliday = new TreeMap<Number, String[]>();
    }

    public void addTime(Day dayType, Number hour, String... minutes) {
        getHours(dayType).put(hour, minutes);
    }

    @Override
    public int hashCode() {
        return (name != null ? name.hashCode() : 0) + (link != null ? link.hashCode() : 0);
    }

    /**
	 * Constructs a <code>String</code> with all attributes
	 * in name = value format.
	 *
	 * @return a <code>String</code> representation 
	 * of this object.
	 */
    public String toString() {
        final String TAB = "]\n    ";
        final String END = "]\n";
        String retValue = "";
        retValue = "Stop ( " + "name = " + this.name + TAB + "link = " + this.link + TAB + "hours = " + this.hours + TAB + "hoursHoliday = " + this.hoursHoliday + TAB + " )";
        return retValue;
    }

    private Map<Number, String[]> getHours(Day dayType) {
        if (dayType == Day.WEEK) {
            return hours;
        } else if (dayType == Day.HOLIDAY) {
            return hoursHoliday;
        }
        return null;
    }
}
