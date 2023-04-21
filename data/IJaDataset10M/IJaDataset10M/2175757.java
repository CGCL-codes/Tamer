package teach.multiagent07.simulation;

import org.matsim.basic.v01.BasicAct;
import org.matsim.basic.v01.BasicLeg;
import org.matsim.basic.v01.BasicPlan;
import org.matsim.basic.v01.BasicPlanImpl;
import org.matsim.basic.v01.BasicRoute;
import org.matsim.interfaces.networks.basicNet.BasicLink;
import org.matsim.utils.vis.netvis.NetVis;
import teach.multiagent07.net.CANetStateWriter;
import teach.multiagent07.net.CANetwork;
import teach.multiagent07.net.CANetworkReader;
import teach.multiagent07.population.Person;
import teach.multiagent07.population.Population;
import teach.multiagent07.population.PopulationReader;
import teach.multiagent07.router.Router;
import teach.multiagent07.util.EventWriterTXT;
import teach.multiagent07.util.PersonsWriterTXT;

public class Controler {

    public static void main(String[] args) {
        String netFileName = "..\\..\\tmp\\studies\\equil\\equil_net.xml";
        String popFileName = "..\\..\\tmp\\studies\\equil\\DSequil_plans.xml";
        CANetwork net = new CANetwork();
        CANetworkReader reader = new CANetworkReader(net, netFileName);
        reader.readNetwork();
        net.connect();
        net.build();
        String visFileName = "../../tmp/testViz";
        Population population = new Population();
        PopulationReader popreader = new PopulationReader(population, net, popFileName);
        popreader.readPopulation();
        population.runHandler(new PersonsWriterTXT());
        Router router = new Router(net);
        router.readNetwork(netFileName);
        String currentVisFile = visFileName;
        for (int i = 0; i < 2; i++) {
            currentVisFile = visFileName + "iteration_" + i;
            CANetStateWriter netVis = CANetStateWriter.createWriter(net, netFileName, currentVisFile);
            EventManager events = new EventManager();
            CAMobSim sim = new CAMobSim(net, netVis, events);
            sim.run(population);
            events.runHandler(new EventWriterTXT());
            events.runHandler(router);
            double replanningRate = 0.2;
            for (Person person : population.getPersons()) {
                if (Math.random() < replanningRate) {
                    BasicPlan plan = person.getSelectedPlan();
                    BasicPlanImpl.ActLegIterator it = plan.getIterator();
                    BasicAct startAct = it.nextAct();
                    BasicLeg leg = it.nextLeg();
                    BasicAct endAct = it.nextAct();
                    BasicLink start = startAct.getLink();
                    BasicLink end = endAct.getLink();
                    BasicRoute route = router.reRoute(start.getToNode().getId(), end.getFromNode().getId(), startAct.getEndTime());
                    leg.setRoute(route);
                }
            }
            router.resetTravelTimes();
        }
        String[] visargs = { currentVisFile };
        NetVis.main(visargs);
    }
}
