package com.angel.email.connection;

/**
 *
 * @author William
 *
 */
public class FullEmailConnection {

    private IncomingEmailConnection incomingEmailConnection;

    private OutgoingEmailConnection outgoingEmailConnection;

    public FullEmailConnection(IncomingEmailConnection incomingEmailConnection, OutgoingEmailConnection outgoingEmailConnection) {
        super();
        this.setIncomingEmailConnection(incomingEmailConnection);
        this.setOutgoingEmailConnection(outgoingEmailConnection);
    }

    /**
	 * @return the incomingEmailConnection
	 */
    public IncomingEmailConnection getIncomingEmailConnection() {
        return incomingEmailConnection;
    }

    /**
	 * @param incomingEmailConnection the incomingEmailConnection to set
	 */
    public void setIncomingEmailConnection(IncomingEmailConnection incomingEmailConnection) {
        this.incomingEmailConnection = incomingEmailConnection;
    }

    /**
	 * @return the outgoingEmailConnection
	 */
    public OutgoingEmailConnection getOutgoingEmailConnection() {
        return outgoingEmailConnection;
    }

    /**
	 * @param outgoingEmailConnection the outgoingEmailConnection to set
	 */
    public void setOutgoingEmailConnection(OutgoingEmailConnection outgoingEmailConnection) {
        this.outgoingEmailConnection = outgoingEmailConnection;
    }
}
