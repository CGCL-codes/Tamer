package playground.marcel.pt.integration;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.matsim.core.mobsim.queuesim.DriverAgent;
import org.matsim.transitSchedule.TransitStopFacility;

public class TransitStopAgentTracker {

    private final Map<TransitStopFacility, List<DriverAgent>> agentsAtStops = new HashMap<TransitStopFacility, List<DriverAgent>>();

    private final List<DriverAgent> emptyList = new LinkedList<DriverAgent>();

    public void addAgentToStop(final DriverAgent agent, final TransitStopFacility stop) {
        List<DriverAgent> agents = this.agentsAtStops.get(stop);
        if (agents == null) {
            agents = new LinkedList<DriverAgent>();
            this.agentsAtStops.put(stop, agents);
        }
        agents.add(agent);
    }

    public List<DriverAgent> getAgentsAtStop(final TransitStopFacility stop) {
        List<DriverAgent> agents = this.agentsAtStops.get(stop);
        if (agents == null) {
            return this.emptyList;
        }
        return agents;
    }
}
