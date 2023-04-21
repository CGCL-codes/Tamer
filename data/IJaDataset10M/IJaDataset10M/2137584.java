package net.sourceforge.jpalm.palmdb;

import net.sourceforge.juint.UInt32;

/**
 * A date in a Palm database.
 */
public class Date {

    /**
     * The Palm's epoch (January 1, 1904) relative to Java's epoch (January 1, 1970) in
     * milliseconds.<br>
     * {@value}
     */
    public static final long PALM_EPOCH_MILLISECONDS = -2082844800000L;

    private UInt32 seconds;

    /**
     * Creates a new date from the number of seconds since the Palm epoch.
     * 
     * @param seconds
     *            the number of seconds since the Palm epoch
     */
    public Date(UInt32 seconds) {
        this.seconds = seconds;
    }

    /**
     * Creates a new date from a java date.
     * 
     * @param date
     *            a date
     */
    public Date(java.util.Date date) {
        this.seconds = convertDate(date);
    }

    /**
     * Creates a new date from the current date.
     */
    public Date() {
        this(new java.util.Date());
    }

    /**
     * Gets the of seconds since the Palm epoch.
     * 
     * @return the number of seconds since the Palm epoch
     */
    public UInt32 getSeconds() {
        return seconds;
    }

    /**
     * Gets the java date representation of this date.
     * 
     * @return the java date
     */
    public java.util.Date getDate() {
        return convertDate(seconds);
    }

    private UInt32 convertDate(java.util.Date date) {
        return new UInt32((date.getTime() - PALM_EPOCH_MILLISECONDS) / 1000L);
    }

    private java.util.Date convertDate(UInt32 seconds) {
        return new java.util.Date((seconds.uint32Value() * 1000L) + PALM_EPOCH_MILLISECONDS);
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Date)) return false;
        return getSeconds().equals(((Date) object).getSeconds());
    }
}
