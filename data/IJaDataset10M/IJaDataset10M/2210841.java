package org.tanso.fountain.router.routingupdate;

import java.io.Serializable;
import org.tanso.fountain.interfaces.func.comm.IMessage;

/**
 * The class wraps the RouitngContent and the forward source-node-id
 * @author SongHuanhuan
 *
 */
public class RoutingMessage implements Serializable, IMessage {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    /**
	 * routingInfo: the routing info which will be routed.
	 * sourceNodeID:the souce node ID which forward the routing info.
	 * time: the send time.
	 */
    private RoutingInfo routingInfo;

    private String sourceNodeID;

    private int time = 0;

    /**
	 * get the routing info
	 * @return routing info
	 */
    public RoutingInfo getRoutingInfo() {
        return routingInfo;
    }

    /**
	 * get the source node ID
	 * @return source node ID
	 */
    public String getSourceNodeID() {
        return sourceNodeID;
    }

    /**
	 * set the routing info
	 * @param routingInfo
	 */
    public void setRoutingInfo(RoutingInfo routingInfo) {
        this.routingInfo = routingInfo;
    }

    /**
	 * set source node ID
	 * @param sourceNodeID
	 */
    public void setSourceNodeID(String sourceNodeID) {
        this.sourceNodeID = sourceNodeID;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int compareTo(Object o) {
        return 0;
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + time;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        return false;
    }
}
