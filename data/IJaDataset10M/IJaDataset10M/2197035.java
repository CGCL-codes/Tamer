package org.rapla.entities.domain;

import java.util.Date;

/** Encapsulates the repeating rule for an appointment.
    @see Appointment*/
public interface Repeating {

    RepeatingType DAILY = RepeatingType.DAILY;

    RepeatingType WEEKLY = RepeatingType.WEEKLY;

    RepeatingType MONTHLY = RepeatingType.MONTHLY;

    RepeatingType YEARLY = RepeatingType.YEARLY;

    void setInterval(int interval);

    /** returns the number of intervals between two repeatings.
     * That are in the selected context:
     * <li>For weekly repeatings: Number of weeks.</li>
     * <li>For dayly repeatings: Number of days.</li>
     */
    int getInterval();

    /** The value returned depends which method was called last.
     *  If <code>setNumber()</code> has been called with a parameter
     *  &gt;=0 <code>fixedNumber()</code> will return true. If
     *  <code>setEnd()</code> has been called 
     *  <code>fixedNumber()</code> will return false.
     *  @see #setEnd
     *  @see #setNumber
     */
    boolean isFixedNumber();

    /** Set the end of repeating.
     *  If this value is set to null and the
     *  number is set to -1 the appointment will repeat
     *  forever.
     *  @param end If not null isFixedNumber will return true.
     *  @see #setNumber
     */
    void setEnd(Date end);

    Date getEnd();

    /** Set a fixed number of repeating.
     * If this value is set to -1
     * and the repeating end is set to null the appointment will
     * repeat forever.
     *  @param number If &gt;=0 isFixedNumber will return true.
     *  @see #setEnd
     *  @see #isFixedNumber
    */
    void setNumber(int number);

    int getNumber();

    RepeatingType getType();

    void setType(RepeatingType type);

    Date[] getExceptions();

    boolean hasExceptions();

    boolean isWeekly();

    boolean isDaily();

    boolean isMonthly();

    boolean isYearly();

    void addException(Date date);

    void removeException(Date date);

    void clearExceptions();

    /** returns the appointment of this repeating.
        @see Appointment
     */
    Appointment getAppointment();

    boolean isException(long date);

    Object clone();
}
