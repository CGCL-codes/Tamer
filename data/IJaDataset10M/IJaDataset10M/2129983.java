package tutorial.example3;

import org.matsim.api.core.v01.Scenario;
import org.matsim.core.api.ScenarioLoader;
import org.matsim.core.events.Events;
import org.matsim.core.events.algorithms.EventWriterTXT;
import org.matsim.core.mobsim.queuesim.QueueSimulation;
import org.matsim.core.network.MatsimNetworkReader;
import org.matsim.core.population.MatsimPopulationReader;
import org.matsim.vis.netvis.NetVis;

public class MyControler3 {

    public static void main(final String[] args) {
        final String netFilename = "./examples/equil/network.xml";
        final String plansFilename = "./examples/equil/plans100.xml";
        ScenarioLoader loader = new ScenarioLoader("./examples/tutorial/myConfig.xml");
        Scenario scenario = loader.getScenario();
        new MatsimNetworkReader(scenario.getNetwork()).readFile(netFilename);
        new MatsimPopulationReader(scenario).readFile(plansFilename);
        Events events = new Events();
        EventWriterTXT eventWriter = new EventWriterTXT("./output/events.txt");
        events.addHandler(eventWriter);
        QueueSimulation sim = new QueueSimulation(scenario, events);
        sim.openNetStateWriter("./output/simout", netFilename, 10);
        sim.run();
        eventWriter.closeFile();
        String[] visargs = { "./output/simout" };
        NetVis.main(visargs);
    }
}
