package com.parfumball.pcap;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Represents a time stamp. The same as a struct timeval.
 *  
 * @author prasanna
 */
public class TimeStamp {

    /**
     * The date format for timestamping dates.
     */
    private static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");

    /**
     * The seconds part of the time stamp.
     */
    private long secs;

    /**
     * The micro-seconds part of the time stamp.
     */
    private long usecs;

    public TimeStamp(long secs, long usecs) {
        this.secs = secs;
        this.usecs = usecs;
    }

    /**
     * @return Returns the secs.
     */
    public long getSecs() {
        return secs;
    }

    /**
     * @return Returns the usecs.
     */
    public long getUsecs() {
        return usecs;
    }

    /**
     * Returns a formatted timestamp string. YYYY-MMM-dd HH:mm:ss
     */
    public String toString() {
        return format.format(new Date(secs * 1000)) + "." + (int) (((double) usecs) / 1000);
    }
}
