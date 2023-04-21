package org.chargecar.serial.streaming;

import java.util.Date;

/**
 * @author Chris Bartley (bartley@cmu.edu)
 */
public abstract class BaseStreamingSerialPortEvent {

    protected static final String TO_STRING_DELIMITER = "\t";

    private final Date timestamp;

    protected BaseStreamingSerialPortEvent(final Date timestamp) {
        this.timestamp = timestamp;
    }

    public final long getTimestampMilliseconds() {
        return timestamp.getTime();
    }

    public abstract String toLoggingString();

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final BaseStreamingSerialPortEvent that = (BaseStreamingSerialPortEvent) o;
        if (timestamp != null ? !timestamp.equals(that.timestamp) : that.timestamp != null) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return timestamp != null ? timestamp.hashCode() : 0;
    }
}
