package org.sepp.services;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sepp.api.components.SecureSelfOrganizingNetwork;
import org.sepp.config.Configuration;
import org.sepp.config.Group;
import org.sepp.config.Peer;
import org.sepp.datatypes.Members;
import org.sepp.datatypes.Network;
import org.sepp.messages.common.Message;
import org.sepp.messages.management.NetworkRequest;
import org.sepp.messages.peer.NeighborRequest;
import org.sepp.messages.peer.NeighborResponse;
import org.sepp.utils.Constants;

public class PeerService {

    public static final int type = 5000;

    private Log log;

    private long lastQueryNeighbors = System.currentTimeMillis();

    private List neighbors = new ArrayList();

    private List discoveredNetworks = new ArrayList();

    private SecureSelfOrganizingNetwork stm;

    public PeerService(SecureSelfOrganizingNetwork stm) {
        log = LogFactory.getLog(this.getClass());
        this.stm = stm;
        initializeNeigbors(stm.getConfiguration());
    }

    /**
	 * This method processes every incoming management message. Depending on its
	 * type and the current state of the groups it is associated different
	 * actions are performed.
	 * 
	 * @param message
	 *            The {@link Message} encapsulating the actual management
	 *            message.
	 */
    public void processMessage(Message message) {
        if (message instanceof Message) {
            switch(message.getType()) {
                case NeighborRequest.type:
                    processNeighborRequest(message);
                    break;
                case NeighborResponse.type:
                    processNeighborResponse(message);
                    break;
            }
        } else {
            log.debug("Provided parameter wasn't an instance of SeppMessage. Discaring it!");
        }
    }

    /**
	 * Handles the processing of a received {@link NeighborRequest} message. The
	 * received message is verified and the associated measures are taken to
	 * react appropriate to this message.
	 * 
	 * @param message
	 *            The {@link Message} encapsulating the {@link NeighborRequest}
	 *            message.
	 */
    private void processNeighborRequest(Message message) {
        NeighborResponse response = new NeighborResponse(stm.getNetworkId());
        stm.sendMessage(new Message(response, message.getSource()));
    }

    /**
	 * Handles the processing of the received {@link NeighborResponse} message.
	 * The received message is verified and the associated measures are taken to
	 * react appropriate to this message.
	 * 
	 * @param message
	 *            The {@link Message} encapsulating the {@link NeighborResponse}
	 *            message.
	 */
    private void processNeighborResponse(Message message) {
        NeighborResponse response = new NeighborResponse(message);
        if (isNeighbor(message.getSource())) {
            setNeighborStatus(message.getSource(), true);
            stm.setNetworkForNeighbor(message.getSource(), response.getNetworkId());
            log.debug("Neighbor " + message.getSource() + " has it's reachability set to " + true);
        } else {
            Peer peer = message.getSourcePeer();
            addNeighbor(peer);
            setNeighborStatus(message.getSource(), true);
            stm.setNetworkForNeighbor(peer.getId(), response.getNetworkId());
            log.debug("Found a new neighbor and added him to the list.");
        }
        if (!stm.getNetworkId().equalsIgnoreCase(response.getNetworkId())) {
            log.debug("Found another existing SEPP network. Obtaining network info.");
            stm.sendMessage(new Message(new NetworkRequest(), message.getSource()));
        }
    }

    /**
	 * Obtains the neighbors defined in the configuration file and uses them as
	 * current neighbors for this peer if they are reachable. If not they are
	 * discarded and not stored in the current neighbors list.
	 */
    private void initializeNeigbors(Configuration configuration) {
        Group group = configuration.getGroup("BasePeerGroup");
        for (int index = 0; index < group.getNeighborsSize(); index++) {
            addNeighbor(group.getNeighbor(index));
        }
        log.debug("Added " + group.getNeighborsSize() + " peers to the neighbor peer list.");
    }

    /**
	 * Returns the peer IDs of the peers which are currently neighbors of the
	 * local peer. The neighbor peers must not necessarily be direct neighbors
	 * in sense of being in broadcast range. They can also be other members from
	 * this group which comply to certain criteria. There is also an upper limit
	 * on the amount of neighbors.
	 * 
	 * @return A list of peer IDs which are currently neighbors of the local
	 *         peer.
	 */
    public List getNeighbors() {
        return neighbors;
    }

    /**
	 * Returns the a {@link Peer} for the provided peer ID if the associated
	 * peer is a neighbor of the local peer. If not <code>null</code> is
	 * returned.
	 * 
	 * @param peerId
	 *            The peer ID of the associated neighbor.
	 * @return A {@link Peer} object if the provided peer ID is associated with
	 *         a neighbor of the local peer.
	 */
    public Peer getNeighbor(String peerId) {
        for (int index = 0; index < neighbors.size(); index++) {
            Peer peer = (Peer) neighbors.get(index);
            if (peer.getId().equalsIgnoreCase(peerId)) return peer;
        }
        return null;
    }

    /**
	 * Returns the {@link Peer} which is stored at the provided position in the
	 * neighbors list. If no peer is stored at the provided position
	 * <code>null</code> is returned.
	 * 
	 * @param index
	 *            The index of the associated neighbor in the list.
	 * @return A {@link Peer} object if the provided peer ID is associated with
	 *         a neighbor of the local peer.
	 */
    public Peer getNeighbor(int index) {
        synchronized (neighbors) {
            if (index >= 0 && index < neighbors.size()) return (Peer) neighbors.get(index); else return null;
        }
    }

    /**
	 * Adds a {@link Peer} to the list of neighbors. If the maximum amount of
	 * neighbors is already reached this treats the neighbor list like a FIFO
	 * memory. The maximum amount of neighbors can be controlled through the
	 * configuration file.
	 * 
	 * @param neighbor
	 *            The peer which should be added to the neighbors list.
	 */
    public void addNeighbor(Peer neighbor) {
        if (neighbors.size() < Constants.MAX_NEIGHBORS) {
            if (!isNeighbor(neighbor)) {
                neighbors.add(neighbor);
                log.debug("Added peer " + neighbor.getId() + " as neighbor.");
            } else log.debug("Neighbor " + neighbor.getId() + " already in neighbor list!");
        } else {
            log.debug("The maximum amount of neighbors are reached");
        }
    }

    /**
	 * Verifies if the provided peer is a neighbor of the local peer. If yes
	 * true is returned otherwise false. It can be that the provided peer could
	 * be a neighbor if the maximum allowed size of neighbors would allow more
	 * neighbors.
	 * 
	 * @param peer
	 *            The peer which should be verified as neighbor.
	 * @return True if the provided peer is a neighbors false otherwise.
	 */
    public boolean isNeighbor(Peer peer) {
        synchronized (neighbors) {
            for (int index = 0; index < neighbors.size(); index++) {
                if (((Peer) neighbors.get(index)).getId().equalsIgnoreCase(peer.getId())) return true;
            }
            return false;
        }
    }

    /**
	 * Verifies if the provided peer ID is a neighbor of the local peer. If yes
	 * true is returned otherwise false. It can be that the provided peer could
	 * be a neighbor if the maximum allowed size of neighbors would allow more
	 * neighbors.
	 * 
	 * @param peer
	 *            The peer ID which should be verified as neighbor.
	 * @return True if the provided peer is a neighbors false otherwise.
	 */
    public boolean isNeighbor(String peerId) {
        synchronized (neighbors) {
            for (int index = 0; index < neighbors.size(); index++) {
                if (((Peer) neighbors.get(index)).getId().equalsIgnoreCase(peerId)) {
                    return true;
                }
            }
            return false;
        }
    }

    /**
	 * Sets the reachable attribute of the neighbor specified by the peer ID to
	 * the provided value. If no neighbor is associated with the provided peer
	 * ID nothing is done. The reachable attribute is required in order to know
	 * which neighbors are currently available for relaying messages.
	 * 
	 * @param peerId
	 *            The peer ID of the preferred neighbor.
	 * @param reachable
	 *            The value for the reachable attribute which should be set.
	 */
    public synchronized void setNeighborStatus(String peerId, boolean reachable) {
        for (int index = 0; index < neighbors.size(); index++) {
            if (((Peer) neighbors.get(index)).getId().equalsIgnoreCase(peerId)) {
                if (((Peer) neighbors.get(index)).isFixed()) ((Peer) neighbors.get(index)).setReachable(reachable); else if (!reachable) {
                    neighbors.remove(index);
                }
                break;
            }
        }
    }

    /**
	 * Returns the amount of neighbors of this peer as integer. Usually there is
	 * an upper bound on the amount of neighbors which can be modified through
	 * the configuration.
	 * 
	 * @return The amount of direct connection peers stored in the list.
	 */
    public int getNeighborsSize() {
        synchronized (neighbors) {
            return neighbors.size();
        }
    }

    /**
	 * Finds the nearest or most suitable member of this group which is then
	 * used for performing the mutual authentication process in order to join
	 * this group. If direct connection peers exist these are used because the
	 * mutual authentication process should usually be performed using a direct
	 * connection. Otherwise the ID of another member peer is returned although
	 * currently this has no effect.
	 * 
	 * TODO: Currently there is no way to contact a non direct connection peer
	 * for joining the peer group. Either we require an direct connection peer
	 * (can also be obtained via broadcasting or multicasting) or we find an
	 * other way to contact the non direct connection peers somehow.
	 * 
	 * @return The peer ID of the member.
	 */
    public String getPeerForJoining() {
        int index = 0;
        int largestFoundNetwork = -1;
        int networkSize = 0;
        String neighborForJoining = null;
        for (; index < discoveredNetworks.size(); index++) {
            if (networkSize <= ((Network) discoveredNetworks.get(index)).getNetworkSize()) {
                networkSize = ((Network) discoveredNetworks.get(index)).getNetworkSize();
                largestFoundNetwork = index;
            }
        }
        if (largestFoundNetwork != -1) {
            Network network = (Network) discoveredNetworks.get(largestFoundNetwork);
            stm.addMembers(network.getMembers());
            stm.setNetworkCreationTime(network.getNetworkCreationTime());
            neighborForJoining = getNeighbor(network);
        }
        log.debug("Starting join process with peer " + neighborForJoining + ".");
        return neighborForJoining;
    }

    private String getNeighbor(Network network) {
        Members members = network.getMembers();
        for (int index = 0; index < members.size(); index++) {
            if (isNeighbor(members.getMember(index))) return members.getMember(index);
        }
        return null;
    }

    /**
	 * Verifies that the neighbors are still available. It also checks if new
	 * neighbors are available and which one should be used as neighbors.
	 * Because only a limited number of peers (directly reachable) can be used
	 * as neighbors.
	 */
    public void queryNeighbors() {
        if (lastQueryNeighbors + Constants.QUERY_NEIGHBORS_INTERVAL < System.currentTimeMillis()) {
            log.debug("Querying the neighborhood of peer " + stm.getPeerId());
            for (int index = 0; index < neighbors.size(); index++) {
                stm.sendMessage(new Message(new NeighborRequest(), ((Peer) neighbors.get(index)).getId()));
            }
            lastQueryNeighbors = System.currentTimeMillis();
        }
    }

    /**
	 * Adds the provided network to the discovered networks. This means that the
	 * local peer has a connection to another SEPP network through either an
	 * existent neighbor or through a peer wanting to join the local network.
	 * 
	 * @param network
	 *            The {@link Network} which has been discovered.
	 */
    public void addNetwork(Network network) {
        if (!isNetworkKnown(network) && !stm.isSwitchNetwork()) {
            if (stm.isJoined()) {
                discoveredNetworks.add(network);
                log.debug("Discovered another network of size " + network.getMembers().size());
                if (stm.getMembers().size() < network.getNetworkSize() || ((network.getNetworkSize() == stm.getMembers().size()) && (network.getNetworkCreationTime() <= stm.getNetworkCreationTime()))) {
                    stm.setSwitchToNetwork(network);
                }
            } else if (network.getNetworkSize() > stm.getMembers().size() || ((network.getNetworkSize() == stm.getMembers().size()) && (network.getNetworkCreationTime() <= stm.getNetworkCreationTime()))) {
                discoveredNetworks.add(network);
                log.debug("Discovered another network of size " + network.getMembers().size());
            } else log.debug("Found a network with the same amount of members but not that mature. Therefore, it is not added.");
        }
    }

    private boolean isNetworkKnown(Network network) {
        boolean alreadyContained = false;
        for (int index = 0; index < discoveredNetworks.size(); index++) {
            if (((Network) discoveredNetworks.get(index)).getNetworkId().equalsIgnoreCase(network.getNetworkId())) {
                alreadyContained = true;
                if (((Network) discoveredNetworks.get(index)).getMembers().size() < network.getNetworkSize()) ((Network) discoveredNetworks.get(index)).setMembers(network.getMembers());
                break;
            }
        }
        return alreadyContained;
    }

    public void clearNetworks() {
        discoveredNetworks.clear();
    }
}
