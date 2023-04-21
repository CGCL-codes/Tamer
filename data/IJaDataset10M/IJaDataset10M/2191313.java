package org.matsim.core.mobsim.qsim.qnetsimengine;

import java.util.ArrayList;
import java.util.List;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.TransportMode;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.network.Node;
import org.matsim.api.core.v01.population.Activity;
import org.matsim.api.core.v01.population.Leg;
import org.matsim.api.core.v01.population.Plan;
import org.matsim.core.api.experimental.events.AgentStuckEvent;
import org.matsim.core.api.experimental.events.Event;
import org.matsim.core.basic.v01.IdImpl;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.config.groups.QSimConfigGroup;
import org.matsim.core.events.EventsUtils;
import org.matsim.core.mobsim.qsim.QSim;
import org.matsim.core.mobsim.qsim.agents.PersonDriverAgentImpl;
import org.matsim.core.mobsim.qsim.interfaces.Netsim;
import org.matsim.core.mobsim.qsim.qnetsimengine.DefaultQSimEngineFactory;
import org.matsim.core.mobsim.qsim.qnetsimengine.NetsimLink;
import org.matsim.core.mobsim.qsim.qnetsimengine.NetsimNetwork;
import org.matsim.core.mobsim.qsim.qnetsimengine.QLinkImpl;
import org.matsim.core.mobsim.qsim.qnetsimengine.QNetwork;
import org.matsim.core.mobsim.qsim.qnetsimengine.QVehicle;
import org.matsim.core.network.NetworkImpl;
import org.matsim.core.population.ActivityImpl;
import org.matsim.core.population.LegImpl;
import org.matsim.core.population.PersonImpl;
import org.matsim.core.population.PlanImpl;
import org.matsim.core.population.PopulationFactoryImpl;
import org.matsim.core.population.routes.LinkNetworkRouteImpl;
import org.matsim.core.population.routes.NetworkRoute;
import org.matsim.core.scenario.ScenarioImpl;
import org.matsim.core.scenario.ScenarioUtils;
import org.matsim.core.utils.geometry.CoordImpl;
import org.matsim.testcases.MatsimTestCase;
import org.matsim.testcases.utils.EventsCollector;
import org.matsim.vehicles.Vehicle;
import org.matsim.vehicles.VehicleImpl;
import org.matsim.vehicles.VehicleType;
import org.matsim.vehicles.VehicleTypeImpl;

/**
 * @author dgrether
 * @author mrieser
 */
public class QLinkTest extends MatsimTestCase {

    public void testInit() {
        Fixture f = new Fixture();
        assertNotNull(f.qlink1);
        assertEquals(1.0, f.qlink1.getSimulatedFlowCapacity(), EPSILON);
        assertEquals(1.0, f.qlink1.getSpaceCap(), EPSILON);
        assertEquals(f.link1, f.qlink1.getLink());
        assertEquals(f.queueNetwork.getNetsimNode(new IdImpl("2")), f.qlink1.getToNode());
    }

    public void testAdd() {
        Fixture f = new Fixture();
        assertEquals(0, f.qlink1.vehOnLinkCount());
        QVehicle v = new QVehicle(f.basicVehicle);
        PersonImpl p = new PersonImpl(new IdImpl("1"));
        p.addPlan(new PlanImpl());
        ScenarioImpl scenario = (ScenarioImpl) ScenarioUtils.createScenario(ConfigUtils.createConfig());
        scenario.getConfig().addQSimConfigGroup(new QSimConfigGroup());
        v.setDriver(createAndInsertPersonDriverAgentImpl(p, f.sim));
        f.qlink1.addFromIntersection(v);
        assertEquals(1, f.qlink1.vehOnLinkCount());
        assertFalse(f.qlink1.hasSpace());
        assertTrue(f.qlink1.isNotOfferingVehicle());
    }

    private PersonDriverAgentImpl createAndInsertPersonDriverAgentImpl(PersonImpl p, Netsim simulation) {
        PersonDriverAgentImpl agent = new PersonDriverAgentImpl(p, p.getSelectedPlan(), simulation);
        simulation.insertAgentIntoMobsim(agent);
        return agent;
    }

    /**
	 * Tests that vehicles driving on a link are found with {@link NetsimLink#getVehicle(Id)}
	 * and {@link NetsimLink#getAllVehicles()}.
	 *
	 * @author mrieser
	 */
    public void testGetVehicle_Driving() {
        Fixture f = new Fixture();
        Id id1 = new IdImpl("1");
        QVehicle veh = new QVehicle(f.basicVehicle);
        PersonImpl p = new PersonImpl(new IdImpl(23));
        p.addPlan(new PlanImpl());
        veh.setDriver(createAndInsertPersonDriverAgentImpl(p, f.sim));
        assertTrue(f.qlink1.isNotOfferingVehicle());
        assertEquals(0, f.qlink1.vehOnLinkCount());
        assertNull(f.qlink1.getVehicle(id1));
        assertEquals(0, f.qlink1.getAllVehicles().size());
        f.qlink1.addFromIntersection(veh);
        assertTrue(f.qlink1.isNotOfferingVehicle());
        assertEquals(1, f.qlink1.vehOnLinkCount());
        assertEquals("vehicle not found on link.", veh, f.qlink1.getVehicle(id1));
        assertEquals(1, f.qlink1.getAllVehicles().size());
        f.qlink1.doSimStep(1.0);
        assertFalse(f.qlink1.isNotOfferingVehicle());
        assertEquals(0, f.qlink1.vehOnLinkCount());
        assertEquals("vehicle not found in buffer.", veh, f.qlink1.getVehicle(id1));
        assertEquals(1, f.qlink1.getAllVehicles().size());
        assertEquals(veh, f.qlink1.getAllVehicles().iterator().next());
        f.qlink1.doSimStep(2.0);
        assertEquals(veh, f.qlink1.popFirstVehicle());
        assertTrue(f.qlink1.isNotOfferingVehicle());
        assertEquals(0, f.qlink1.vehOnLinkCount());
        assertNull("vehicle should not be on link anymore.", f.qlink1.getVehicle(id1));
        assertEquals(0, f.qlink1.getAllVehicles().size());
    }

    /**
	 * Tests that vehicles parked on a link are found with {@link NetsimLink#getVehicle(Id)}
	 * and {@link NetsimLink#getAllVehicles()}.
	 *
	 * @author mrieser
	 */
    public void testGetVehicle_Parking() {
        Fixture f = new Fixture();
        Id id1 = new IdImpl("1");
        QVehicle veh = new QVehicle(f.basicVehicle);
        PersonImpl p = new PersonImpl(new IdImpl(42));
        p.addPlan(new PlanImpl());
        veh.setDriver(createAndInsertPersonDriverAgentImpl(p, f.sim));
        assertTrue(f.qlink1.isNotOfferingVehicle());
        assertEquals(0, f.qlink1.vehOnLinkCount());
        assertEquals(0, f.qlink1.getAllVehicles().size());
        f.qlink1.addParkedVehicle(veh);
        assertTrue(f.qlink1.isNotOfferingVehicle());
        assertEquals(0, f.qlink1.vehOnLinkCount());
        assertEquals("vehicle not found in parking list.", veh, f.qlink1.getVehicle(id1));
        assertEquals(1, f.qlink1.getAllVehicles().size());
        assertEquals(veh, f.qlink1.getAllVehicles().iterator().next());
        assertEquals("removed wrong vehicle.", veh, f.qlink1.removeParkedVehicle(veh.getId()));
        assertTrue(f.qlink1.isNotOfferingVehicle());
        assertEquals(0, f.qlink1.vehOnLinkCount());
        assertNull("vehicle not found in parking list.", f.qlink1.getVehicle(id1));
        assertEquals(0, f.qlink1.getAllVehicles().size());
    }

    /**
	 * Tests that vehicles departing on a link are found with {@link NetsimLink#getVehicle(Id)}
	 * and {@link NetsimLink#getAllVehicles()}.
	 *
	 * @author mrieser
	 */
    public void testGetVehicle_Departing() {
        Fixture f = new Fixture();
        Id id1 = new IdImpl("1");
        QVehicle veh = new QVehicle(f.basicVehicle);
        PersonImpl pers = new PersonImpl(new IdImpl(80));
        Plan plan = new PlanImpl();
        pers.addPlan(plan);
        plan.addActivity(new ActivityImpl("home", f.link1.getId()));
        Leg leg = new LegImpl(TransportMode.car);
        LinkNetworkRouteImpl route = new LinkNetworkRouteImpl(f.link1.getId(), f.link2.getId());
        route.setVehicleId(f.basicVehicle.getId());
        leg.setRoute(route);
        plan.addLeg(leg);
        plan.addActivity(new ActivityImpl("work", f.link2.getId()));
        PersonDriverAgentImpl driver = createAndInsertPersonDriverAgentImpl(pers, f.sim);
        veh.setDriver(driver);
        driver.setVehicle(veh);
        f.qlink1.addParkedVehicle(veh);
        assertTrue(f.qlink1.isNotOfferingVehicle());
        assertEquals(0, f.qlink1.vehOnLinkCount());
        assertEquals(1, f.qlink1.getAllVehicles().size());
        driver.endActivityAndComputeNextState(0);
        f.queueNetwork.simEngine.internalInterface.arrangeNextAgentState(driver);
        assertTrue(f.qlink1.isNotOfferingVehicle());
        assertEquals(0, f.qlink1.vehOnLinkCount());
        assertEquals("vehicle not found in waiting list.", veh, f.qlink1.getVehicle(id1));
        assertEquals(1, f.qlink1.getAllVehicles().size());
        assertEquals(veh, f.qlink1.getAllVehicles().iterator().next());
        f.qlink1.doSimStep(1.0);
        assertFalse(f.qlink1.isNotOfferingVehicle());
        assertEquals(0, f.qlink1.vehOnLinkCount());
        assertEquals("vehicle not found in buffer.", veh, f.qlink1.getVehicle(id1));
        assertEquals(1, f.qlink1.getAllVehicles().size());
        assertEquals(veh, f.qlink1.popFirstVehicle());
        assertTrue(f.qlink1.isNotOfferingVehicle());
        assertEquals(0, f.qlink1.vehOnLinkCount());
        assertNull("vehicle should not be on link anymore.", f.qlink1.getVehicle(id1));
        assertEquals(0, f.qlink1.getAllVehicles().size());
    }

    /**
	 * Tests the behavior of the buffer (e.g. that it does not accept too many vehicles).
	 *
	 * @author mrieser
	 */
    public void testBuffer() {
        Config conf = super.loadConfig(null);
        ScenarioImpl scenario = (ScenarioImpl) ScenarioUtils.createScenario(conf);
        conf.addQSimConfigGroup(new QSimConfigGroup());
        NetworkImpl network = (NetworkImpl) scenario.getNetwork();
        network.setCapacityPeriod(1.0);
        Node node1 = network.createAndAddNode(new IdImpl("1"), new CoordImpl(0, 0));
        Node node2 = network.createAndAddNode(new IdImpl("2"), new CoordImpl(1, 0));
        Node node3 = network.createAndAddNode(new IdImpl("3"), new CoordImpl(2, 0));
        Link link1 = network.createAndAddLink(new IdImpl("1"), node1, node2, 1.0, 1.0, 1.0, 1.0);
        Link link2 = network.createAndAddLink(new IdImpl("2"), node2, node3, 1.0, 1.0, 1.0, 1.0);
        QSim qsim = QSim.createQSimAndAddAgentSource(scenario, (EventsUtils.createEventsManager()));
        NetsimNetwork queueNetwork = qsim.getNetsimNetwork();
        QLinkImpl qlink = (QLinkImpl) queueNetwork.getNetsimLink(new IdImpl("1"));
        QVehicle v1 = new QVehicle(new VehicleImpl(new IdImpl("1"), new VehicleTypeImpl(new IdImpl("defaultVehicleType"))));
        PersonImpl p = new PersonImpl(new IdImpl("1"));
        PlanImpl plan = p.createAndAddPlan(true);
        try {
            plan.createAndAddActivity("h", link1.getId());
            LegImpl leg = plan.createAndAddLeg(TransportMode.car);
            NetworkRoute route = (NetworkRoute) ((PopulationFactoryImpl) scenario.getPopulation().getFactory()).createRoute(TransportMode.car, link1.getId(), link2.getId());
            leg.setRoute(route);
            route.setLinkIds(link1.getId(), null, link2.getId());
            leg.setRoute(route);
            plan.createAndAddActivity("w", link2.getId());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        PersonDriverAgentImpl pa1 = createAndInsertPersonDriverAgentImpl(p, qsim);
        v1.setDriver(pa1);
        pa1.setVehicle(v1);
        QVehicle v2 = new QVehicle(new VehicleImpl(new IdImpl("2"), new VehicleTypeImpl(new IdImpl("defaultVehicleType"))));
        PersonDriverAgentImpl pa2 = createAndInsertPersonDriverAgentImpl(p, qsim);
        v2.setDriver(pa2);
        pa2.setVehicle(v2);
        assertTrue(qlink.isNotOfferingVehicle());
        assertEquals(0, qlink.vehOnLinkCount());
        qlink.addFromIntersection(v1);
        assertEquals(1, qlink.vehOnLinkCount());
        assertTrue(qlink.isNotOfferingVehicle());
        qlink.doSimStep(1.0);
        assertEquals(0, qlink.vehOnLinkCount());
        assertFalse(qlink.isNotOfferingVehicle());
        qlink.addFromIntersection(v2);
        assertEquals(1, qlink.vehOnLinkCount());
        assertFalse(qlink.isNotOfferingVehicle());
        qlink.doSimStep(2.0);
        assertEquals(1, qlink.vehOnLinkCount());
        assertFalse(qlink.isNotOfferingVehicle());
        assertEquals(v1, qlink.popFirstVehicle());
        assertEquals(1, qlink.vehOnLinkCount());
        assertTrue(qlink.isNotOfferingVehicle());
        qlink.doSimStep(3.0);
        assertEquals(0, qlink.vehOnLinkCount());
        assertFalse(qlink.isNotOfferingVehicle());
        assertEquals(v2, qlink.popFirstVehicle());
        assertEquals(0, qlink.vehOnLinkCount());
        assertTrue(qlink.isNotOfferingVehicle());
        qlink.doSimStep(4.0);
        assertEquals(0, qlink.vehOnLinkCount());
        assertTrue(qlink.isNotOfferingVehicle());
    }

    public void testStorageSpaceDifferentVehicleSizes() {
        Fixture f = new Fixture();
        PersonImpl p = new PersonImpl(new IdImpl(5));
        p.addPlan(new PlanImpl());
        VehicleType vehType = new VehicleTypeImpl(new IdImpl("defaultVehicleType"));
        QVehicle veh1 = new QVehicle(new VehicleImpl(new IdImpl(1), vehType));
        veh1.setDriver(createAndInsertPersonDriverAgentImpl(p, f.sim));
        QVehicle veh25 = new QVehicle(new VehicleImpl(new IdImpl(2), vehType), 2.5);
        veh25.setDriver(createAndInsertPersonDriverAgentImpl(p, f.sim));
        QVehicle veh5 = new QVehicle(new VehicleImpl(new IdImpl(3), vehType), 5);
        veh5.setDriver(createAndInsertPersonDriverAgentImpl(p, f.sim));
        assertEquals("wrong initial storage capacity.", 10.0, f.qlink2.getSpaceCap(), EPSILON);
        f.qlink2.addFromIntersection(veh5);
        assertTrue(f.qlink2.hasSpace());
        f.qlink2.addFromIntersection(veh5);
        assertFalse(f.qlink2.hasSpace());
        assertTrue(f.qlink2.isNotOfferingVehicle());
        f.qlink2.doSimStep(5.0);
        assertTrue(f.qlink2.hasSpace());
        assertFalse(f.qlink2.isNotOfferingVehicle());
        f.qlink2.popFirstVehicle();
        assertTrue(f.qlink2.hasSpace());
        f.qlink2.addFromIntersection(veh25);
        f.qlink2.addFromIntersection(veh1);
        f.qlink2.addFromIntersection(veh1);
        assertTrue(f.qlink2.hasSpace());
        f.qlink2.addFromIntersection(veh1);
        assertFalse(f.qlink2.hasSpace());
        f.qlink2.doSimStep(6.0);
        assertTrue(f.qlink2.hasSpace());
        f.qlink2.addFromIntersection(veh1);
        f.qlink2.addFromIntersection(veh25);
        f.qlink2.addFromIntersection(veh1);
        assertFalse(f.qlink2.hasSpace());
    }

    public void testStuckEvents() {
        Scenario scenario = ScenarioUtils.createScenario(ConfigUtils.createConfig());
        scenario.getConfig().addQSimConfigGroup(new QSimConfigGroup());
        scenario.getConfig().getQSimConfigGroup().setStuckTime(100);
        scenario.getConfig().getQSimConfigGroup().setRemoveStuckVehicles(true);
        NetworkImpl network = (NetworkImpl) scenario.getNetwork();
        network.setCapacityPeriod(3600.0);
        Node node1 = network.createAndAddNode(new IdImpl("1"), new CoordImpl(0, 0));
        Node node2 = network.createAndAddNode(new IdImpl("2"), new CoordImpl(1, 0));
        Node node3 = network.createAndAddNode(new IdImpl("3"), new CoordImpl(1001, 0));
        Link link1 = network.createAndAddLink(new IdImpl("1"), node1, node2, 1.0, 1.0, 3600.0, 1.0);
        Link link2 = network.createAndAddLink(new IdImpl("2"), node2, node3, 10 * 7.5, 7.5, 3600.0, 1.0);
        Link link3 = network.createAndAddLink(new IdImpl("3"), node2, node2, 2 * 7.5, 7.5, 3600.0, 1.0);
        for (int i = 0; i < 5; i++) {
            PersonImpl p = new PersonImpl(new IdImpl(i));
            PlanImpl plan = new PlanImpl();
            Activity act = new ActivityImpl("h", link1.getId());
            act.setEndTime(7 * 3600);
            plan.addActivity(act);
            Leg leg = new LegImpl("car");
            NetworkRoute route = new LinkNetworkRouteImpl(link1.getId(), link2.getId());
            List<Id> links = new ArrayList<Id>();
            links.add(link3.getId());
            links.add(link3.getId());
            links.add(link3.getId());
            links.add(link3.getId());
            route.setLinkIds(link1.getId(), links, link2.getId());
            leg.setRoute(route);
            plan.addLeg(leg);
            plan.addActivity(new ActivityImpl("w", link2.getId()));
            p.addPlan(plan);
            scenario.getPopulation().addPerson(p);
        }
        QSim sim = QSim.createQSimAndAddAgentSource(scenario, EventsUtils.createEventsManager(), new DefaultQSimEngineFactory());
        EventsCollector collector = new EventsCollector();
        sim.getEventsManager().addHandler(collector);
        sim.run();
        int stuckCnt = 0;
        for (Event e : collector.getEvents()) {
            if (e instanceof AgentStuckEvent) {
                stuckCnt++;
            }
        }
        assertEquals(3, stuckCnt);
    }

    /**
	 * Initializes some commonly used data in the tests.
	 *
	 * @author mrieser
	 */
    private static final class Fixture {

        final ScenarioImpl scenario;

        final Link link1;

        final Link link2;

        final QNetwork queueNetwork;

        final QLinkImpl qlink1;

        final QLinkImpl qlink2;

        final Vehicle basicVehicle;

        final QSim sim;

        Fixture() {
            this.scenario = (ScenarioImpl) ScenarioUtils.createScenario(ConfigUtils.createConfig());
            this.scenario.getConfig().addQSimConfigGroup(new QSimConfigGroup());
            this.scenario.getConfig().getQSimConfigGroup().setStuckTime(100);
            this.scenario.getConfig().getQSimConfigGroup().setRemoveStuckVehicles(true);
            NetworkImpl network = (NetworkImpl) this.scenario.getNetwork();
            network.setCapacityPeriod(3600.0);
            Node node1 = network.createAndAddNode(new IdImpl("1"), new CoordImpl(0, 0));
            Node node2 = network.createAndAddNode(new IdImpl("2"), new CoordImpl(1, 0));
            Node node3 = network.createAndAddNode(new IdImpl("3"), new CoordImpl(1001, 0));
            this.link1 = network.createAndAddLink(new IdImpl("1"), node1, node2, 1.0, 1.0, 3600.0, 1.0);
            this.link2 = network.createAndAddLink(new IdImpl("2"), node2, node3, 10 * 7.5, 2.0 * 7.5, 3600.0, 1.0);
            sim = QSim.createQSimAndAddAgentSource(scenario, (EventsUtils.createEventsManager()));
            this.queueNetwork = (QNetwork) sim.getNetsimNetwork();
            this.qlink1 = (QLinkImpl) this.queueNetwork.getNetsimLink(new IdImpl("1"));
            this.qlink2 = (QLinkImpl) this.queueNetwork.getNetsimLink(new IdImpl("2"));
            this.basicVehicle = new VehicleImpl(new IdImpl("1"), new VehicleTypeImpl(new IdImpl("defaultVehicleType")));
        }
    }
}
