package solarnetwork.central;

import java.util.Set;
import solarnetwork.Location;
import solarnetwork.ObjectCriteria;
import solarnetwork.node.LoadManagementState;
import solarnetwork.node.NodeIdentity;
import solarnetwork.node.NodeInformation;

/**
 * Service methods for supporting an electricity supplier.
 * 
 * @author matt
 * @version $Revision$
 */
public interface SupplierService {

    /**
	 * Query for a list of all network nodes participating with the supplier.
	 * 
	 * @return collection of node identities, never <em>null</em>
	 */
    Set<NodeIdentity> getParticipatingNodes();

    /**
	 * Query for a list of network nodes participating with the supplier,
	 * filtering the results according to a {@link Location} criteria.
	 * 
	 * @param criteria the location criteria
	 * @return collection of node identities, never <em>null</em>
	 */
    Set<NodeIdentity> getParticipatingNodesForLocation(ObjectCriteria<Location> criteria);

    /**
	 * Get a collection of {@link NodeStatus} objects for a specific
	 * set of node identities.
	 * 
	 * <p>The order of the returned status objects is not defined, and may
	 * not be assumed to be in the same order as the {@code nodes} parameter.</p>
	 * 
	 * @param nodes the node identities to get the status of
	 * @return collection of node statuses (never <em>null</em>)
	 */
    Set<SupplierNodeStatus> getNodeStatus(Set<NodeIdentity> nodes);

    /**
	 * Get a collection of {@link NodeInformation} objects for a specific
	 * set of node identities.
	 * 
	 * <p>The order of the returned status objects is not defined, and may
	 * not be assumed to be in the same order as the {@code nodes} parameter.</p>
	 * 
	 * @param nodes the node identities to get the status of
	 * @return collection of node information (never <em>null</em>)
	 */
    Set<NodeInformation> getNodeInformation(Set<NodeIdentity> nodes);

    /**
	 * Request a set of nodes to enter a specific load management state.
	 * 
	 * @param nodes the node identities to make the request to
	 * @param state the desired load management state of those nodes
	 */
    void requestLoadManagementState(Set<NodeIdentity> nodes, LoadManagementState state);
}
