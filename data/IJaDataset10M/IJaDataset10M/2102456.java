package info.metlos.jdc.nmdc.messages;

import info.metlos.jdc.common.MessageDirection;
import info.metlos.jdc.nmdc.NMDCHub;

/**
 * Abstract base for the NMDC hub messages.
 * 
 * @author metlos
 * 
 * @version $Id: AbstractHubMessage.java 231 2008-09-10 23:50:19Z metlos $
 */
public abstract class AbstractHubMessage extends AbstractNMDCMessage {

    protected AbstractHubMessage(NMDCHub hub, MessageDirection direction, byte[] bytes) {
        super(hub, direction, bytes);
    }

    /**
	 * @return the hub
	 */
    public NMDCHub getHub() {
        return (NMDCHub) getRemoteSide();
    }
}
