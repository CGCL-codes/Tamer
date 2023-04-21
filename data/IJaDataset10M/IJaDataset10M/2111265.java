package playground.mrieser.pt.demo;

import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.TransportMode;
import org.matsim.contrib.otfvis.OTFVis;
import org.matsim.core.api.experimental.events.EventsManager;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.config.groups.QSimConfigGroup;
import org.matsim.core.events.EventsUtils;
import org.matsim.core.events.algorithms.EventWriterXML;
import org.matsim.core.mobsim.qsim.QSim;
import org.matsim.core.network.MatsimNetworkReader;
import org.matsim.core.network.NetworkImpl;
import org.matsim.core.population.PopulationFactoryImpl;
import org.matsim.core.scenario.ScenarioImpl;
import org.matsim.core.scenario.ScenarioUtils;
import org.matsim.pt.routes.ExperimentalTransitRouteFactory;
import org.matsim.pt.transitSchedule.TransitScheduleReaderV1;
import org.matsim.pt.transitSchedule.api.TransitSchedule;
import org.matsim.pt.utils.CreateVehiclesForSchedule;
import org.matsim.vis.otfvis.OTFClientLive;
import org.matsim.vis.otfvis.OnTheFlyServer;
import org.xml.sax.SAXException;

/**
 * Visualizes a transit schedule and simulates the transit vehicle's movements.
 *
 * @author mrieser
 */
public class ScenarioPlayer {

    public static void play(final Scenario scenario, final EventsManager events) {
        scenario.getConfig().getQSimConfigGroup().setSnapshotStyle("queue");
        final QSim sim = QSim.createQSimAndAddAgentSource(scenario, events);
        OnTheFlyServer server = OTFVis.startServerAndRegisterWithQSim(scenario.getConfig(), scenario, events, sim);
        OTFClientLive.run(scenario.getConfig(), server);
        sim.run();
    }

    /**
	 * @param args
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 */
    public static void main(final String[] args) throws SAXException, ParserConfigurationException, IOException {
        Scenario scenario = ScenarioUtils.createScenario(ConfigUtils.createConfig());
        scenario.getConfig().addQSimConfigGroup(new QSimConfigGroup());
        scenario.getConfig().getQSimConfigGroup().setSnapshotPeriod(0.0);
        scenario.getConfig().scenario().setUseTransit(true);
        scenario.getConfig().scenario().setUseVehicles(true);
        NetworkImpl network = (NetworkImpl) scenario.getNetwork();
        ((PopulationFactoryImpl) scenario.getPopulation().getFactory()).setRouteFactory(TransportMode.pt, new ExperimentalTransitRouteFactory());
        new MatsimNetworkReader(scenario).readFile("test/scenarios/pt-tutorial/multimodalnetwork.xml");
        TransitSchedule schedule = ((ScenarioImpl) scenario).getTransitSchedule();
        new TransitScheduleReaderV1(schedule, network, scenario).parse("test/scenarios/pt-tutorial/transitschedule.xml");
        new CreateVehiclesForSchedule(schedule, ((ScenarioImpl) scenario).getVehicles()).run();
        final EventsManager events = EventsUtils.createEventsManager();
        EventWriterXML writer = new EventWriterXML("./transitEvents.xml");
        events.addHandler(writer);
        play(scenario, events);
        writer.closeFile();
    }
}
