package ch.ethz.dcg.spamato.peerato.common.msg;

import java.io.Serializable;
import ch.ethz.dcg.spamato.peerato.common.msg.data.SharedFileInfo;

/**
 * Contains information about a peer list request.
 * This includes the <code>fileInfos</code> the peer is interested in, and his port number.
 * A peer sending a PeerListRequest to the tracker gets a PeerListAnswer from the tracker
 * containing contact information of other peers interested in the same files.
 * <code>fileInfos</code> contains SharedFileInfos
 * about all files the peer shares or wants to download.
 * 
 * @author Michelle Ackermann 
 */
public class PeerListRequestMessage implements Serializable, Message {

    private SharedFileInfo[] fileInfos;

    private boolean refresh;

    private int portNumber;

    public PeerListRequestMessage() {
    }

    /**
	 * Creates a PeerListRequestMessage from the specified <code>fileInfos</code> and port number.
	 * @param fileInfos infos of the shared files
	 * @param portNumber port number where the peer can be contacted by other peers
	 */
    public PeerListRequestMessage(SharedFileInfo[] fileInfos, int portNumber, boolean refresh) {
        this.fileInfos = fileInfos;
        this.portNumber = portNumber;
        this.refresh = refresh;
    }

    public SharedFileInfo[] getFileInfos() {
        return fileInfos;
    }

    public int getPortNumber() {
        return portNumber;
    }

    public boolean isRefresh() {
        return refresh;
    }
}
