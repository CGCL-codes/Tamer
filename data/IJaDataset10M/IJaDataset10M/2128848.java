package org.matsim.withinday.replanning.identifiers.tools;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.apache.log4j.Logger;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.network.Network;
import org.matsim.api.core.v01.population.Person;
import org.matsim.core.api.experimental.events.AgentArrivalEvent;
import org.matsim.core.api.experimental.events.AgentDepartureEvent;
import org.matsim.core.api.experimental.events.AgentStuckEvent;
import org.matsim.core.api.experimental.events.AgentWait2LinkEvent;
import org.matsim.core.api.experimental.events.LinkEnterEvent;
import org.matsim.core.api.experimental.events.LinkLeaveEvent;
import org.matsim.core.api.experimental.events.handler.AgentArrivalEventHandler;
import org.matsim.core.api.experimental.events.handler.AgentDepartureEventHandler;
import org.matsim.core.api.experimental.events.handler.AgentStuckEventHandler;
import org.matsim.core.api.experimental.events.handler.AgentWait2LinkEventHandler;
import org.matsim.core.api.experimental.events.handler.LinkEnterEventHandler;
import org.matsim.core.api.experimental.events.handler.LinkLeaveEventHandler;
import org.matsim.core.mobsim.framework.MobsimAgent;
import org.matsim.core.mobsim.framework.events.MobsimInitializedEvent;
import org.matsim.core.mobsim.framework.listeners.MobsimInitializedListener;
import org.matsim.core.mobsim.qsim.QSim;
import org.matsim.core.mobsim.qsim.agents.PlanBasedWithinDayAgent;
import org.matsim.core.mobsim.qsim.interfaces.Mobsim;
import org.matsim.core.mobsim.qsim.multimodalsimengine.router.util.MultiModalTravelTime;
import org.matsim.core.network.LinkImpl;

/**
 * This Module is used by a CurrentLegReplanner. It calculates the time
 * when an agent should do CurrentLegReplanning.
 * <p/>
 * The time is estimated as following:
 * When a LinkEnterEvent is thrown the Replanning Time is set to
 * the current time + the FreeSpeed Travel Time. This guarantees that
 * the replanning will be done while the agent is on the Link. After that
 * time, the agent might be already in the outgoing queue of a QLink
 * where not all replanning operations are possible anymore (the agent
 * can e.g. not insert an Activity on its current link anymore).
 * <p/>
 * <p>
 * The replanning interval (multiple replannings on the same link when
 * an agent is stuck on a link due to a traffic jam) has been removed
 * since it cannot be guaranteed that all replanning operations are
 * valid anymore.
 * </p>
 * 
 * @author cdobler
 */
public class LinkReplanningMap implements LinkEnterEventHandler, LinkLeaveEventHandler, AgentArrivalEventHandler, AgentDepartureEventHandler, AgentWait2LinkEventHandler, AgentStuckEventHandler, MobsimInitializedListener {

    private static final Logger log = Logger.getLogger(LinkReplanningMap.class);

    private final Network network;

    private final MultiModalTravelTime multiModalTravelTime;

    private enum TimeFilterMode {

        EXACT, RESTRICTED, UNRESTRICTED
    }

    private final Map<Id, PlanBasedWithinDayAgent> personAgentMapping;

    private final Map<Id, Double> replanningMap;

    private final Set<Id> enrouteAgents;

    private final Map<Id, String> agentTransportModeMap;

    public LinkReplanningMap(Network network) {
        this(network, null);
        log.info("Note: no MultiModalTravelTime object given. Therefore use free speed car travel time as " + "minimal link travel time for all modes.");
    }

    public LinkReplanningMap(Network network, MultiModalTravelTime multiModalTravelTime) {
        log.info("Note that the LinkReplanningMap has to be registered as an EventHandler and a SimulationListener!");
        this.network = network;
        this.multiModalTravelTime = multiModalTravelTime;
        this.enrouteAgents = new HashSet<Id>();
        this.replanningMap = new HashMap<Id, Double>();
        this.personAgentMapping = new HashMap<Id, PlanBasedWithinDayAgent>();
        this.agentTransportModeMap = new HashMap<Id, String>();
    }

    public Map<Id, PlanBasedWithinDayAgent> getPersonAgentMapping() {
        return Collections.unmodifiableMap(this.personAgentMapping);
    }

    @Override
    public void notifyMobsimInitialized(MobsimInitializedEvent e) {
        Mobsim sim = (Mobsim) e.getQueueSimulation();
        personAgentMapping.clear();
        if (sim instanceof QSim) {
            for (MobsimAgent mobsimAgent : ((QSim) sim).getAgents()) {
                if (mobsimAgent instanceof PlanBasedWithinDayAgent) {
                    PlanBasedWithinDayAgent withinDayAgent = (PlanBasedWithinDayAgent) mobsimAgent;
                    personAgentMapping.put(withinDayAgent.getId(), withinDayAgent);
                }
            }
        }
    }

    @Override
    public void handleEvent(LinkEnterEvent event) {
        String mode = agentTransportModeMap.get(event.getPersonId());
        double now = event.getTime();
        Link link = network.getLinks().get(event.getLinkId());
        double departureTime;
        if (this.multiModalTravelTime != null) {
            Person person = this.personAgentMapping.get(event.getPersonId()).getSelectedPlan().getPerson();
            multiModalTravelTime.setPerson(person);
            double travelTime = multiModalTravelTime.getModalLinkTravelTime(link, now, mode);
            departureTime = Math.floor(now + travelTime);
        } else {
            departureTime = Math.floor((now + ((LinkImpl) link).getFreespeedTravelTime(now)));
        }
        replanningMap.put(event.getPersonId(), departureTime);
    }

    @Override
    public void handleEvent(LinkLeaveEvent event) {
        replanningMap.remove(event.getPersonId());
    }

    @Override
    public void handleEvent(AgentArrivalEvent event) {
        replanningMap.remove(event.getPersonId());
        agentTransportModeMap.remove(event.getPersonId());
        enrouteAgents.remove(event.getPersonId());
    }

    @Override
    public void handleEvent(AgentDepartureEvent event) {
        this.agentTransportModeMap.put(event.getPersonId(), event.getLegMode());
        this.replanningMap.put(event.getPersonId(), event.getTime());
        this.enrouteAgents.add(event.getPersonId());
    }

    @Override
    public void handleEvent(AgentWait2LinkEvent event) {
    }

    @Override
    public void handleEvent(AgentStuckEvent event) {
        replanningMap.remove(event.getPersonId());
        enrouteAgents.remove(event.getPersonId());
        agentTransportModeMap.remove(event.getPersonId());
    }

    @Override
    public void reset(int iteration) {
        this.replanningMap.clear();
        this.enrouteAgents.clear();
        this.agentTransportModeMap.clear();
    }

    /**
	 * @param time
	 * @return a list of agents who might need a replanning
	 */
    public Set<Id> getReplanningAgents(final double time) {
        Set<String> transportModes = null;
        return this.getReplanningAgents(time, transportModes);
    }

    /**
	 * @param time
	 * @param transportMode
	 * @return a list of agents who might need a replanning and use the given transport mode
	 */
    public Set<Id> getReplanningAgents(final double time, final String transportMode) {
        Set<String> transportModes = new HashSet<String>();
        transportModes.add(transportMode);
        return this.getReplanningAgents(time, transportModes);
    }

    /**
	 * @param time
	 * @param transportModes
	 * @return a list of agents who might need a replanning and use one of the given transport modes
	 */
    public Set<Id> getReplanningAgents(final double time, final Set<String> transportModes) {
        return this.filterAgents(time, transportModes, TimeFilterMode.EXACT);
    }

    /**
	 * @param time
	 * @return a list of agents who might need an unrestricted replanning and use the given transport mode
	 */
    public Set<Id> getUnrestrictedReplanningAgents(final double time) {
        Set<String> transportModes = null;
        return this.getUnrestrictedReplanningAgents(time, transportModes);
    }

    /**
	 * @param time
	 * @param transportMode
	 * @return a list of agents who might need an unrestricted replanning and use the given transport mode
	 */
    public Set<Id> getUnrestrictedReplanningAgents(final double time, final String transportMode) {
        Set<String> transportModes = new HashSet<String>();
        transportModes.add(transportMode);
        return this.getUnrestrictedReplanningAgents(time, transportModes);
    }

    /**
	 * @param time
	 * @param transportModes
	 * @return a list of agents who might need an unrestricted replanning and use one of the given transport modes
	 */
    public Set<Id> getUnrestrictedReplanningAgents(final double time, final Set<String> transportModes) {
        return this.filterAgents(time, transportModes, TimeFilterMode.UNRESTRICTED);
    }

    /**
	 * @param time
	 * @return a list of agents who might need a restricted replanning and use the given transport mode
	 */
    public Set<Id> getRestrictedReplanningAgents(final double time) {
        Set<String> transportModes = null;
        return this.getRestrictedReplanningAgents(time, transportModes);
    }

    /**
	 * @param time
	 * @param transportMode
	 * @return a list of agents who might need a restricted replanning and use the given transport mode
	 */
    public Set<Id> getRestrictedReplanningAgents(final double time, final String transportMode) {
        Set<String> transportModes = new HashSet<String>();
        transportModes.add(transportMode);
        return this.getRestrictedReplanningAgents(time, transportModes);
    }

    /**
	 * @param time
	 * @param transportModes
	 * @return a list of agents who might need a restricted replanning and use one of the given transport modes
	 */
    public Set<Id> getRestrictedReplanningAgents(final double time, final Set<String> transportModes) {
        return this.filterAgents(time, transportModes, TimeFilterMode.RESTRICTED);
    }

    private Set<Id> filterAgents(final double time, final Set<String> transportModes, final TimeFilterMode timeMode) {
        Set<Id> set = new HashSet<Id>();
        Iterator<Entry<Id, Double>> entries = replanningMap.entrySet().iterator();
        while (entries.hasNext()) {
            Entry<Id, Double> entry = entries.next();
            Id personId = entry.getKey();
            double replanningTime = entry.getValue();
            if (timeMode == TimeFilterMode.EXACT) {
                if (time != replanningTime) continue;
            } else if (timeMode == TimeFilterMode.RESTRICTED) {
                if (time <= replanningTime) continue;
            } else if (timeMode == TimeFilterMode.UNRESTRICTED) {
                if (time > replanningTime) continue;
            }
            if (transportModes != null) {
                String mode = this.agentTransportModeMap.get(personId);
                if (!transportModes.contains(mode)) continue;
            }
            set.add(personId);
        }
        return set;
    }

    /**
	 * @return A list of all agents that are currently performing a leg. Note that
	 * some of them might be limited in the available replanning operations! 
	 */
    public Set<Id> getLegPerformingAgents() {
        Set<String> transportModes = null;
        return this.getLegPerformingAgents(transportModes);
    }

    /**
	 * @param transportMode
	 * @return A list of all agents that are currently performing a leg with the
	 * given transport mode. Note that some of them might be limited in the available 
	 * replanning operations! 
	 */
    public Set<Id> getLegPerformingAgents(final String transportMode) {
        Set<String> transportModes = new HashSet<String>();
        transportModes.add(transportMode);
        return this.getLegPerformingAgents(transportModes);
    }

    /**
	 * @param transportModes
	 * @return A list of Ids of all agents that are currently performing a leg with the
	 * given transport mode. Note that some of them might be limited in the available 
	 * replanning operations! 
	 */
    public Set<Id> getLegPerformingAgents(final Set<String> transportModes) {
        Set<Id> legPerformingAgents = new HashSet<Id>();
        for (Id id : this.enrouteAgents) {
            if (transportModes != null) {
                String mode = this.agentTransportModeMap.get(id);
                if (!transportModes.contains(mode)) continue;
            }
            legPerformingAgents.add(id);
        }
        return legPerformingAgents;
    }
}
