package playground.johannes.interaction;

import java.io.FileNotFoundException;
import java.io.IOException;
import org.matsim.core.api.population.Person;
import org.matsim.core.controler.Controler;
import org.matsim.core.controler.events.IterationEndsEvent;
import org.matsim.core.controler.events.StartupEvent;
import org.matsim.core.controler.listener.IterationEndsListener;
import org.matsim.core.controler.listener.StartupListener;
import playground.johannes.graph.GraphStatistics;
import playground.johannes.socialnet.SocialNetwork;
import playground.johannes.socialnet.io.PajekVisWriter;
import playground.johannes.socialnet.io.SNGraphMLWriter;
import playground.johannes.statistics.WeightedStatistics;

/**
 * @author illenberger
 *
 */
public class SNRun {

    private static SocialNetwork<Person> socialnet;

    /**
	 * @param args
	 * @throws IOException 
	 */
    public static void main(String[] args) throws IOException {
        Controler c = new Controler(args);
        c.setOverwriteFiles(true);
        c.setCreateGraphs(false);
        c.setWriteEventsInterval(0);
        c.addControlerListener(new SNSetup());
        c.run();
        System.out.println("Mean degree is " + GraphStatistics.getDegreeStatistics(socialnet).getMean());
        System.out.println("Mean clustering is " + GraphStatistics.getClusteringStatistics(socialnet).getMean());
        WeightedStatistics.writeHistogram(GraphStatistics.getDegreeDistribution(socialnet).absoluteDistribution(), c.getOutputFilename("degree.hist.txt"));
        SNGraphMLWriter writer = new SNGraphMLWriter();
        writer.write(socialnet, c.getOutputFilename("socialnet.graphml"));
        PajekVisWriter pWriter = new PajekVisWriter();
        pWriter.write(socialnet, c.getOutputFilename("socialnet.net"));
    }

    private static class SNSetup implements StartupListener, IterationEndsListener {

        private InteractionHandler handler;

        public void notifyStartup(StartupEvent event) {
            socialnet = new SocialNetwork<Person>(event.getControler().getPopulation());
            InteractionSelector selector = new RandomSelector(1, event.getControler().getConfig().global().getRandomSeed());
            BefriendInteractor interactor = new BefriendInteractor(socialnet, 1, event.getControler().getConfig().global().getRandomSeed());
            handler = new InteractionHandler(selector, interactor, event.getControler().getFacilities());
            event.getControler().addControlerListener(interactor);
            event.getControler().getEvents().addHandler(handler);
        }

        public void notifyIterationEnds(IterationEndsEvent event) {
            try {
                handler.dumpVisitorStatisitcs(event.getControler().getIterationFilename("facilityStats.txt"));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
