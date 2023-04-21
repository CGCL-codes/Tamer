package gov.sns.services.tripmonitor;

import java.util.Date;
import java.util.Comparator;
import gov.sns.ca.Timestamp;

/** record of a trip */
public class TripRecord {

    /** PV which indicates the trip */
    protected final String PV;

    /** the timestamp of the trip */
    protected final Timestamp TIME_STAMP;

    /** number of trips accumulated */
    final int TRIP_COUNT;

    /** Primary Constructor */
    public TripRecord(final String pv, final Timestamp timeStamp, final int tripCount) {
        PV = pv;
        TIME_STAMP = timeStamp;
        TRIP_COUNT = tripCount;
    }

    /** Constructor */
    public TripRecord(final String pv, final java.sql.Timestamp sqlTimestamp) {
        this(pv, new Timestamp(sqlTimestamp), -1);
    }

    /** get the trip record from the record map */
    public static TripRecord getInstanceFromRecordMap(final java.util.Map recordMap) {
        final String pv = (String) recordMap.get(TripMonitorPortal.PV_KEY);
        final long time = ((Date) recordMap.get(TripMonitorPortal.TIMESTAMP_KEY)).getTime();
        return new TripRecord(pv, new java.sql.Timestamp(time));
    }

    /** Get the PV */
    public String getPV() {
        return PV;
    }

    /** Get the time stamp */
    public Timestamp getTimeStamp() {
        return TIME_STAMP;
    }

    /** Get the time stamp */
    public Date getDate() {
        return TIME_STAMP.getDate();
    }

    /** Get the time stamp */
    public java.sql.Timestamp getSQLTimestamp() {
        return TIME_STAMP.getSQLTimestamp();
    }

    /** Get the number of trips accumulated */
    public int getTripCount() {
        return TRIP_COUNT;
    }

    /** get a description of this record */
    @Override
    public String toString() {
        return "PV:  " + PV + ", Timestamp:  " + TIME_STAMP + ", Trip Count:  " + TRIP_COUNT;
    }

    /** determine if the specified trip record is equal to this one */
    @Override
    public boolean equals(final Object tripRecord) {
        if (tripRecord != null && tripRecord instanceof TripRecord) {
            final TripRecord record = (TripRecord) tripRecord;
            return record.TIME_STAMP.equals(TIME_STAMP) && record.PV.equals(PV);
        } else {
            return false;
        }
    }

    /** get the timestamp based comparator */
    public static Comparator<TripRecord> timestampComparator() {
        return new Comparator<TripRecord>() {

            public int compare(final TripRecord record1, final TripRecord record2) {
                return record1.TIME_STAMP.compareTo(record2.TIME_STAMP);
            }

            @Override
            public boolean equals(final Object comparator) {
                return this == comparator;
            }
        };
    }

    /** get the PV based comparator */
    public static Comparator<TripRecord> pvComparator() {
        return new Comparator<TripRecord>() {

            /** compare first by PV and then by time stamp */
            public int compare(final TripRecord record1, final TripRecord record2) {
                final int result = record1.PV.compareTo(record2.PV);
                return result != 0 ? result : record1.TIME_STAMP.compareTo(record2.TIME_STAMP);
            }

            @Override
            public boolean equals(final Object comparator) {
                return this == comparator;
            }
        };
    }
}
