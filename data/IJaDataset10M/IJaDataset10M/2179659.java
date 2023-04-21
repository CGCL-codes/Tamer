package org.matsim.core.mobsim.qsim.pt;

import java.util.Arrays;
import java.util.Collections;
import junit.framework.TestCase;
import org.matsim.api.core.v01.TransportMode;
import org.matsim.api.core.v01.network.Node;
import org.matsim.api.core.v01.population.Activity;
import org.matsim.api.core.v01.population.Leg;
import org.matsim.api.core.v01.population.Person;
import org.matsim.api.core.v01.population.Plan;
import org.matsim.api.core.v01.population.PopulationFactory;
import org.matsim.core.basic.v01.IdImpl;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.config.groups.QSimConfigGroup;
import org.matsim.core.events.EventsUtils;
import org.matsim.core.mobsim.qsim.QSim;
import org.matsim.core.mobsim.qsim.agents.TransitAgent;
import org.matsim.core.network.NetworkImpl;
import org.matsim.core.scenario.ScenarioImpl;
import org.matsim.core.scenario.ScenarioUtils;
import org.matsim.core.utils.geometry.CoordImpl;
import org.matsim.pt.routes.ExperimentalTransitRoute;
import org.matsim.pt.transitSchedule.TransitScheduleFactoryImpl;
import org.matsim.pt.transitSchedule.api.TransitLine;
import org.matsim.pt.transitSchedule.api.TransitRoute;
import org.matsim.pt.transitSchedule.api.TransitRouteStop;
import org.matsim.pt.transitSchedule.api.TransitScheduleFactory;
import org.matsim.pt.transitSchedule.api.TransitStopFacility;

/**
 * @author mrieser
 */
public class TransitAgentTest extends TestCase {

    public void testAcceptLineRoute() {
        ScenarioImpl scenario = (ScenarioImpl) ScenarioUtils.createScenario(ConfigUtils.createConfig());
        scenario.getConfig().addQSimConfigGroup(new QSimConfigGroup());
        NetworkImpl network = (NetworkImpl) scenario.getNetwork();
        Node node1 = network.createAndAddNode(new IdImpl("1"), new CoordImpl(0, 0));
        Node node2 = network.createAndAddNode(new IdImpl("2"), new CoordImpl(1000, 0));
        Node node3 = network.createAndAddNode(new IdImpl("3"), new CoordImpl(2000, 0));
        network.createAndAddLink(new IdImpl("1"), node1, node2, 1000.0, 10.0, 3600.0, 1);
        network.createAndAddLink(new IdImpl("2"), node2, node3, 1000.0, 10.0, 3600.0, 1);
        TransitScheduleFactory builder = new TransitScheduleFactoryImpl();
        PopulationFactory pb = scenario.getPopulation().getFactory();
        Person person = pb.createPerson(scenario.createId("1"));
        Plan plan = pb.createPlan();
        person.addPlan(plan);
        Activity homeAct = pb.createActivityFromLinkId("home", scenario.createId("1"));
        Leg leg = pb.createLeg(TransportMode.pt);
        TransitStopFacility stopFacility1 = builder.createTransitStopFacility(scenario.createId("1"), scenario.createCoord(100, 100), false);
        TransitStopFacility stopFacility2 = builder.createTransitStopFacility(scenario.createId("2"), scenario.createCoord(900, 100), false);
        TransitRouteStop stop1 = builder.createTransitRouteStop(stopFacility1, 50, 60);
        TransitRouteStop stop2 = builder.createTransitRouteStop(stopFacility2, 100, 110);
        TransitLine line1 = builder.createTransitLine(scenario.createId("L1"));
        TransitLine line2 = builder.createTransitLine(scenario.createId("L2"));
        TransitRoute route1a = builder.createTransitRoute(scenario.createId("1a"), null, Arrays.asList(stop1, stop2), TransportMode.pt);
        TransitRoute route1b = builder.createTransitRoute(scenario.createId("1b"), null, Collections.<TransitRouteStop>emptyList(), TransportMode.pt);
        TransitRoute route2a = builder.createTransitRoute(scenario.createId("2a"), null, Collections.<TransitRouteStop>emptyList(), TransportMode.pt);
        leg.setRoute(new ExperimentalTransitRoute(stopFacility1, line1, route1a, stopFacility2));
        Activity workAct = pb.createActivityFromLinkId("work", scenario.createId("2"));
        plan.addActivity(homeAct);
        plan.addLeg(leg);
        plan.addActivity(workAct);
        QSim sim = QSim.createQSimAndAddAgentSource(scenario, EventsUtils.createEventsManager());
        TransitAgent agent = TransitAgent.createTransitAgent(person, sim);
        sim.insertAgentIntoMobsim(agent);
        agent.endActivityAndComputeNextState(10);
        assertTrue(agent.getEnterTransitRoute(line1, route1a, route1a.getStops()));
        assertFalse(agent.getEnterTransitRoute(line1, route1b, route1b.getStops()));
        assertFalse(agent.getEnterTransitRoute(line2, route2a, route2a.getStops()));
        assertTrue(agent.getEnterTransitRoute(line1, route1a, route1a.getStops()));
    }

    public void testArriveAtStop() {
        ScenarioImpl scenario = (ScenarioImpl) ScenarioUtils.createScenario(ConfigUtils.createConfig());
        scenario.getConfig().addQSimConfigGroup(new QSimConfigGroup());
        NetworkImpl network = (NetworkImpl) scenario.getNetwork();
        Node node1 = network.createAndAddNode(new IdImpl("1"), new CoordImpl(0, 0));
        Node node2 = network.createAndAddNode(new IdImpl("2"), new CoordImpl(1000, 0));
        Node node3 = network.createAndAddNode(new IdImpl("3"), new CoordImpl(2000, 0));
        network.createAndAddLink(new IdImpl("1"), node1, node2, 1000.0, 10.0, 3600.0, 1);
        network.createAndAddLink(new IdImpl("2"), node2, node3, 1000.0, 10.0, 3600.0, 1);
        TransitScheduleFactory builder = new TransitScheduleFactoryImpl();
        PopulationFactory pb = scenario.getPopulation().getFactory();
        Person person = pb.createPerson(scenario.createId("1"));
        Plan plan = pb.createPlan();
        person.addPlan(plan);
        Activity homeAct = pb.createActivityFromLinkId("home", scenario.createId("1"));
        Leg leg = pb.createLeg(TransportMode.pt);
        TransitStopFacility stop1 = builder.createTransitStopFacility(scenario.createId("1"), scenario.createCoord(100, 100), false);
        TransitStopFacility stop2 = builder.createTransitStopFacility(scenario.createId("2"), scenario.createCoord(900, 100), false);
        TransitStopFacility stop3 = builder.createTransitStopFacility(scenario.createId("3"), scenario.createCoord(1900, 100), false);
        TransitLine line1 = builder.createTransitLine(scenario.createId("L1"));
        leg.setRoute(new ExperimentalTransitRoute(stop1, line1, null, stop2));
        Activity workAct = pb.createActivityFromLinkId("work", scenario.createId("2"));
        plan.addActivity(homeAct);
        plan.addLeg(leg);
        plan.addActivity(workAct);
        QSim sim = QSim.createQSimAndAddAgentSource(scenario, EventsUtils.createEventsManager());
        TransitAgent agent = TransitAgent.createTransitAgent(person, sim);
        sim.insertAgentIntoMobsim(agent);
        agent.endActivityAndComputeNextState(10);
        assertFalse(agent.getExitAtStop(stop1));
        assertTrue(agent.getExitAtStop(stop2));
        assertFalse(agent.getExitAtStop(stop3));
        assertTrue(agent.getExitAtStop(stop2));
    }
}
