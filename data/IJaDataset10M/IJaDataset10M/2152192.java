package org.actioncenters.udm.data;

import java.sql.Timestamp;

/**
 * A utility class providing time stamps.
 * 
 * @author dougk
 */
public class TransactionTimestamp {

    /**
     * Prevent instantiation of this class.
     */
    private TransactionTimestamp() {
    }

    /** A time stamp as a local variable to this thread. */
    private static ThreadLocal<Timestamp> timestamp = new ThreadLocal<Timestamp>() {

        @Override
        protected Timestamp initialValue() {
            return new Timestamp(System.currentTimeMillis());
        }
    };

    /**
     * Resets the time stamp to the current time.
     */
    public static void resetTimestamp() {
        timestamp.set(new Timestamp(System.currentTimeMillis()));
    }

    /**
     * Gets the current time stamp value.
     * 
     * @return the time stamp
     */
    public static Timestamp getTimestamp() {
        return timestamp.get();
    }
}
