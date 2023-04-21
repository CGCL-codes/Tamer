package cz.cuni.mff.ksi.jinfer.attrstats.experiments.sets;

import cz.cuni.mff.ksi.jinfer.attrstats.experiments.AbstractExperimentSet;
import cz.cuni.mff.ksi.jinfer.attrstats.experiments.ExperimentParameters;
import cz.cuni.mff.ksi.jinfer.attrstats.experiments.interfaces.ImprovementHeuristic;
import cz.cuni.mff.ksi.jinfer.attrstats.experiments.quality.Weight;
import cz.cuni.mff.ksi.jinfer.attrstats.experiments.termination.TimeIterations;
import cz.cuni.mff.ksi.jinfer.attrstats.heuristics.construction.Random;
import cz.cuni.mff.ksi.jinfer.attrstats.heuristics.improvement.LocalBranching;
import cz.cuni.mff.ksi.jinfer.attrstats.utils.Constants;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author vektor
 */
public class RandomLocalBranching extends AbstractExperimentSet {

    @Override
    public String getName() {
        return "Random and Local Branching";
    }

    @Override
    protected List<ExperimentParameters> getExperiments() {
        final List<ImprovementHeuristic> improvement = Arrays.<ImprovementHeuristic>asList(new LocalBranching(0.1, 0));
        final List<ExperimentParameters> ret = new ArrayList<ExperimentParameters>(10);
        for (int i = 0; i < 3; i++) {
            ret.add(new ExperimentParameters(Constants.GRAPH, 10, 1, 1, 0.2429268293, new Random(), improvement, new Weight(), new TimeIterations(1, 10000)));
        }
        return ret;
    }
}
