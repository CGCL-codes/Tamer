package org.matsim.core.mobsim.jdeqsim;

import org.matsim.core.events.AgentWait2LinkEventImpl;
import org.matsim.core.events.EventImpl;
import org.matsim.core.events.LinkEnterEventImpl;

/**
 * The micro-simulation internal handler for entering a road.
 *
 * @author rashid_waraich
 */
public class EnterRoadMessage extends EventMessage {

    @Override
    public void handleMessage() {
        Road road = Road.getRoad(vehicle.getCurrentLinkId());
        road.enterRoad(vehicle, getMessageArrivalTime());
    }

    public EnterRoadMessage(Scheduler scheduler, Vehicle vehicle) {
        super(scheduler, vehicle);
        priority = SimulationParameters.PRIORITY_ENTER_ROAD_MESSAGE;
    }

    @Override
    public void processEvent() {
        EventImpl event = null;
        if (vehicle.getLinkIndex() == -1) {
            event = new AgentWait2LinkEventImpl(this.getMessageArrivalTime(), vehicle.getOwnerPerson().getId(), vehicle.getCurrentLinkId(), null);
        } else {
            event = new LinkEnterEventImpl(this.getMessageArrivalTime(), vehicle.getOwnerPerson().getId(), vehicle.getCurrentLinkId(), null);
        }
        SimulationParameters.getProcessEventThread().processEvent(event);
    }
}
