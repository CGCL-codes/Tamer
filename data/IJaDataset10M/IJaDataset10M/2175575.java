package com.ivis.xprocess.core;

import com.ivis.xprocess.framework.Xrecord;
import com.ivis.xprocess.framework.annotations.Property;
import com.ivis.xprocess.framework.annotations.RecordKey;
import com.ivis.xprocess.framework.properties.PropertyType;
import com.ivis.xprocess.util.Day;

/**
 * Effort booked in the plan against a particular RequiredResource on a given
 * day. "Booked" is a term that used generally for the action of putting time in
 * the schedule.
 *
 * There are two questions we can ask about booked effort: who booked it? has it
 * been confirmed?
 *
 * Booked effort is displayed to the user as: - auto (black, italic) - effort
 * xProcess booked for someone - manual (blue, normal) - effort manually booked
 * for someone - confirmed (green, bold) - the actual effort that was done
 *
 * Auto: isAutoScheduled() - effort scheduled by xProcess. - cannot be
 * confirmed. If auto, we can assert that (isConfirmed() == false) Manual:
 * !isAutoScheduled() - effort manually scheduled by a project participant
 *
 * Confirmed: isConfirmed() - effort today or in past that has been confirmed by
 * a participant.. - must be manual. If confirmed, we can assert that
 * (isAutoScheduled() == false) Unconfirmed/Planned: !isConfirmed() - any effort
 * that is not confirmed!! Could be manual or auto. - all effort in the schedule
 * is considered unconfirmed unless confirmed. This is why we treat "Planned" as
 * the opposite of confirmed, whether planned by xProcess or a project
 * participant.
 *
 */
@com.ivis.xprocess.framework.annotations.Record(designator = "DAILY_RECORD")
public interface DailyRecord extends Xrecord {

    /**
     * Property name for Day
     */
    @RecordKey(index = 0)
    public static final String DAY = "DAY";

    /**
     * Property name for Time
     */
    public static final String TIME = "TIME";

    /**
     * Property name for Log Entry
     */
    public static final String LOG_ENTRY = "LOG_ENTRY";

    /**
     * Property name for Auto Scheduled
     */
    public static final String AUTO_SCHEDULED = "AUTO_SCHEDULED";

    /**
     * Property name for Confirmed
     */
    public static final String CONFIRMED = "CONFIRMED";

    /**
     * Property name for Assignment
     */
    public static final String ASSIGNMENT = "ASSIGNMENT";

    /**
     * @return the day for which the DailyRecord is based on
     */
    @Property(name = DAY, propertyType = PropertyType.DAY)
    public Day getDay();

    /**
     * @return the time booked on the DailyRecord in minutes
     */
    @Property(name = TIME, propertyType = PropertyType.INTEGER)
    public int getTime();

    @Property(name = LOG_ENTRY, propertyType = PropertyType.STRING)
    public String getLogEntry();

    /**
     * Set the log entry, possibly a description of the tasks done etc...
     * @param string
     */
    public void setLogEntry(String string);

    /**
     * @return true if the time booked was by the scheduler,
     * otherwise false
     */
    @Property(name = AUTO_SCHEDULED, propertyType = PropertyType.BOOLEAN)
    public boolean isAutoScheduled();

    /**
     * @return true if the time has been confirmed
     */
    @Property(name = CONFIRMED, propertyType = PropertyType.BOOLEAN)
    public boolean isConfirmed();

    /**
     * @return the assignment associated with the DailyRecord
     */
    @Property(name = ASSIGNMENT, propertyType = PropertyType.REFERENCE)
    public Assignment getAssignment();

    /**
     * @param assignment
     */
    public void setAssignment(Assignment assignment);

    /**
     * @return the task the DailyRecord is for
     */
    @Property(propertyType = PropertyType.REFERENCE)
    public Xtask getTask();
}
