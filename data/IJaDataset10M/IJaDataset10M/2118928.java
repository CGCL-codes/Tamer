package samples;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import samples.objectivefunctions.OneMax;
import engine.Algorithm;
import engine.CachedObjectiveFunction;
import engine.Population;
import engine.PopulationEvaluator;
import engine.SingleThreadedEvaluator;
import engine.exitcriteria.MaxIterations;
import engine.individuals.BinaryVector;
import engine.operators.BestFractionSelection;
import engine.operators.binary.UniformCrossover;
import engine.operators.binary.UniformProbabilityNegationMutation;
import engine.utils.JavaRandom;
import engine.utils.ListUtils;

/**
 * Simple Genetic Algorithm implementation using wevo. Optimizes OneMax.
 * 
 * @author Marcin Brodziak (marcin@nierobcietegowdomu.pl)
 */
public class SGA {

    /** Logger. */
    private final Logger logger = Logger.getLogger(SGA.class.getCanonicalName());

    /** Fraction of individuals being saved in next population. */
    @Option(name = "-f", aliases = { "fraction" }, usage = "Fraction of " + "individuals being saved in next population.")
    private double fraction = 0.5;

    /** For each individual and gene probability of the mutation. */
    @Option(name = "-mp", aliases = { "mutationProbability" }, usage = "Probability of mutating an individual and it's single gene.")
    private double mutationProbability = 0.03;

    /** Number of iterations. */
    @Option(name = "-mi", aliases = { "maxIterations" }, usage = "Maximum number " + "of iterations for the algorithm to run.")
    private int maxIterations = 100;

    /** Size of vector to be optimized. */
    @Option(name = "-il", aliases = { "individualLength" }, usage = "Length of " + "each individual in the population.")
    private int individualLength = 20;

    /** Size of the population. */
    @Option(name = "-ps", aliases = { "populationSize" }, usage = "Size of the " + "population under evaluation.")
    private int populationSize = 1000;

    /** Size of the cache. */
    @Option(name = "-cs", aliases = { "cacheSize" }, usage = "Size of the cache " + "for evaluation results.")
    private int cacheSize = 2 * populationSize;

    /**
   * Main program routine.
   * @param args Command line arguments.
   */
    public void doMain(final String[] args) {
        CmdLineParser parser = new CmdLineParser(this);
        try {
            parser.parseArgument(args);
        } catch (CmdLineException e) {
            parser.printUsage(System.err);
            logger.log(Level.SEVERE, "Failed to parse command line arguments", e);
            System.exit(1);
        }
        Population<BinaryVector> population = BinaryVector.generatePopulationOfRandomBinaryIndividuals(new JavaRandom(), individualLength, populationSize);
        Algorithm<BinaryVector> alg = new Algorithm<BinaryVector>(population);
        CachedObjectiveFunction<BinaryVector> objectiveFunctionWrapper = new CachedObjectiveFunction<BinaryVector>(new OneMax(), cacheSize);
        alg.addExitPoint(new MaxIterations<BinaryVector>(maxIterations));
        alg.addEvaluationPoint(buildObjectiveFunctions(objectiveFunctionWrapper));
        alg.addOperator(new BestFractionSelection<BinaryVector>(new OneMax(), fraction));
        alg.addOperator(new UniformCrossover(new JavaRandom()));
        alg.addOperator(new UniformProbabilityNegationMutation(mutationProbability, new JavaRandom()));
        alg.run();
        System.out.println(alg.getPopulation().toString());
    }

    /**
   * Entry point to the program.
   * @param args Command line arguments.
   */
    public static void main(final String[] args) {
        new SGA().doMain(args);
    }

    /**
   * Creates a list of objective functions (one in this case) to be optimized.
   * @param objectiveFunctionWrapper A function that the list is based on.
   * @return List of objective functions.
   */
    @SuppressWarnings("unchecked")
    private static PopulationEvaluator<BinaryVector> buildObjectiveFunctions(CachedObjectiveFunction<BinaryVector> objectiveFunctionWrapper) {
        return new SingleThreadedEvaluator<BinaryVector>(ListUtils.buildList(objectiveFunctionWrapper));
    }
}
