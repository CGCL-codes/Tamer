package eg.nileu.cis.nilestore.immutable.peertracker.port;

import java.util.Map;
import java.util.Set;
import se.sics.kompics.Event;
import eg.nileu.cis.nilestore.common.ComponentAddress;
import eg.nileu.cis.nilestore.common.NilestoreAddress;
import eg.nileu.cis.nilestore.common.Status;

/**
 * The Class QueryResponse.
 * 
 * @author Mahmoud Ismail <mahmoudahmedismail@gmail.com>
 */
public class QueryResponse extends Event {

    /** The status. */
    private final Status status;

    /** The peer tracker id. */
    private final String peerTrackerId;

    /** The server address. */
    private final NilestoreAddress serverAddress;

    /** The sharemap. */
    private final Map<Integer, ComponentAddress> sharemap;

    /** The alreadygot. */
    private final Set<Integer> alreadygot;

    /**
	 * Instantiates a new query response.
	 * 
	 * @param status
	 *            the status
	 * @param peerTrackerId
	 *            the peer tracker id
	 * @param serverAddress
	 *            the server address
	 * @param sharemap
	 *            the sharemap
	 * @param alreadygot
	 *            the alreadygot
	 */
    public QueryResponse(Status status, String peerTrackerId, NilestoreAddress serverAddress, Map<Integer, ComponentAddress> sharemap, Set<Integer> alreadygot) {
        this.status = status;
        this.sharemap = sharemap;
        this.serverAddress = serverAddress;
        this.peerTrackerId = peerTrackerId;
        this.alreadygot = alreadygot;
    }

    /**
	 * Gets the status.
	 * 
	 * @return the status
	 */
    public Status getStatus() {
        return status;
    }

    /**
	 * Gets the peer tracker id.
	 * 
	 * @return the peer tracker id
	 */
    public String getPeerTrackerId() {
        return peerTrackerId;
    }

    /**
	 * Gets the server address.
	 * 
	 * @return the server address
	 */
    public NilestoreAddress getServerAddress() {
        return serverAddress;
    }

    /**
	 * Gets the sharemap.
	 * 
	 * @return the sharemap
	 */
    public Map<Integer, ComponentAddress> getSharemap() {
        return sharemap;
    }

    /**
	 * Gets the alreadygot.
	 * 
	 * @return the alreadygot
	 */
    public Set<Integer> getAlreadygot() {
        return alreadygot;
    }
}
