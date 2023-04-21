package playground.andreas.bvgAna;

import java.util.TreeMap;
import java.util.TreeSet;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.TransportMode;
import org.matsim.core.api.experimental.events.EventsManager;
import org.matsim.core.basic.v01.IdImpl;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.events.EventsReaderXMLv1;
import org.matsim.core.events.EventsUtils;
import org.matsim.core.network.MatsimNetworkReader;
import org.matsim.core.population.MatsimPopulationReader;
import org.matsim.core.population.PopulationFactoryImpl;
import org.matsim.core.population.PopulationImpl;
import org.matsim.core.scenario.ScenarioImpl;
import org.matsim.core.scenario.ScenarioUtils;
import org.matsim.pt.routes.ExperimentalTransitRouteFactory;
import org.matsim.pt.transitSchedule.api.TransitScheduleReader;
import org.matsim.vehicles.VehicleReaderV1;
import playground.andreas.bvgAna.level1.AgentId2EnterLeaveVehicleEventHandler;
import playground.andreas.bvgAna.level1.AgentId2PtTripTravelTimeMap;
import playground.andreas.bvgAna.level1.PersonEnterLeaveVehicle2ActivityHandler;
import playground.andreas.bvgAna.level1.StopId2LineId2Pulk;
import playground.andreas.bvgAna.level1.StopId2PersonEnterLeaveVehicleHandler;
import playground.andreas.bvgAna.level1.VehDelayAtStopHistogram;
import playground.andreas.bvgAna.level1.VehId2DelayAtStopMap;
import playground.andreas.bvgAna.level1.VehId2OccupancyHandler;
import playground.andreas.bvgAna.level1.VehId2PersonEnterLeaveVehicleMap;
import playground.andreas.bvgAna.level2.StopId2DelayOfLine24hMap;
import playground.andreas.bvgAna.level2.StopId2RemainSeatedDataMap;
import playground.andreas.bvgAna.level2.VehId2AgentIds;
import playground.andreas.bvgAna.level2.VehId2LoadMap;
import playground.andreas.bvgAna.level3.AgentId2StopDifferenceMap;
import playground.andreas.bvgAna.level4.StopId2MissedVehMap;
import playground.andreas.bvgAna.level4.StopId2MissedVehMapData;

/**
 * Simple test class
 *
 * @author aneumann
 */
public class TestMain {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        String eventsFile = "./bvg.run128.25pct.100.events.xml.gz";
        String plansFile = "./bvg.run128.25pct.100.plans.selected.xml.gz";
        String netFile = "./network.final.xml.gz";
        String transitScheduleFile = "./transitSchedule.xml.gz";
        String vehDefinitionFile = "./transitVehicles.final.xml.gz";
        String outFile = "./out.txt";
        EventsManager eventsManager = EventsUtils.createEventsManager();
        EventsReaderXMLv1 reader = new EventsReaderXMLv1(eventsManager);
        ScenarioImpl sc = (ScenarioImpl) ScenarioUtils.createScenario(ConfigUtils.createConfig());
        sc.getConfig().getModule("scenario").addParam("useTransit", "true");
        sc.getConfig().getModule("scenario").addParam("useVehicles", "true");
        ((PopulationFactoryImpl) sc.getPopulation().getFactory()).setRouteFactory(TransportMode.pt, new ExperimentalTransitRouteFactory());
        new MatsimNetworkReader(sc).readFile(netFile);
        final PopulationImpl plans = (PopulationImpl) sc.getPopulation();
        MatsimPopulationReader popReader = new MatsimPopulationReader(sc);
        popReader.readFile(plansFile);
        new TransitScheduleReader(sc).readFile(transitScheduleFile);
        new VehicleReaderV1(sc.getVehicles()).readFile(vehDefinitionFile);
        TreeSet<Id> agentIds = new TreeSet<Id>();
        agentIds.add(new IdImpl("1000"));
        agentIds.add(new IdImpl("10001"));
        agentIds.add(new IdImpl("10002"));
        agentIds.add(new IdImpl("2176"));
        agentIds.add(new IdImpl("182"));
        AgentId2StopDifferenceMap comp = new AgentId2StopDifferenceMap(plans, agentIds);
        eventsManager.addHandler(comp);
        TreeSet<Id> stopIds = new TreeSet<Id>();
        stopIds.add(new IdImpl("812013.1"));
        stopIds.add(new IdImpl("792200.4"));
        stopIds.add(new IdImpl("792050.2"));
        stopIds.add(new IdImpl("801040.1"));
        stopIds.add(new IdImpl("804070.2"));
        StopId2PersonEnterLeaveVehicleHandler stophandler = new StopId2PersonEnterLeaveVehicleHandler(stopIds);
        PersonEnterLeaveVehicle2ActivityHandler enterLeaveHandler = new PersonEnterLeaveVehicle2ActivityHandler(agentIds);
        AgentId2EnterLeaveVehicleEventHandler aid2elhandler = new AgentId2EnterLeaveVehicleEventHandler(agentIds);
        VehId2OccupancyHandler veh2occu = new VehId2OccupancyHandler();
        VehId2LoadMap veh2load = new VehId2LoadMap(sc.getVehicles());
        StopId2RemainSeatedDataMap remainSeated = new StopId2RemainSeatedDataMap();
        AgentId2PtTripTravelTimeMap a2ptleg = new AgentId2PtTripTravelTimeMap(agentIds);
        VehId2DelayAtStopMap v2delay = new VehId2DelayAtStopMap();
        StopId2DelayOfLine24hMap s224h = new StopId2DelayOfLine24hMap();
        VehId2PersonEnterLeaveVehicleMap v2ELM = new VehId2PersonEnterLeaveVehicleMap();
        VehId2AgentIds v2agid = new VehId2AgentIds();
        StopId2LineId2Pulk s2pulk = new StopId2LineId2Pulk();
        StopId2MissedVehMap s2mv = new StopId2MissedVehMap(plans);
        eventsManager.addHandler(s2mv);
        VehDelayAtStopHistogram dH = new VehDelayAtStopHistogram(24 * 60);
        reader.parse(eventsFile);
        TreeMap<Id, StopId2MissedVehMapData> s2mvR = s2mv.getStopId2StopId2MissedVehMapDataMap();
        s2mv.writeResultsToFile(outFile);
        System.out.println("Waiting");
    }
}
