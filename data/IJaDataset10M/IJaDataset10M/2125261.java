package example.aggregation;

import peersim.config.Configuration;
import peersim.core.Control;
import peersim.core.Network;
import peersim.util.IncrementalStats;
import peersim.vector.SingleValue;

/**
 * Print statistics for an average aggregation computation. Statistics printed
 * are defined by {@link IncrementalStats#toString}
 * 
 * @author Alberto Montresor
 * @version $Revision: 1.17 $
 */
public class AverageObserver implements Control {

    /**
     * Config parameter that determines the accuracy for standard deviation
     * before stopping the simulation. If not defined, a negative value is used
     * which makes sure the observer does not stop the simulation
     * 
     * @config
     */
    private static final String PAR_ACCURACY = "accuracy";

    /**
     * The protocol to operate on.
     * 
     * @config
     */
    private static final String PAR_PROT = "protocol";

    /**
     * The name of this observer in the configuration. Initialized by the
     * constructor parameter.
     */
    private final String name;

    /**
     * Accuracy for standard deviation used to stop the simulation; obtained
     * from config property {@link #PAR_ACCURACY}.
     */
    private final double accuracy;

    /** Protocol identifier; obtained from config property {@link #PAR_PROT}. */
    private final int pid;

    /**
     * Creates a new observer reading configuration parameters.
     */
    public AverageObserver(String name) {
        this.name = name;
        accuracy = Configuration.getDouble(name + "." + PAR_ACCURACY, -1);
        pid = Configuration.getPid(name + "." + PAR_PROT);
    }

    /**
     * Print statistics for an average aggregation computation. Statistics
     * printed are defined by {@link IncrementalStats#toString}. The current
     * timestamp is also printed as a first field.
     * 
     * @return if the standard deviation is less than the given
     *         {@value #PAR_ACCURACY}.
     */
    public boolean execute() {
        long time = peersim.core.CommonState.getTime();
        IncrementalStats is = new IncrementalStats();
        for (int i = 0; i < Network.size(); i++) {
            SingleValue protocol = (SingleValue) Network.get(i).getProtocol(pid);
            is.add(protocol.getValue());
        }
        System.out.println(name + ": " + time + " " + is);
        return (is.getStD() <= accuracy);
    }
}
