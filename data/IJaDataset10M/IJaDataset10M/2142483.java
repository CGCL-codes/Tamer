package jcontrol.eib.extended.ai_layer.rmi;

import java.rmi.Remote;
import jcontrol.eib.extended.ai_layer.AI_OutConnection;

/**
 * Extends the {@link jcontrol.eib.extended.ai_layer.AI_OutConnection}
 * interface to a remote interface, so it can be used per RMI.
 *
 * @author Bjoern Hornburg
 * @version 0.2.0.000
 * @since 0.2.0
 */
public interface AI_OutConnectionRMI extends AI_OutConnection, Remote {

    /**
     * Extends the {@link
     * jcontrol.eib.extended.ai_layer.AI_OutConnection.Listener} interface to
     * a remote interface, so it can be used per RMI.
     *
     * @author Bjoern Hornburg
     * @version 0.2.0.000
     * @since 0.2.0
     */
    public interface Listener extends AI_OutConnection.Listener, Remote {
    }
}
