package com.ivis.xprocess.core;

import com.ivis.xprocess.framework.annotations.Element;
import com.ivis.xprocess.util.Day;

/**
 * Gives access to and manages the AvailabilityRecords for the Party
 *
 */
@Element(designator = "PV")
public interface PartyAvailability extends com.ivis.xprocess.framework.Xelement {

    public static final String AVAILABILITY_RECORDS = "AVAILABILITY_RECORDS";

    /**
     * @return the owner of this PartyAvailability delegate
     */
    public Party getParty();

    /**
     * @param day
     * @return in minutes the availability for a specific day. If no records exists it returns 0
     */
    public int getAvailability(Day day);

    /**
     * @param from
     * @param to
     * @return an array of minutes for each day in the range, or empty if there are no records in the day range
     */
    public int[] getAvailability(Day from, Day to);

    /**
     * @param day
     * @return the AvailabilityRecord for the specific day, or null if a record does not exist for that day
     */
    public AvailabilityRecord getAvailabilityRecord(Day day);

    /**
     * @return an unmodifiable array containing every AvailabilityRecord. Will be empty if there are no records
     */
    public AvailabilityRecord[] getAvailabilityRecords();
}
