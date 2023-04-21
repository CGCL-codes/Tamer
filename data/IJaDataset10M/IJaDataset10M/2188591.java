package gov.sns.services.mpstool;

import gov.sns.ca.Timestamp;

/**
 * SignalEvent holds one event for a signal.
 * <p>
 * TODO: Note that the implementation of Comparable breaks the asymmetric rule so we cannot prospect what will happen.
 * 
 * @author    tap
 * @created   April 13, 2004
 */
public class SignalEvent implements Comparable<SignalEvent> {

    /** the event's timestamp */
    protected final Timestamp _timestamp;

    /** the PV name */
    protected final String _signal;

    /**
	 * Constructor
	 *
	 * @param signal     The PV signal
	 * @param timestamp  The timestamp of the event
	 */
    public SignalEvent(String signal, Timestamp timestamp) {
        _timestamp = timestamp;
        _signal = signal;
    }

    /**
	 * Get the signal
	 *
	 * @return   the signal
	 */
    public String getSignal() {
        return _signal;
    }

    /**
	 * Get the timestamp of the signal event
	 *
	 * @return   the timestamp of the signal event
	 */
    public Timestamp getTimestamp() {
        return _timestamp;
    }

    /**
	 * compare timestamp of other SignalEvent instance with this one's timestamp
	 *
	 * @param other  the SignalEvent instance against which to compare this one
	 * @return       -1 if this is earlier than the specified record or +1 if it is
	 *      later or the same
	 */
    public int compareTo(SignalEvent other) {
        Timestamp otherTimestamp = other._timestamp;
        return (_timestamp.compareTo(otherTimestamp) < 0) ? -1 : 1;
    }

    /**
	 * Generate a string description of this instance.
	 *
	 * @return   description of this instance
	 */
    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("signal: " + _signal);
        buffer.append(", timestamp: " + _timestamp);
        return buffer.toString();
    }
}
