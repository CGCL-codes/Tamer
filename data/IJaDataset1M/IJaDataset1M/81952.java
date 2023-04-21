package openrpg2.common.core.network;

import java.util.Date;
import openrpg2.common.core.ORPGConstants;
import openrpg2.common.core.ORPGMessage;
import openrpg2.common.module.NetworkedModule;
import openrpg2.common.module.ServerLoadable;

/**
 * The NetworkServerModule is a special server loadable module to provide network information to other modules
 * in a controled and safe manner. This class is loaded into the module manager and provides an API through which
 * network related information can be obtained by other modules and thus keep other modules from relying directly
 * on the network's specific implementation. This module also provides support for message based requests for
 * graceful disconnection of/by a client.
 * @author Snowdog
 */
public class NetworkServerModule extends NetworkedModule implements ServerLoadable {

    private static final String HEADER_OP = "OP";

    private static final int OP_NOOP = 0;

    private static final int OP_DISCONNECT = 1;

    private static final int OP_ALERT = 2;

    private static final int OP_SHUTDOWN = 3;

    private NetworkServer network = null;

    /** Creates a new instance of NetworkServerModule */
    public NetworkServerModule(NetworkServer networkReference) {
        network = networkReference;
    }

    public void doRegistration() {
        modCom.registerMessageType(ORPGConstants.TYPE_NETWORK, this);
    }

    public void processMessage(ORPGMessage msg) {
        int clientId = msg.getOriginatorId();
        int operation = Integer.parseInt(msg.getHeader(HEADER_OP));
        switch(operation) {
            case (OP_NOOP):
                {
                    break;
                }
            case (OP_DISCONNECT):
                {
                    handleDisconnectMessage(msg);
                    break;
                }
            case (OP_ALERT):
                {
                    handleAlertMessage(msg);
                    break;
                }
            case (OP_SHUTDOWN):
                {
                    handleShutdownMessage(msg);
                    break;
                }
            default:
                {
                    break;
                }
        }
    }

    private void handleDisconnectMessage(ORPGMessage msg) {
    }

    private void handleAlertMessage(ORPGMessage msg) {
    }

    private void handleShutdownMessage(ORPGMessage msg) {
    }
}
