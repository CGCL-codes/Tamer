package net.sourceforge.jcoupling.bus.server;

import java.io.Serializable;

/**
 * The id of a communicator.
 */
public class CommunicatorID implements Serializable {

    private static Long _idCounter = 0L;

    private String _myID;

    /**
     * Creates a new Receiver id.
     */
    protected CommunicatorID() {
        _myID = "C" + _idCounter++;
    }

    /**
     * Creates a communicator object from a previously existing one.
     * @param myID
     */
    protected CommunicatorID(String myID) {
        _myID = myID;
    }

    /**
     * Returns the string value of the id.
     * @return the id.
     */
    public String toString() {
        return _myID;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CommunicatorID that = (CommunicatorID) o;
        return !(_myID != null ? !_myID.equals(that._myID) : that._myID != null);
    }

    public int hashCode() {
        return _myID.hashCode();
    }
}
