package playground.mrieser.core.mobsim.impl;

import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.population.Leg;
import org.matsim.core.basic.v01.IdImpl;
import org.matsim.core.population.routes.NetworkRoute;
import org.matsim.vehicles.VehicleImpl;
import org.matsim.vehicles.VehicleType;
import org.matsim.vehicles.VehicleTypeImpl;
import playground.mrieser.core.mobsim.api.DepartureHandler;
import playground.mrieser.core.mobsim.api.DriverAgent;
import playground.mrieser.core.mobsim.api.NewMobsimEngine;
import playground.mrieser.core.mobsim.api.PlanAgent;
import playground.mrieser.core.mobsim.api.MobsimVehicle;
import playground.mrieser.core.mobsim.features.NetworkFeature;
import playground.mrieser.core.mobsim.network.api.MobsimLink;

/**
 * This DepartureHandler assigns departing agents to a vehicle which is placed on
 * the network to be moved around. A vehicle will be created for each agent the first
 * time no vehicle can be found on the departing link. *
 * <br />
 * Requirements / Assumptions of the code:
 * <ul>
 * 	<li>handleDeparture() assumes the currentPlanElement of agents to be of type {@link Leg}</li>
 *  <li>handleDeparture() assumes the route of legs to be of type {@link NetworkRoute}</li>
 * </ul>
 *
 * @author mrieser
 */
public class CarDepartureHandler implements DepartureHandler {

    private static final Logger log = Logger.getLogger(CarDepartureHandler.class);

    private final NetworkFeature networkFeature;

    private final NewMobsimEngine engine;

    private final Map<Id, Id> vehicleLocations;

    private final VehicleType defaultVehicleType;

    private boolean teleportVehicles = false;

    public CarDepartureHandler(final NewMobsimEngine engine, final NetworkFeature networkFeature, final Scenario scenario) {
        this.engine = engine;
        this.networkFeature = networkFeature;
        this.vehicleLocations = new HashMap<Id, Id>((int) (scenario.getPopulation().getPersons().size() * 1.4));
        this.defaultVehicleType = new VehicleTypeImpl(new IdImpl("auto-generated vehicle"));
    }

    public void setTeleportVehicles(final boolean teleportVehicles) {
        this.teleportVehicles = teleportVehicles;
    }

    public boolean isTeleportVehicles() {
        return this.teleportVehicles;
    }

    @Override
    public void handleDeparture(final PlanAgent agent) {
        Leg leg = (Leg) agent.getCurrentPlanElement();
        NetworkRoute route = (NetworkRoute) leg.getRoute();
        MobsimLink link = this.networkFeature.getSimNetwork().getLinks().get(route.getStartLinkId());
        Id vehId = agent.getPlan().getPerson().getId();
        MobsimVehicle simVehicle = link.getParkedVehicle(vehId);
        if (simVehicle == null) {
            Id linkId = this.vehicleLocations.get(vehId);
            if (linkId == null) {
                simVehicle = new DefaultMobsimVehicle(new VehicleImpl(vehId, this.defaultVehicleType));
                link.insertVehicle(simVehicle, MobsimLink.POSITION_AT_TO_NODE, MobsimLink.PRIORITY_PARKING);
            } else if (this.teleportVehicles) {
                log.warn("Agent departs on link " + route.getStartLinkId() + ", but vehicle is on link " + linkId + ". Teleporting the vehicle.");
                MobsimLink link2 = this.networkFeature.getSimNetwork().getLinks().get(linkId);
                simVehicle = link2.getParkedVehicle(vehId);
                link2.removeVehicle(simVehicle);
                link.insertVehicle(simVehicle, MobsimLink.POSITION_AT_TO_NODE, MobsimLink.PRIORITY_PARKING);
            } else {
                log.error("Agent departs on link " + route.getStartLinkId() + ", but vehicle is on link " + linkId + ". Agent is removed from simulation.");
                return;
            }
        }
        this.vehicleLocations.put(vehId, route.getEndLinkId());
        DriverAgent driver = new NetworkRouteDriver(agent, this.engine, route, simVehicle);
        simVehicle.setDriver(driver);
        driver.notifyMoveToNextLink();
        if (driver.getNextLinkId() == null) {
            this.engine.handleAgent(agent);
        } else {
            link.continueVehicle(simVehicle);
        }
    }
}
