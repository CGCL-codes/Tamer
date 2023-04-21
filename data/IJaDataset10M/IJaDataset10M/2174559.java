package connections;

import saveCCM.From;
import saveCCM.To;
import controlFlow.Ctrl_Type;

/**
 * Class for a simple connection. A simple connection origins in exactly one port
 * and has exactly one destination.
 * 
 * @author Kathrin Dannmann
 * @version 1.0
 */
public class SimpleConnection extends Connection {

    From from;

    To to;

    /**
	 * Creates a new SimpleConnection.
	 */
    public SimpleConnection() {
        this.connector = Ctrl_Type.SIMPLE;
    }

    /**
	 * Creates a new SimpleConnection with the specified
	 * origin and destination.
	 * @param org the origin of the new connection
	 * @param dest the destination of the new connection
	 */
    public SimpleConnection(From org, To dest) {
        this.connector = Ctrl_Type.SIMPLE;
        this.from = org;
        this.to = dest;
    }

    public From getFrom() {
        return from;
    }

    /**
	 * Sets the from.
	 * @param from new origin
	 */
    public void setFrom(From from) {
        this.from = from;
    }

    public To getTo() {
        return to;
    }

    /**
	 * Sets the to.
	 * @param to new destination
	 */
    public void setTo(To to) {
        this.to = to;
    }
}
