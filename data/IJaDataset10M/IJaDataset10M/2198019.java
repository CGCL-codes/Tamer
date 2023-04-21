package org.robocup.msl.refbox.communication;

import org.robocup.msl.refbox.CommunicationClient;
import org.robocup.msl.refbox.protocol.Protocol;

public abstract class ReceiveConnectionHandler extends ConnectionHandler {

    ReceiveConnectionHandler(final Protocol protocolToUse, final int portToUse) {
        super(protocolToUse, portToUse);
    }

    /**
     * @return the gameControl
     */
    public CommunicationClient getGameControl() {
        return getProtocol().getGameControl();
    }
}
