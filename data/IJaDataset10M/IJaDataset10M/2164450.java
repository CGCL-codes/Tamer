package playground.wrashid.PDES3;

import org.matsim.interfaces.core.v01.Link;
import org.matsim.mobsim.jdeqsim.Road;
import org.matsim.mobsim.jdeqsim.Scheduler;
import org.matsim.mobsim.jdeqsim.SimulationParameters;
import org.matsim.mobsim.jdeqsim.Vehicle;

public class BorderRoad extends Road {

    private int startingRoadZoneId = 0;

    private int endingRoadZoneId = 0;

    public BorderRoad(PScheduler scheduler, Link link, int startingRoadZoneId, int endingRoadZoneId) {
        super(scheduler, link);
        this.startingRoadZoneId = startingRoadZoneId;
        this.endingRoadZoneId = endingRoadZoneId;
    }

    public void enterRequest(Vehicle vehicle, double simTime) {
        synchronized (this) {
            super.enterRequest(vehicle, simTime);
        }
    }

    public void enterRoad(Vehicle vehicle, double simTime) {
        synchronized (this) {
            if (carsOnTheRoad.size() == 0) {
                ((PVehicle) vehicle).setCurrentZoneId(endingRoadZoneId);
                ((PVehicle) vehicle).setOwnsCurrentZone(false);
            }
            super.enterRoad(vehicle, simTime);
            if (carsOnTheRoad.size() == 1) {
                ((PVehicle) vehicle).setOwnsCurrentZone(true);
            }
        }
    }

    public void leaveRoad(Vehicle vehicle, double simTime) {
        synchronized (this) {
            super.leaveRoad(vehicle, simTime);
        }
    }
}
