package peerApplications.FdnGuiConnector;

import java.io.Serializable;
import peer.Peer;

public class FdnGuiConnectorData implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 666L;

    public Peer self;

    public Peer lower;

    public Peer upper;

    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof FdnGuiConnectorData)) return false;
        return (((FdnGuiConnectorData) other).self.equals(this.self) && ((FdnGuiConnectorData) other).lower.equals(this.lower) && ((FdnGuiConnectorData) other).upper.equals(this.upper));
    }
}
