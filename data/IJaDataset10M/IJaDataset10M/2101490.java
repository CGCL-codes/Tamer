package org.matsim.pt.transitSchedule;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.network.Node;
import org.matsim.core.basic.v01.IdImpl;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.network.NetworkImpl;
import org.matsim.core.population.routes.LinkNetworkRouteImpl;
import org.matsim.core.population.routes.NetworkRoute;
import org.matsim.core.scenario.ScenarioImpl;
import org.matsim.core.scenario.ScenarioUtils;
import org.matsim.core.utils.geometry.CoordImpl;
import org.matsim.core.utils.misc.Time;
import org.matsim.pt.transitSchedule.api.Departure;
import org.matsim.pt.transitSchedule.api.TransitLine;
import org.matsim.pt.transitSchedule.api.TransitRoute;
import org.matsim.pt.transitSchedule.api.TransitRouteStop;
import org.matsim.pt.transitSchedule.api.TransitSchedule;
import org.matsim.pt.transitSchedule.api.TransitScheduleFactory;
import org.matsim.pt.transitSchedule.api.TransitStopFacility;
import org.matsim.testcases.MatsimTestCase;
import org.xml.sax.SAXException;

/**
 * Tests the file format <code>transitSchedule_v1.dtd</code> by creating a
 * transit schedule with different attributes, writing it out to a file in
 * the specified file format, reads it back in and compares the actual values
 * with the previously set ones.
 *
 * @author mrieser
 */
public class TransitScheduleFormatV1Test extends MatsimTestCase {

    public void testWriteRead() throws IOException, SAXException, ParserConfigurationException {
        NetworkImpl network = NetworkImpl.createNetwork();
        Node n1 = network.createAndAddNode(new IdImpl("1"), new CoordImpl(0, 0));
        Node n2 = network.createAndAddNode(new IdImpl("2"), new CoordImpl(0, 0));
        Node n3 = network.createAndAddNode(new IdImpl("3"), new CoordImpl(0, 0));
        Node n4 = network.createAndAddNode(new IdImpl("4"), new CoordImpl(0, 0));
        Node n5 = network.createAndAddNode(new IdImpl("5"), new CoordImpl(0, 0));
        Link l1 = network.createAndAddLink(new IdImpl("1"), n1, n2, 1000, 10, 3600, 1.0);
        Link l2 = network.createAndAddLink(new IdImpl("2"), n2, n3, 1000, 10, 3600, 1.0);
        Link l3 = network.createAndAddLink(new IdImpl("3"), n3, n4, 1000, 10, 3600, 1.0);
        Link l4 = network.createAndAddLink(new IdImpl("4"), n4, n5, 1000, 10, 3600, 1.0);
        TransitScheduleFactory builder = new TransitScheduleFactoryImpl();
        TransitSchedule schedule1 = builder.createTransitSchedule();
        TransitStopFacility stop1 = builder.createTransitStopFacility(new IdImpl("stop1"), new CoordImpl(0, 0), false);
        TransitStopFacility stop2 = builder.createTransitStopFacility(new IdImpl("stop2"), new CoordImpl(1000, 0), true);
        TransitStopFacility stop3 = builder.createTransitStopFacility(new IdImpl("stop3"), new CoordImpl(1000, 1000), true);
        TransitStopFacility stop4 = builder.createTransitStopFacility(new IdImpl("stop4"), new CoordImpl(0, 1000), false);
        stop2.setName("S + U Nirgendwo");
        stop4.setName("Irgendwo");
        schedule1.addStopFacility(stop1);
        schedule1.addStopFacility(stop2);
        schedule1.addStopFacility(stop3);
        schedule1.addStopFacility(stop4);
        List<TransitRouteStop> stops = new ArrayList<TransitRouteStop>(4);
        stops.add(builder.createTransitRouteStop(stop1, Time.UNDEFINED_TIME, 0));
        stops.add(builder.createTransitRouteStop(stop2, 90, 120));
        TransitRouteStop routeStop3 = builder.createTransitRouteStop(stop3, Time.UNDEFINED_TIME, 300);
        routeStop3.setAwaitDepartureTime(true);
        stops.add(routeStop3);
        stops.add(builder.createTransitRouteStop(stop4, 400, Time.UNDEFINED_TIME));
        TransitRoute route1 = builder.createTransitRoute(new IdImpl(1), null, stops, "bus");
        route1.setDescription("Just a comment.");
        route1.addDeparture(builder.createDeparture(new IdImpl("2"), 7.0 * 3600));
        Departure dep = builder.createDeparture(new IdImpl("4"), 7.0 * 3600 + 300);
        dep.setVehicleId(new IdImpl("86"));
        route1.addDeparture(dep);
        dep = builder.createDeparture(new IdImpl("7"), 7.0 * 3600 + 600);
        dep.setVehicleId(new IdImpl("19"));
        route1.addDeparture(dep);
        TransitLine line1 = builder.createTransitLine(new IdImpl("8"));
        line1.addRoute(route1);
        schedule1.addTransitLine(line1);
        String filename = getOutputDirectory() + "scheduleNoRoute.xml";
        new TransitScheduleWriterV1(schedule1).write(filename);
        TransitScheduleFactory builder2 = new TransitScheduleFactoryImpl();
        TransitSchedule schedule2 = builder2.createTransitSchedule();
        new TransitScheduleReaderV1(schedule2, network, ((ScenarioImpl) ScenarioUtils.createScenario(ConfigUtils.createConfig()))).readFile(filename);
        assertEquals(schedule1, schedule2);
        NetworkRoute route = new LinkNetworkRouteImpl(l1.getId(), l4.getId());
        List<Id> links = new ArrayList<Id>(2);
        links.add(l2.getId());
        links.add(l3.getId());
        route.setLinkIds(l1.getId(), links, l4.getId());
        TransitRoute route2 = builder.createTransitRoute(new IdImpl(2), route, stops, "bus");
        line1.addRoute(route2);
        stop1.setLinkId(l1.getId());
        filename = getOutputDirectory() + "scheduleWithRoute.xml";
        new TransitScheduleWriterV1(schedule1).write(filename);
        TransitScheduleFactory builder3 = new TransitScheduleFactoryImpl();
        TransitSchedule schedule3 = builder3.createTransitSchedule();
        new TransitScheduleReaderV1(schedule3, network, ((ScenarioImpl) ScenarioUtils.createScenario(ConfigUtils.createConfig()))).readFile(filename);
        assertEquals(schedule1, schedule3);
    }

    private static void assertEquals(final TransitSchedule expected, final TransitSchedule actual) {
        assertEquals("different number of stopFacilities.", expected.getFacilities().size(), actual.getFacilities().size());
        for (TransitStopFacility stopE : expected.getFacilities().values()) {
            TransitStopFacility stopA = actual.getFacilities().get(stopE.getId());
            assertNotNull("stopFacility not found: " + stopE.getId().toString(), stopA);
            assertEquals("different x coordinates.", stopE.getCoord().getX(), stopA.getCoord().getX(), EPSILON);
            assertEquals("different y coordinates.", stopE.getCoord().getY(), stopA.getCoord().getY(), EPSILON);
            assertEquals("different link information.", stopE.getLinkId(), stopA.getLinkId());
            assertEquals("different isBlocking.", stopE.getIsBlockingLane(), stopA.getIsBlockingLane());
            assertEquals("different names.", stopE.getName(), stopA.getName());
        }
        assertEquals("different number of transitLines.", expected.getTransitLines().size(), actual.getTransitLines().size());
        for (TransitLine lineE : expected.getTransitLines().values()) {
            TransitLine lineA = actual.getTransitLines().get(lineE.getId());
            assertNotNull("transit line not found: " + lineE.getId().toString(), lineA);
            assertEquals("different number of routes in line.", lineE.getRoutes().size(), lineA.getRoutes().size());
            for (TransitRoute routeE : lineE.getRoutes().values()) {
                TransitRoute routeA = lineA.getRoutes().get(routeE.getId());
                assertNotNull("transit route not found: " + routeE.getId().toString(), routeA);
                assertEquals("different route descriptions.", routeE.getDescription(), routeA.getDescription());
                assertEquals("different number of stops.", routeE.getStops().size(), routeA.getStops().size());
                for (int i = 0, n = routeE.getStops().size(); i < n; i++) {
                    TransitRouteStop stopE = routeE.getStops().get(i);
                    TransitRouteStop stopA = routeA.getStops().get(i);
                    assertNotNull("stop not found", stopA);
                    assertEquals("different stop facilities.", stopE.getStopFacility().getId(), stopA.getStopFacility().getId());
                    assertEquals("different arrival delay.", stopE.getArrivalOffset(), stopA.getArrivalOffset(), EPSILON);
                    assertEquals("different departure delay.", stopE.getDepartureOffset(), stopA.getDepartureOffset(), EPSILON);
                    assertEquals("different awaitDepartureTime.", stopE.isAwaitDepartureTime(), stopA.isAwaitDepartureTime());
                }
                NetworkRoute netRouteE = routeE.getRoute();
                if (netRouteE == null) {
                    assertNull("bad network route, must be null.", routeA.getRoute());
                } else {
                    NetworkRoute netRouteA = routeA.getRoute();
                    assertNotNull("bad network route, must not be null.", netRouteA);
                    assertEquals("wrong start link.", netRouteE.getStartLinkId(), netRouteA.getStartLinkId());
                    assertEquals("wrong end link.", netRouteE.getEndLinkId(), netRouteA.getEndLinkId());
                    List<Id> linkIdsE = netRouteE.getLinkIds();
                    List<Id> linkIdsA = netRouteA.getLinkIds();
                    for (int i = 0, n = linkIdsE.size(); i < n; i++) {
                        assertEquals("wrong link in network route", linkIdsE.get(i), linkIdsA.get(i));
                    }
                }
                assertEquals("different number of departures in route.", routeE.getDepartures().size(), routeA.getDepartures().size());
                for (Departure departureE : routeE.getDepartures().values()) {
                    Departure departureA = routeA.getDepartures().get(departureE.getId());
                    assertNotNull("departure not found: " + departureE.getId().toString(), departureA);
                    assertEquals("different departure times.", departureE.getDepartureTime(), departureA.getDepartureTime(), EPSILON);
                    assertEquals("different vehicle ids.", departureE.getVehicleId(), departureA.getVehicleId());
                }
            }
        }
    }
}
