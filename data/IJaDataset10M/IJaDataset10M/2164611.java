package preprocessing.automatic.FitnessCalculation.FitnessPenalty;

import configuration.Slider;
import org.ytoh.configurations.annotations.Range;
import preprocessing.automatic.Population.APAIndividual;
import preprocessing.automatic.Population.Subsequence;
import java.util.Iterator;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: cepekm1
 * Date: Nov 1, 2010
 * Time: 4:21:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class ManyPreprocessingMethodsPenalizer extends FitnessPenalizer {

    @Slider(min = 2, max = 20, multiplicity = 1, name = "Maximal number of methods without penalization", value = 3)
    private int maxAllowedMethods = 3;

    @Range(from = 0.0, to = 1.0)
    private double perMethodPenalty = 0.05;

    @Override
    protected void runPenalization(int individualIndex, APAIndividual individual) {
        Iterator<Subsequence> iter = individual.getIterator();
        while (iter.hasNext()) {
            Subsequence sub = iter.next();
            int penalizedGenes = sub.getNumGenes() - maxAllowedMethods;
            if (penalizedGenes > 0) {
                individual.penalizeFitness(penalizedGenes * perMethodPenalty);
            }
        }
    }

    @Override
    protected void initiateRun(List<APAIndividual> population) {
    }

    @Override
    protected void setName() {
        name = "Too many preprocessing methods penalizer";
    }
}
