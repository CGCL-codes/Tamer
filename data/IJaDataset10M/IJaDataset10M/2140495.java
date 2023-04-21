package example.loadbalance;

import peersim.config.*;
import peersim.core.*;

/**
 * This class restores the quota value at each node in the topology in order to
 * be able to perform the next cycle. It is assumed that the network nodes are
 * instancies of the {@link example.loadbalance.BasicBalance} class.
 *
 * <p>Note that this control can be replaced by the library class
 * {@link peersim.dynamics.MethodInvoker} by configuring method "resetQuota".
 */
public class ResetQuota implements Control {

    /**
     * The protocol to operate on.
     * 
     * @config
     */
    private static final String PAR_PROT = "protocol";

    /** Value obtained from config property {@link #PAR_PROT}. */
    private final int protocolID;

    /**
     * Standard constructor that reads the configuration parameters. Invoked by
     * the simulation engine.
     * 
     * @param prefix
     *            the configuration prefix for this class.
     */
    public ResetQuota(String prefix) {
        protocolID = Configuration.getPid(prefix + "." + PAR_PROT);
    }

    public boolean execute() {
        for (int i = 0; i < Network.size(); ++i) {
            ((BasicBalance) Network.get(i).getProtocol(protocolID)).resetQuota();
        }
        return false;
    }
}
