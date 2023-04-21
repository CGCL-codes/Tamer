package playground.meisterk.kti.controler.listeners;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import org.apache.log4j.Logger;
import org.matsim.api.core.v01.population.Population;
import org.matsim.core.controler.Controler;
import org.matsim.core.controler.events.IterationEndsEvent;
import org.matsim.core.controler.listener.IterationEndsListener;
import playground.meisterk.org.matsim.population.algorithms.AbstractClassifiedFrequencyAnalysis;
import playground.meisterk.org.matsim.population.algorithms.PopulationLegDistanceDistribution;
import playground.meisterk.org.matsim.population.algorithms.AbstractClassifiedFrequencyAnalysis.CrosstabFormat;

public class LegDistanceDistributionWriter implements IterationEndsListener {

    public static final double[] distanceClasses = new double[] { 0.0, 100, 200, 500, 1000, 2000, 5000, 10000, 20000, 50000, 100000, 200000, 500000, 1000000 };

    private final String filename;

    private static final Logger log = Logger.getLogger(LegDistanceDistributionWriter.class);

    public LegDistanceDistributionWriter(String filename) {
        super();
        this.filename = filename;
    }

    public void notifyIterationEnds(IterationEndsEvent event) {
        if (event.getIteration() % 10 == 0) {
            Controler c = event.getControler();
            Population pop = c.getPopulation();
            PrintStream out = null;
            try {
                out = new PrintStream(event.getControler().getControlerIO().getIterationFilename(event.getIteration(), filename));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            AbstractClassifiedFrequencyAnalysis algo = new PopulationLegDistanceDistribution(out);
            algo.run(pop);
            log.info("Writing results file...");
            algo.printClasses(CrosstabFormat.ABSOLUTE, false, distanceClasses, out);
            algo.printDeciles(true, out);
            out.close();
            log.info("Writing results file...done.");
        }
    }
}
