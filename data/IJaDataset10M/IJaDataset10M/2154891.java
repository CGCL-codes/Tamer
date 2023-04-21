package org.mars_sim.msp.core.person.ai.mission;

import org.mars_sim.msp.core.events.HistoricalEvent;
import org.mars_sim.msp.core.events.HistoricalEventManager;
import org.mars_sim.msp.core.person.Person;

/**
 * This class represents the historical actions involving missions.
 */
public class MissionHistoricalEvent extends HistoricalEvent {

    public static final String START = "Mission Started";

    public static final String JOINING = "Mission Joined";

    public static final String FINISH = "Mission Finished";

    public static final String DEVELOPMENT = "Mission Development";

    public static final String EMERGENCY_DESTINATION = "Changing To Emergency Destination";

    public static final String EMERGENCY_BEACON = "Emergency Beacon Turned On";

    public static final String RENDEZVOUS = "Rescue/Salvage Mission Rendezvous with Target Vehicle";

    public static final String SALVAGE_VEHICLE = "Salvage Vehicle";

    public static final String RESCUE_PERSON = "Rescue Person";

    /**
	 * Constructor
	 * @param person The person on the mission.
	 * @param mission The mission with the event.
	 * @param eventType The type of event.
	 */
    public MissionHistoricalEvent(Person person, Mission mission, String eventType) {
        super(HistoricalEventManager.MISSION, eventType, person, mission.getName());
    }
}
