package playground.gregor.multidestpeds.io;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.network.Network;
import org.matsim.api.core.v01.network.Node;
import org.matsim.core.api.experimental.events.EventsFactory;
import org.matsim.core.api.experimental.events.EventsManager;
import org.matsim.core.events.EventsUtils;
import org.matsim.core.events.algorithms.EventWriterXML;
import org.matsim.core.network.NetworkImpl;
import org.matsim.core.router.Dijkstra;
import org.matsim.core.router.costcalculators.TravelCostCalculatorFactoryImpl;
import org.matsim.core.router.util.LeastCostPathCalculator;
import org.matsim.core.router.util.LeastCostPathCalculator.Path;
import org.matsim.core.router.util.PersonalizableTravelDisutility;
import org.matsim.core.trafficmonitoring.FreeSpeedTravelTimeCalculator;
import org.matsim.core.utils.geometry.geotools.MGC;
import playground.gregor.sim2d_v2.events.XYVxVyEvent;
import playground.gregor.sim2d_v2.events.XYVxVyEventImpl;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;

/**
 * @author laemmel
 * 
 */
public class Mat2XYVxVyEvents {

    private static final double COS_LEFT = Math.cos(Math.PI / 2);

    private static final double SIN_LEFT = Math.sin(Math.PI / 2);

    private static final double COS_RIGHT = Math.cos(-Math.PI / 2);

    private static final double SIN_RIGHT = Math.sin(-Math.PI / 2);

    private HashMap<Id, LineString> finishLines;

    private final GeometryFactory geofac = new GeometryFactory();

    private final Scenario sc;

    private final String inputDir;

    private final String inputMat;

    private final String mode;

    public Mat2XYVxVyEvents(Scenario sc, String inputDir, String inputMat, String mode) {
        this.sc = sc;
        this.mode = mode;
        this.inputDir = inputDir;
        this.inputMat = inputMat;
    }

    public void run() {
        Importer imp = new Importer();
        try {
            imp.read(this.inputMat, this.sc);
        } catch (IOException e) {
            e.printStackTrace();
        }
        createFinishLines(this.sc);
        EventWriterXML writer = new EventWriterXML(this.inputDir + "/events.xml");
        EventsManager manager = EventsUtils.createEventsManager();
        XYZEvents2Plan planGen;
        if (this.mode.equals("car") || this.mode.equals("walkPrioQ")) {
            planGen = new XYZEvents2Plan(this.sc, this.mode, "/Users/laemmel/devel/gr90/input/events_walk2d.xml.gz");
        } else {
            planGen = new XYZEvents2Plan(this.sc, this.mode);
        }
        manager.addHandler(writer);
        manager.addHandler(planGen);
        EventsFactory fac = manager.getFactory();
        List<Ped> peds = imp.getPeds();
        List<Double> timeSteps = imp.getTimeSteps();
        Network network = this.sc.getNetwork();
        FreeSpeedTravelTimeCalculator fs = new FreeSpeedTravelTimeCalculator();
        PersonalizableTravelDisutility cost = new TravelCostCalculatorFactoryImpl().createTravelDisutility(fs, this.sc.getConfig().planCalcScore());
        LeastCostPathCalculator dijkstra = new Dijkstra(network, cost, fs);
        Set<Ped> excl = new HashSet<Ped>();
        for (int i = 0; i < timeSteps.size(); i++) {
            double time = timeSteps.get(i);
            for (Ped ped : peds) {
                if (excl.contains(ped)) {
                    continue;
                }
                if (time < ped.depart || time > ped.arrived) {
                    continue;
                }
                if (time == ped.depart) {
                    try {
                        calculateRoute(time, ped, this.sc, dijkstra);
                    } catch (Exception e) {
                        excl.add(ped);
                        continue;
                    }
                    ped.lastPos = ped.coords.get(time);
                    manager.processEvent(fac.createAgentDepartureEvent(time, ped.id, ped.path.links.get(ped.currLink).getId(), "walk2d"));
                }
                Id id = ped.id;
                Coordinate c = ped.coords.get(time);
                Coordinate v = ped.velocities.get(time);
                XYVxVyEvent ev = new XYVxVyEventImpl(id, c, v.x, v.y, time);
                manager.processEvent(ev);
                if (time == ped.arrived) {
                    Id linkId = ped.path.links.get(ped.path.links.size() - 1).getId();
                    manager.processEvent(fac.createAgentArrivalEvent(time, ped.id, linkId, "walk2d"));
                } else if (checkForNextLink(ped, c)) {
                    manager.processEvent(fac.createLinkLeaveEvent(time, id, ped.path.links.get(ped.currLink).getId(), null));
                    if (ped.currLink < ped.path.links.size() - 2) {
                        ped.currLink++;
                        manager.processEvent(fac.createLinkEnterEvent(time, id, ped.path.links.get(ped.currLink).getId(), null));
                    }
                }
                ped.lastPos = c;
            }
        }
        writer.closeFile();
    }

    private boolean checkForNextLink(Ped ped, Coordinate c) {
        Id currLinkId = ped.path.links.get(ped.currLink).getId();
        LineString finishLine = this.finishLines.get(currLinkId);
        LineString trajectory = this.geofac.createLineString(new Coordinate[] { ped.lastPos, c });
        if (trajectory.crosses(finishLine)) {
            return true;
        }
        return false;
    }

    private void calculateRoute(double time, Ped ped, Scenario sc, LeastCostPathCalculator dijkstra) {
        Id startLink = getLinkId(ped.coords.get(time), ped.velocities.get(time), sc);
        Node from = sc.getNetwork().getLinks().get(startLink).getFromNode();
        Node to = null;
        Id endLink = null;
        if (ped.id.toString().contains("g")) {
            Coordinate toC = new Coordinate(2, -10);
            endLink = getLinkId(toC, ped.velocities.get(ped.arrived), sc);
            to = sc.getNetwork().getLinks().get(endLink).getToNode();
        } else {
            Coordinate toC = new Coordinate(10, -2);
            endLink = getLinkId(toC, ped.velocities.get(ped.arrived), sc);
            to = sc.getNetwork().getLinks().get(endLink).getToNode();
        }
        Path path = dijkstra.calcLeastCostPath(from, to, 0);
        boolean routeAgain = false;
        if (!path.links.get(0).getId().equals(startLink)) {
            Link oldStart = sc.getNetwork().getLinks().get(startLink);
            Node oldTo = oldStart.getToNode();
            for (Link l : from.getInLinks().values()) {
                if (l.getFromNode().equals(oldTo)) {
                    startLink = l.getId();
                    break;
                }
            }
            from = sc.getNetwork().getLinks().get(startLink).getFromNode();
            routeAgain = true;
        }
        if (!path.links.get(path.links.size() - 1).getId().equals(endLink)) {
            Link oldEnd = sc.getNetwork().getLinks().get(endLink);
            Node oldFrom = oldEnd.getFromNode();
            for (Link l : to.getOutLinks().values()) {
                if (l.getToNode().equals(oldFrom)) {
                    endLink = l.getId();
                    break;
                }
            }
            to = sc.getNetwork().getLinks().get(endLink).getToNode();
            routeAgain = true;
        }
        if (routeAgain) {
            path = dijkstra.calcLeastCostPath(from, to, 0);
        }
        ped.path = path;
    }

    private Id getLinkId(Coordinate loc, Coordinate vel, Scenario sc) {
        Link l1 = ((NetworkImpl) sc.getNetwork()).getNearestNode(MGC.coordinate2Coord(loc)).getOutLinks().values().iterator().next();
        Id id = l1.getId();
        return id;
    }

    private void createFinishLines(Scenario sc) {
        GeometryFactory geofac = new GeometryFactory();
        this.finishLines = new HashMap<Id, LineString>();
        for (Link link : sc.getNetwork().getLinks().values()) {
            Coordinate to = MGC.coord2Coordinate(link.getToNode().getCoord());
            Coordinate from = MGC.coord2Coordinate(link.getFromNode().getCoord());
            Coordinate c = new Coordinate(from.x - to.x, from.y - to.y);
            double scale = 30 / Math.sqrt(Math.pow(c.x, 2) + Math.pow(c.y, 2));
            c.x *= scale;
            c.y *= scale;
            Coordinate c1 = new Coordinate(COS_LEFT * c.x + SIN_LEFT * c.y, -SIN_LEFT * c.x + COS_LEFT * c.y);
            c1.x += to.x;
            c1.y += to.y;
            Coordinate c2 = new Coordinate(COS_RIGHT * c.x + SIN_RIGHT * c.y, -SIN_RIGHT * c.x + COS_RIGHT * c.y);
            c2.x += to.x;
            c2.y += to.y;
            LineString ls = geofac.createLineString(new Coordinate[] { c1, c2 });
            this.finishLines.put(link.getId(), ls);
        }
    }
}
