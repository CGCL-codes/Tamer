package playground.david.vis;

import org.matsim.config.Config;
import org.matsim.events.Events;
import org.matsim.gbl.Gbl;
import org.matsim.mobsim.QueueNetworkLayer;
import org.matsim.network.MatsimNetworkReader;
import org.matsim.plans.MatsimPlansReader;
import org.matsim.plans.Plans;
import org.matsim.plans.PlansReaderI;
import org.matsim.utils.misc.Time;
import org.matsim.world.World;
import playground.david.vis.executables.OnTheFlyQueueSim;

/**
 * @author DS
 *
 */
public class OnTheFlyQueueSimSWISS2_3Mill {

    public static void main(String[] args) {
        OnTheFlyQueueSim sim;
        QueueNetworkLayer net;
        Plans population;
        Events events;
        String netFileName = "../../tmp/studies/ivtch/network.xml";
        String popFileName = "../../tmp/studies/ivtch/all_plans.xml.gz";
        args = new String[] { "../../tmp/studies/ivtch/config.xml" };
        Gbl.createConfig(args);
        Gbl.startMeasurement();
        Config config = Gbl.getConfig();
        config.setParam("global", "localDTDBase", "dtd/");
        World world = Gbl.getWorld();
        net = new QueueNetworkLayer();
        new MatsimNetworkReader(net).readFile(netFileName);
        world.setNetworkLayer(net);
        Gbl.printElapsedTime();
        population = new Plans();
        PlansReaderI plansReader = new MatsimPlansReader(population);
        plansReader.readFile(popFileName);
        world.setPopulation(population);
        events = new Events();
        world.setEvents(events);
        config.simulation().setStartTime(Time.parseTime("00:00:00"));
        sim = new OnTheFlyQueueSim(net, population, events);
        sim.setOtfwriter(new OTFQuadFileHandler.Writer(600, net, "output/OTFQuadfileSCHWEIZ2.3.mvi"));
        sim.run();
        Gbl.printElapsedTime();
    }
}
