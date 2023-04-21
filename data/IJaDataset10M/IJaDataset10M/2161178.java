package org.matsim.mobsim.jdeqsim;

public class EndRoadMessage extends EventMessage {

    @Override
    public void handleMessage() {
        if (vehicle.isCurrentLegFinished()) {
            vehicle.initiateEndingLegMode();
            vehicle.moveToFirstLinkInNextLeg();
            Road road = Road.getRoad(vehicle.getCurrentLink().getId().toString());
            road.enterRequest(vehicle, getMessageArrivalTime());
        } else if (!vehicle.isCurrentLegFinished()) {
            vehicle.moveToNextLinkInLeg();
            Road nextRoad = Road.getRoad(vehicle.getCurrentLink().getId().toString());
            nextRoad.enterRequest(vehicle, getMessageArrivalTime());
        }
    }

    public EndRoadMessage(Scheduler scheduler, Vehicle vehicle) {
        super(scheduler, vehicle);
    }

    public void processEvent() {
    }
}
