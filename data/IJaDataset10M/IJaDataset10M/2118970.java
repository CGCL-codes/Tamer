package example.aggregation;

import peersim.cdsim.CDProtocol;
import peersim.config.FastConfig;
import peersim.core.CommonState;
import peersim.core.Linkable;
import peersim.core.Node;
import peersim.vector.SingleValueHolder;

/**
 * This class provides an implementation for the averaging function in the
 * aggregation framework. When a pair of nodes interact, their values are
 * averaged. The class subclasses {@link SingleValueHolder} in
 * order to provide a consistent access to the averaging variable value.
 *
 * Note that this class does not override the clone method, because it does
 * not have any state other than what is inherited from
 * {@link SingleValueHolder}.
 * 
 * @author Alberto Montresor
 * @version $Revision: 1.11 $
 */
public class AverageFunction extends SingleValueHolder implements CDProtocol {

    /**
     * Creates a new {@link example.aggregation.AverageFunction} protocol
     * instance.
     * 
     * @param prefix
     *            the component prefix declared in the configuration file.
     */
    public AverageFunction(String prefix) {
        super(prefix);
    }

    /**
     * Using an underlying {@link Linkable} protocol choses a neighbor and
     * performs a variance reduction step.
     * 
     * @param node
     *            the node on which this component is run.
     * @param protocolID
     *            the id of this protocol in the protocol array.
     */
    public void nextCycle(Node node, int protocolID) {
        int linkableID = FastConfig.getLinkable(protocolID);
        Linkable linkable = (Linkable) node.getProtocol(linkableID);
        if (linkable.degree() > 0) {
            Node peer = linkable.getNeighbor(CommonState.r.nextInt(linkable.degree()));
            if (!peer.isUp()) {
                return;
            }
            AverageFunction neighbor = (AverageFunction) peer.getProtocol(protocolID);
            double mean = (this.value + neighbor.value) / 2;
            this.value = mean;
            neighbor.value = mean;
        }
    }
}
