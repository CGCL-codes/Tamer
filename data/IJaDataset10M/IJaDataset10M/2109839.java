package com.medcentrex.interfaces;

/**
 * Title: Atin
 * Description: Class to create a time slot for the Appointments.
 * @author Atin
 * @version 1.0
 */
public class TimeSlot {

    public java.sql.Time StartTime;

    public java.sql.Time EndTime;

    public java.sql.Date ApptDate;

    public int location;

    public int physician;

    public int apptType;

    public int duration;

    public boolean isOccupied;

    public TimeSlot(java.sql.Time start, java.sql.Time end) {
        StartTime = start;
        EndTime = end;
        isOccupied = false;
    }

    public TimeSlot() {
        isOccupied = false;
    }

    public long getDurationInMinutes() {
        long diffHour = EndTime.getHours() - StartTime.getHours();
        long diffMin = EndTime.getMinutes() - StartTime.getMinutes();
        long diffSec = EndTime.getSeconds() - StartTime.getSeconds();
        return ((diffHour * 60) + diffMin + diffSec / 60);
    }

    public boolean equals(Object obj) {
        if (obj instanceof TimeSlot) {
            try {
                if ((((TimeSlot) obj).StartTime.equals(this.StartTime) || (this.StartTime.after(((TimeSlot) obj).StartTime) && this.StartTime.before(((TimeSlot) obj).EndTime))) && (((TimeSlot) obj).ApptDate.equals(this.ApptDate))) {
                    return true;
                } else return false;
            } catch (Exception e) {
                return false;
            }
        } else return false;
    }
}
