package protopeer.network.flowbased.uplinkbottleneck;

import java.util.*;
import protopeer.network.*;
import protopeer.network.flowbased.*;
import protopeer.util.quantities.*;

/**
 * Very simple bandwidth allocation algorithm.
 * 
 * Uses the <code>UplinkBottleneckTopology</code>, i.e., it assumes that
 * only the uplink is the bottleneck of the connections.
 * 
 * Every connection on the uplink receives a bandwidth of bw/n when
 * bw is the uplink bandwidth and n is the number of connections. 
 * 
 */
public class UplinkBWAllocation extends BandwidthAllocationAlgorithm {

    public UplinkBWAllocation(UplinkBottleneckTopology topology) {
        super(topology);
    }

    @Override
    public UplinkBottleneckTopology getTopology() {
        return (UplinkBottleneckTopology) super.getTopology();
    }

    @Override
    public Map<Connection, Bandwidth> connectionAdded(Connection newConnection, Set<Connection> activeConnections) {
        return adjustRates(newConnection, activeConnections);
    }

    @Override
    public Map<Connection, Bandwidth> connectionTerminated(Connection connection, Set<Connection> activeConnections) {
        return adjustRates(connection, activeConnections);
    }

    private Map<Connection, Bandwidth> adjustRates(Connection changedConnection, Set<Connection> activeConnections) {
        Map<Connection, Bandwidth> newRates = new LinkedHashMap<Connection, Bandwidth>();
        NetworkAddress source = changedConnection.getSourceAddress();
        Link uplink = getTopology().getUplink(source);
        Set<Connection> connections = getTopology().getUplink(source).getConnections();
        Bandwidth newRate = uplink.getCapacity().divideBy(connections.size());
        for (Connection connection : connections) {
            newRates.put(connection, newRate);
        }
        return newRates;
    }
}
