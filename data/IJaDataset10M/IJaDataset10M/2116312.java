package com.ivis.xprocess.core;

import com.ivis.xprocess.framework.Xrecord;
import com.ivis.xprocess.framework.annotations.Property;
import com.ivis.xprocess.framework.annotations.RecordKey;
import com.ivis.xprocess.framework.properties.PropertyType;
import com.ivis.xprocess.util.Day;

/**
 * Defines a Person's availability to a Project over a period of time.
 * Availability is defined as a percentage - this being a proportion of
 * the Person's overall availability, i.e. a proportion of their PartyAvailability.
 *
 */
@com.ivis.xprocess.framework.annotations.Record(designator = "PERIOD_OF_APPOINTMENT")
public interface PeriodOfAppointment extends Xrecord, Comparable<PeriodOfAppointment> {

    @RecordKey(index = 0)
    public static final String FROM = "FROM";

    @RecordKey(index = 1)
    public static final String TO = "TO";

    public static final String MONDAY_PERCENT = "MONDAY_PERCENT";

    public static final String TUESDAY_PERCENT = "TUESDAY_PERCENT";

    public static final String WEDNESDAY_PERCENT = "WEDNESDAY_PERCENT";

    public static final String THURSDAY_PERCENT = "THURSDAY_PERCENT";

    public static final String FRIDAY_PERCENT = "FRIDAY_PERCENT";

    public static final String SATURDAY_PERCENT = "SATURDAY_PERCENT";

    public static final String SUNDAY_PERCENT = "SUNDAY_PERCENT";

    /**
     * @directed
     * @supplierCardinality 1..*
     */
    @Property(name = FROM, propertyType = PropertyType.DAY)
    Day getFrom();

    @Property(name = TO, propertyType = PropertyType.DAY)
    Day getTo();

    @Property(name = MONDAY_PERCENT, propertyType = PropertyType.FLOAT)
    double getMondayPercent();

    @Property(name = TUESDAY_PERCENT, propertyType = PropertyType.FLOAT)
    double getTuesdayPercent();

    @Property(name = WEDNESDAY_PERCENT, propertyType = PropertyType.FLOAT)
    double getWednesdayPercent();

    @Property(name = THURSDAY_PERCENT, propertyType = PropertyType.FLOAT)
    double getThursdayPercent();

    @Property(name = FRIDAY_PERCENT, propertyType = PropertyType.FLOAT)
    double getFridayPercent();

    @Property(name = SATURDAY_PERCENT, propertyType = PropertyType.FLOAT)
    double getSaturdayPercent();

    @Property(name = SUNDAY_PERCENT, propertyType = PropertyType.FLOAT)
    double getSundayPercent();

    /**
     * Let this method find the appropriate percentage for the day of the week
     *
     * @param day
     * @return the percentage for that day of the week
     */
    public double getPercentageFor(Day.DayOfWeek dayOfTheWeek);

    /**
     * Does this period of appointment cover this day?
     *
     * @param day
     * @return true if the day falls in the range of this appointment, otherwise false
     */
    boolean covers(Day day);

    public int compareTo(PeriodOfAppointment o);
}
