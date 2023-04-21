package proper.gui.core.event;

import proper.database.Connector;
import java.util.EventObject;

/**
* This Event is fired by a component that handles a Connector object and the
* the state of this connector is changed. Useful if one Connector object
* is shared between different components in order to update their states.
*
*
* @see         proper.database.Connector
* @see         ConnectorChangeListener
* @author      FracPete
* @version $Revision: 1.2 $
*/
public class ConnectorChangeEvent extends EventObject {

    private Connector conn;

    /**
   * creates an event with the given Connector
   */
    public ConnectorChangeEvent(Object source, Connector conn) {
        super(source);
        this.conn = conn;
    }

    /**
   * returns the Connector instance
   */
    public Connector getConnector() {
        return conn;
    }
}
