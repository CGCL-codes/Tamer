package playground.andreas.bvgAna.level1;

import java.util.Set;
import java.util.TreeMap;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.TransportMode;
import org.matsim.core.api.experimental.events.AgentDepartureEvent;
import org.matsim.core.api.experimental.events.handler.AgentDepartureEventHandler;
import org.matsim.core.events.PersonEntersVehicleEvent;
import org.matsim.core.events.handler.PersonEntersVehicleEventHandler;

/**
 * Collects the <code>AgentDepartureEventHandler</code> and the corresponding <code>PersonEntersVehicleEventHandler</code> for a given set of agent ids.
 *
 * @author aneumann
 *
 */
public class AgentId2DepartureDelayAtStopMap implements AgentDepartureEventHandler, PersonEntersVehicleEventHandler {

    private final Logger log = Logger.getLogger(AgentId2DepartureDelayAtStopMap.class);

    private final Level logLevel = Level.OFF;

    private final Set<Id> agentIds;

    private TreeMap<Id, AgentId2DepartureDelayAtStopMapData> stopId2DelayAtStopMap = new TreeMap<Id, AgentId2DepartureDelayAtStopMapData>();

    public AgentId2DepartureDelayAtStopMap(Set<Id> agentIds) {
        this.log.setLevel(this.logLevel);
        this.agentIds = agentIds;
    }

    @Override
    public void handleEvent(AgentDepartureEvent event) {
        if (this.agentIds.contains(event.getPersonId())) {
            if (event.getLegMode() == TransportMode.pt) {
                if (this.stopId2DelayAtStopMap.get(event.getPersonId()) == null) {
                    this.stopId2DelayAtStopMap.put(event.getPersonId(), new AgentId2DepartureDelayAtStopMapData(event.getPersonId()));
                }
                this.stopId2DelayAtStopMap.get(event.getPersonId()).addAgentDepartureEvent(event);
            }
        }
    }

    @Override
    public void reset(int iteration) {
        this.log.debug("reset method in iteration " + iteration + " not implemented, yet");
    }

    @Override
    public void handleEvent(PersonEntersVehicleEvent event) {
        if (this.agentIds.contains(event.getPersonId())) {
            this.stopId2DelayAtStopMap.get(event.getPersonId()).addPersonEntersVehicleEvent(event);
        }
    }

    /**
	 * Returns departure and enter vehicle time information.
	 *
	 * @return A map containing a <code>AgentDelayAtStopContainer</code> for each agent id
	 */
    public TreeMap<Id, AgentId2DepartureDelayAtStopMapData> getStopId2DelayAtStopMap() {
        return this.stopId2DelayAtStopMap;
    }
}
