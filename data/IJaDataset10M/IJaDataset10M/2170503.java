package net.sf.myra.cantminer.pittsburgh;

import static net.sf.myra.datamining.IterationTest.DEFAULT_MAXIMUM_ITERATIONS;
import static net.sf.myra.datamining.IterationTest.MAXIMUM_ITERATIONS;
import java.util.Properties;
import java.util.logging.Logger;
import net.sf.myra.datamining.AbstractObjectiveFunction;
import net.sf.myra.datamining.BacktrackPruner;
import net.sf.myra.datamining.Classifier;
import net.sf.myra.datamining.ConvergenceTest;
import net.sf.myra.datamining.Evaluator;
import net.sf.myra.datamining.Main;
import net.sf.myra.datamining.MajorityClassAssignator;
import net.sf.myra.datamining.Measure;
import net.sf.myra.datamining.Model;
import net.sf.myra.datamining.Rule;
import net.sf.myra.datamining.data.Dataset;
import net.sf.myra.datamining.data.EntropyHeuristicInformation;
import net.sf.myra.datamining.data.Label;
import net.sf.myra.datamining.data.Term;
import net.sf.myra.datamining.function.SensitivitySpecificityFunction;
import net.sf.myra.datamining.measure.ReducedErrorMeasure;
import net.sf.myra.framework.Colony;
import net.sf.myra.framework.Configuration;
import net.sf.myra.framework.Graph;
import net.sf.myra.framework.HeuristicInformation;
import net.sf.myra.framework.LocalSearch;
import net.sf.myra.framework.ObjectFactory;
import net.sf.myra.framework.Vertex;

/**
 * @author Fernando Esteban Barril Otero
 * @version $Revision: 2389 $ $Date:: 2011-09-14 07:12:53#$
 */
public class cAntMinerPB extends Classifier {

    /**
	 * Virtual starting vertex label.
	 */
    public static final String START_VERTEX = "[START]";

    /**
	 * The default configuration file name.
	 */
    public static final String OPTIONS_FILE = "myra-cantminer-pb.messages";

    /**
	 * The property key name under which the percentage of uncovered cases is
	 * stored.
	 */
    public static final String UNCOVERED_CASES_PCT = "net.sf.myra.cantminer.pittsburgh.uncovered";

    /**
	 * The property key name under which the measure class name to be used to
	 * evaluate a list of rules is stored.
	 */
    public static final String LIST_MEASURE = "net.sf.myra.cantminer.pittsburgh.list.measure";

    /**
	 * Default uncovered percentage.
	 */
    private static final double DEFAULT_PERCENTAGE = 1.0;

    /**
	 * The logger instance.
	 */
    private Logger logger = Logger.getLogger(cAntMinerPB.class.getName());

    /**
	 * Default constructor.
	 */
    public cAntMinerPB() {
        super(OPTIONS_FILE);
    }

    @Override
    public Properties filter(Properties properties) {
        properties.remove(Main.EXPORTER);
        properties.remove(ConvergenceTest.CONVERGENCE_TEST_SIZE);
        properties.remove(Classifier.MAXIMUM_UNCOVERED_CASES);
        return properties;
    }

    @Override
    public Model run(Dataset dataset) {
        if (dataset.getMetadata().isHierarchical()) {
            throw new IllegalArgumentException("Hierarchical dataset not supported.");
        }
        final int colony = Configuration.getIntProperty(Colony.COLONY_SIZE);
        final int maxUncoveredCases = (int) ((Configuration.getDoubleProperty(UNCOVERED_CASES_PCT, DEFAULT_PERCENTAGE) / 100) * dataset.getSize() + 0.5);
        Graph<Term> graph = new Graph<Term>();
        graph.add(new Vertex<Term>(START_VERTEX));
        graph.merge(dataset.getMetadata().toGraph());
        Measure measure = (Measure) ObjectFactory.create(Configuration.getProperty(LIST_MEASURE, ReducedErrorMeasure.class.getName()));
        PheromoneUpdater updater = new PheromoneUpdater();
        updater.initialize(graph);
        Label majority = Evaluator.findMajorityClass(dataset);
        CandidateRuleList discovered = null;
        logger.info("Run started.");
        final int maximum = Configuration.getIntProperty(MAXIMUM_ITERATIONS, DEFAULT_MAXIMUM_ITERATIONS);
        int iteration = 0;
        while (iteration < maximum) {
            CandidateRuleList current = null;
            for (int i = 0; i < colony; i++) {
                Dataset clone = dataset.clone();
                RuleFactory factory = new RuleFactory(graph, clone);
                CandidateRuleList candidate = new CandidateRuleList();
                while (clone.getSize() > maxUncoveredCases) {
                    HeuristicInformation heuristic = new EntropyHeuristicInformation(clone);
                    heuristic.initialize(graph);
                    AbstractObjectiveFunction function = (AbstractObjectiveFunction) ObjectFactory.create(Configuration.getProperty(RULE_QUALITY_FUNCTION, SensitivitySpecificityFunction.class.getName()), new Class<?>[] { Dataset.class }, new Object[] { clone });
                    LocalSearch pruner = new BacktrackPruner(function, new MajorityClassAssignator());
                    Rule rule = (Rule) pruner.explore(factory.create(candidate.size()));
                    clone.remove(Evaluator.findCoveredCases(rule, clone));
                    candidate.add(rule.clone());
                }
                if (!candidate.hasDefault()) {
                    if (!clone.isEmpty()) {
                        majority = Evaluator.findMajorityClass(clone);
                    }
                    Rule defaultRule = new Rule(clone.getMetadata());
                    defaultRule.setConsequent(majority);
                    candidate.add(defaultRule);
                }
                candidate.setQuality(measure.evaluate(dataset, candidate).doubleValue());
                if ((current == null) || (candidate.compareTo(current) == 1)) {
                    current = candidate;
                }
            }
            updater.update(current, graph);
            if ((discovered == null) || (current.compareTo(discovered) == 1)) {
                discovered = current;
            }
            if (updater.hasConverged(graph, current)) {
                logger.info("Search has converged at iteration " + iteration);
                break;
            }
            iteration++;
        }
        if (iteration == maximum) {
            logger.info("Maximum number of iterations reached.");
        }
        logger.info("Rule list quality during training: " + discovered.getQuality());
        return discovered;
    }
}
