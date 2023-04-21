package org.matsim.ptproject.qsim.qnetsimengine;

import org.apache.log4j.Logger;
import org.matsim.api.core.v01.Id;
import org.matsim.core.mobsim.framework.MobsimAgent;
import org.matsim.core.mobsim.framework.MobsimDriverAgent;
import org.matsim.ptproject.qsim.agents.PersonDriverAgentImpl;
import org.matsim.ptproject.qsim.interfaces.DepartureHandler;

class VehicularDepartureHandler implements DepartureHandler {

    public static enum VehicleBehavior {

        TELEPORT, WAIT_UNTIL_IT_COMES_ALONG, EXCEPTION
    }

    ;

    private static Logger log = Logger.getLogger(VehicularDepartureHandler.class);

    private int cntTeleportVehicle = 0;

    private VehicleBehavior vehicleBehavior;

    private QNetsimEngine qNetsimEngine;

    private String transportMode;

    VehicularDepartureHandler(QNetsimEngine qNetsimEngine, VehicleBehavior vehicleBehavior) {
        this.qNetsimEngine = qNetsimEngine;
        this.vehicleBehavior = vehicleBehavior;
        this.transportMode = qNetsimEngine.getMobsim().getScenario().getConfig().getQSimConfigGroup().getMainMode();
    }

    @Override
    public boolean handleDeparture(double now, MobsimAgent agent, Id linkId) {
        if (agent.getMode().equals(this.transportMode)) {
            if (agent instanceof MobsimDriverAgent) {
                handleCarDeparture(now, (MobsimDriverAgent) agent, linkId);
                return true;
            } else {
                throw new UnsupportedOperationException("wrong agent type to use a car");
            }
        }
        return false;
    }

    private void handleCarDeparture(double now, MobsimDriverAgent agent, Id linkId) {
        Id vehicleId = agent.getPlannedVehicleId();
        AbstractQLink qlink = (AbstractQLink) qNetsimEngine.getNetsimNetwork().getNetsimLink(linkId);
        QVehicle vehicle = qlink.removeParkedVehicle(vehicleId);
        if (vehicle == null) {
            if (vehicleBehavior == VehicleBehavior.TELEPORT && agent instanceof PersonDriverAgentImpl) {
                vehicle = qNetsimEngine.getVehicles().get(vehicleId);
                teleportVehicleTo(vehicle, linkId);
                qlink.letAgentDepartWithVehicle(agent, vehicle, now);
            } else if (vehicleBehavior == VehicleBehavior.WAIT_UNTIL_IT_COMES_ALONG) {
                qlink.registerAgentWaitingForCar(agent);
            } else {
                throw new RuntimeException("vehicle not available for agent " + agent.getId() + " on link " + linkId);
            }
        } else {
            qlink.letAgentDepartWithVehicle(agent, vehicle, now);
        }
    }

    /**
	 * Design thoughs:<ul>
	 * <li> yyyyyy It is not completely clear what happens when the vehicle is used by someone else. kai, nov'11
	 * <li> Seems to me that a parked vehicle is teleported. kai, nov'11
	 * <li> yyyyyy Seems to me that a non-parked vehicle will end up with two references to it, with race conditions???? kai, nov11
	 * <li> yyyyyy Note that the "linkId" parameter is not used for any physical action!!
	 * </ul> 
	 */
    private void teleportVehicleTo(QVehicle vehicle, Id linkId) {
        if (vehicle.getCurrentLink() != null) {
            if (cntTeleportVehicle < 9) {
                cntTeleportVehicle++;
                log.info("teleport vehicle " + vehicle.getId() + " from link " + vehicle.getCurrentLink().getId() + " to link " + linkId);
                if (cntTeleportVehicle == 9) {
                    log.info("No more occurrences of teleported vehicles will be reported.");
                }
            }
            AbstractQLink qlinkOld = (AbstractQLink) qNetsimEngine.getNetsimNetwork().getNetsimLink(vehicle.getCurrentLink().getId());
            qlinkOld.removeParkedVehicle(vehicle.getId());
        }
    }
}
