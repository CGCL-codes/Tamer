package org.omg.DsObservationAccess;

/**
 * Exception definition: MaxConnectionsExceeded.
 * 
 * @author OpenORB Compiler
 */
public final class MaxConnectionsExceeded extends org.omg.CORBA.UserException {

    /**
     * Exception member max_connections
     */
    public int max_connections;

    /**
     * Default constructor
     */
    public MaxConnectionsExceeded() {
        super(MaxConnectionsExceededHelper.id());
    }

    /**
     * Constructor with fields initialization
     * @param max_connections max_connections exception member
     */
    public MaxConnectionsExceeded(int max_connections) {
        super(MaxConnectionsExceededHelper.id());
        this.max_connections = max_connections;
    }

    /**
     * Full constructor with fields initialization
     * @param max_connections max_connections exception member
     */
    public MaxConnectionsExceeded(String orb_reason, int max_connections) {
        super(MaxConnectionsExceededHelper.id() + " " + orb_reason);
        this.max_connections = max_connections;
    }
}
