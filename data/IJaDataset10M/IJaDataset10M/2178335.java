package ow.tool.mrouted;

import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import ow.id.ID;
import ow.id.IDAddressPair;
import ow.mcast.SpanningTreeChangedCallback;
import ow.messaging.MessagingAddress;

final class OverlayGroupObserver implements SpanningTreeChangedCallback {

    private static Logger logger = Logger.getLogger("mrouted");

    private final ApplicationLevelMulticastRouterConfiguration config;

    private final GroupTable groupTable;

    OverlayGroupObserver(ApplicationLevelMulticastRouterConfiguration config, GroupTable groupTable) {
        this.config = config;
        this.groupTable = groupTable;
    }

    /**
	 * Implements
	 * {@link SpanningTreeChangedCallback#topologyChanged(ID, IDAddressPair, IDAddressPair[])
	 * SpanningTreeChangedCallback#topology()}.
	 */
    public void topologyChanged(ID groupID, IDAddressPair parent, IDAddressPair[] children) {
        OverlayTrafficForwarder forwarder;
        if (parent == null && (children == null || children.length <= 0)) {
            forwarder = this.groupTable.unregisterOverlayTrafficForwarder(groupID);
        } else {
            MessagingAddress msgAddr;
            ForwarderAddress parentAddr = null;
            ForwarderAddress[] childrenAddr = null;
            if (parent != null) {
                msgAddr = parent.getAddress();
                try {
                    parentAddr = new ForwarderAddress(msgAddr.getHostAddress(), msgAddr.getPort() + config.getPortDiffFromMcast());
                } catch (UnknownHostException e) {
                    logger.log(Level.WARNING, "Could not resolve: " + msgAddr.getHostAddress(), e);
                }
            }
            if (children != null) {
                childrenAddr = new ForwarderAddress[children.length];
                for (int i = 0; i < children.length; i++) {
                    msgAddr = children[i].getAddress();
                    try {
                        childrenAddr[i] = new ForwarderAddress(msgAddr.getHostAddress(), msgAddr.getPort() + config.getPortDiffFromMcast());
                    } catch (UnknownHostException e) {
                        logger.log(Level.WARNING, "Could not resolve: " + msgAddr.getHostAddress(), e);
                    }
                }
            }
            forwarder = this.groupTable.registerOverlayTrafficForwarder(groupID);
            forwarder.setParent(parentAddr);
            forwarder.setChildren(childrenAddr);
        }
    }
}
