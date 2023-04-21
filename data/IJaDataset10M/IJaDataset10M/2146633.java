package org.promotego.logic.storehours;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TimeZone;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.IntRange;

/**
 * A representation of times a store is open and closed.
 * 
 * The StoreHours object is mapped to and from a simple language for
 * purposes of mapping the object to a persistent store.  The format
 * is as follows:
 * 
 * MON 8-19;TUE-FRI 7-21;SAT-SUN 10-19
 * 
 * @author alf
 */
public class StoreHours {

    private static Map<String, Integer> s_dayMapping;

    private static int SECONDS_IN_DAY = 24 * 3600;

    static int SECONDS_IN_WEEK = 7 * SECONDS_IN_DAY;

    private WeeklyHours m_hours;

    private Set<HourSpecification> m_hourSpecs;

    static {
        s_dayMapping = new HashMap<String, Integer>();
        s_dayMapping.put("SUN", 0);
        s_dayMapping.put("MON", 1);
        s_dayMapping.put("TUE", 2);
        s_dayMapping.put("WED", 3);
        s_dayMapping.put("THU", 4);
        s_dayMapping.put("FRI", 5);
        s_dayMapping.put("SAT", 6);
    }

    public StoreHours() {
        m_hours = new WeeklyHours();
    }

    public StoreHours(String hourString) {
        this();
        setHours(hourString);
    }

    public StoreHours(Collection<HourSpecification> hourSpecs) {
        this();
        m_hourSpecs = new HashSet<HourSpecification>(hourSpecs);
    }

    public boolean isOpen(Date startTime, Date endTime) {
        if (endTime.before(startTime)) {
            throw new IllegalArgumentException("endTime (" + endTime + " ) may not occur before startTime (" + startTime + ").");
        }
        if (endTime.getTime() - startTime.getTime() > SECONDS_IN_DAY * 1000) {
            throw new IllegalArgumentException("Date ranges greater than one day not supported (startTime: " + startTime + " endTime: " + endTime + ")");
        }
        IntRange theRange = getSecondsRange(startTime, endTime);
        return m_hours.isOpen(theRange);
    }

    /**
	 * @param startTime
	 * @param endTime
	 * @return
	 */
    private IntRange getSecondsRange(Date startTime, Date endTime) {
        if (startTime.after(endTime)) {
            throw new IllegalArgumentException("startTime (" + startTime + ") may not occur after endTime (" + endTime + ")");
        }
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("America/Los_Angeles"));
        int startSeconds = getSecondsInWeek(startTime, calendar);
        int endSeconds = getSecondsInWeek(endTime, calendar);
        if (endSeconds < startSeconds) {
            endSeconds += SECONDS_IN_WEEK;
        }
        return new IntRange(startSeconds, endSeconds);
    }

    /**
	 * @param time
	 * @param calendar
	 */
    private int getSecondsInWeek(Date time, Calendar calendar) {
        calendar.setTime(time);
        int day = (calendar.get(Calendar.DAY_OF_WEEK) - Calendar.SUNDAY + 7) % 7;
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);
        return day * SECONDS_IN_DAY + 3600 * hours + 60 * minutes;
    }

    @Override
    public String toString() {
        StringBuilder retval = new StringBuilder();
        List<HourSpecification> theList = new ArrayList<HourSpecification>(m_hourSpecs);
        Collections.sort(theList, new HourSpecificationComparator());
        for (HourSpecification thisSpec : theList) {
            if (retval.length() != 0) {
                retval.append(";");
            }
            retval.append(thisSpec.toString());
        }
        return retval.toString();
    }

    public void setHours(String hourString) {
        m_hours = new WeeklyHours();
        m_hourSpecs = new HashSet<HourSpecification>();
        StringTokenizer strtok = new StringTokenizer(hourString, ";");
        while (strtok.hasMoreTokens()) {
            String thisToken = StringUtils.strip(strtok.nextToken());
            HourSpecification theSpec = new HourSpecification(thisToken);
            m_hourSpecs.add(theSpec);
            for (Day day : theSpec.getDays()) {
                for (IntRange range : theSpec.getSecondRanges()) {
                    m_hours.addRange(new IntRange(range.getMinimumInteger() + day.ordinal() * SECONDS_IN_DAY, range.getMaximumInteger() + day.ordinal() * SECONDS_IN_DAY));
                }
            }
        }
        coalesceHourSpecs();
    }

    private void coalesceHourSpecs() {
        List<HourSpecification> theList = new ArrayList<HourSpecification>(m_hourSpecs);
        for (int i = 0; i < theList.size(); i++) {
            for (int j = i + 1; j < theList.size(); j++) {
                HourSpecification spec1 = theList.get(i);
                HourSpecification spec2 = theList.get(j);
                if (spec1.sameDays(spec2)) {
                    assert j > i : "j must be greater than i";
                    theList.remove(j);
                    theList.remove(i);
                    theList.add(i, spec1.mergeWith(spec2));
                    j--;
                }
            }
        }
        for (int i = 0; i < theList.size(); i++) {
            for (int j = i + 1; j < theList.size(); j++) {
                HourSpecification spec1 = theList.get(i);
                HourSpecification spec2 = theList.get(j);
                if (spec1.sameHours(spec2)) {
                    assert j > i : "j must be greater than i";
                    theList.remove(j);
                    theList.remove(i);
                    theList.add(i, spec1.mergeWith(spec2));
                    j--;
                }
            }
        }
        Set<HourSpecification> newSet = new HashSet<HourSpecification>();
        newSet.addAll(theList);
        m_hourSpecs = newSet;
    }

    public Set<HourSpecification> getHourSpecifications() {
        return new HashSet<HourSpecification>(m_hourSpecs);
    }

    @Override
    public boolean equals(Object anotherObject) {
        if (anotherObject == null || !(anotherObject instanceof StoreHours)) {
            return false;
        }
        StoreHours otherHours = (StoreHours) anotherObject;
        return m_hours.equals(otherHours.m_hours);
    }

    @Override
    public int hashCode() {
        return m_hours.hashCode();
    }
}
