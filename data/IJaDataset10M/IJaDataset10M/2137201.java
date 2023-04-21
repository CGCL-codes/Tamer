package playground.wrashid.parkingSearch.withinday;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.matsim.api.core.v01.Id;
import org.matsim.core.mobsim.framework.MobsimAgent;
import org.matsim.core.mobsim.framework.events.MobsimInitializedEvent;
import org.matsim.core.mobsim.framework.listeners.MobsimInitializedListener;
import org.matsim.core.mobsim.qsim.QSim;
import org.matsim.core.mobsim.qsim.agents.ExperimentalBasicWithindayAgent;
import org.matsim.core.mobsim.qsim.agents.PlanBasedWithinDayAgent;
import org.matsim.withinday.replanning.identifiers.interfaces.DuringLegIdentifier;

public class RandomSearchIdentifier extends DuringLegIdentifier implements MobsimInitializedListener {

    private final ParkingAgentsTracker parkingAgentsTracker;

    private final ParkingInfrastructure parkingInfrastructure;

    private final Map<Id, PlanBasedWithinDayAgent> agents;

    public RandomSearchIdentifier(ParkingAgentsTracker parkingAgentsTracker, ParkingInfrastructure parkingInfrastructure) {
        this.parkingAgentsTracker = parkingAgentsTracker;
        this.parkingInfrastructure = parkingInfrastructure;
        this.agents = new HashMap<Id, PlanBasedWithinDayAgent>();
    }

    @Override
    public Set<PlanBasedWithinDayAgent> getAgentsToReplan(double time) {
        Set<Id> linkEnteredAgents = this.parkingAgentsTracker.getLinkEnteredAgents();
        Set<PlanBasedWithinDayAgent> identifiedAgents = new HashSet<PlanBasedWithinDayAgent>();
        for (Id agentId : linkEnteredAgents) {
            PlanBasedWithinDayAgent agent = this.agents.get(agentId);
            if (requiresReplanning(agent)) {
                Id linkId = agent.getCurrentLinkId();
                Id facilityId = parkingInfrastructure.getFreeParkingFacilityOnLink(linkId);
                if (facilityId != null) {
                    parkingInfrastructure.reserveParking(facilityId);
                    parkingAgentsTracker.setSelectedParking(agentId, facilityId);
                }
                identifiedAgents.add(agent);
            }
        }
        return identifiedAgents;
    }

    private boolean requiresReplanning(PlanBasedWithinDayAgent agent) {
        return parkingAgentsTracker.getSelectedParking(agent.getId()) == null;
    }

    @Override
    public void notifyMobsimInitialized(MobsimInitializedEvent e) {
        this.agents.clear();
        for (MobsimAgent agent : ((QSim) e.getQueueSimulation()).getAgents()) {
            this.agents.put(agent.getId(), (ExperimentalBasicWithindayAgent) agent);
        }
    }
}
