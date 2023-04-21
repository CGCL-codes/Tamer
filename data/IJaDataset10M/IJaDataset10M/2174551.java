package jenes.tutorials.ga2;

import jenes.GenerationEventListener;
import jenes.GeneticAlgorithm;
import jenes.Random;
import jenes.chromosome.IntegerChromosome;
import jenes.population.Individual;
import jenes.population.Population;
import jenes.population.Population.Statistics;
import jenes.stage.AbstractStage;
import jenes.stage.Parallel;
import jenes.stage.operator.common.OnePointCrossover;
import jenes.stage.operator.common.SimpleMutator;
import jenes.stage.operator.common.TournamentSelector;
import jenes.stage.operator.common.TwoPointsCrossover;
import jenes.tutorials.utils.Utils;

public class PatternProblem implements GenerationEventListener {

    private static int POPULATION_SIZE = 100;

    private static int CHROMOSOME_LENGTH = 10;

    private static int GENERATION_LIMIT = 1000;

    private static int MAX_INT = 49;

    private PatternGA algorithm = null;

    public PatternProblem() {
        IntegerChromosome chrom = new IntegerChromosome(CHROMOSOME_LENGTH, 0, MAX_INT);
        Individual<IntegerChromosome> ind = new Individual<IntegerChromosome>(chrom);
        Population<IntegerChromosome> pop = new Population<IntegerChromosome>(ind, POPULATION_SIZE);
        algorithm = new PatternGA(pop, GENERATION_LIMIT);
        algorithm.setElitism(5);
        AbstractStage<IntegerChromosome> selection = new TournamentSelector<IntegerChromosome>(2);
        Parallel<IntegerChromosome> parallel = new Parallel<IntegerChromosome>(new SimpleDispenser<IntegerChromosome>(2));
        AbstractStage<IntegerChromosome> crossover1p = new OnePointCrossover<IntegerChromosome>(0.8);
        parallel.add(crossover1p);
        AbstractStage<IntegerChromosome> crossover2p = new TwoPointsCrossover<IntegerChromosome>(0.5);
        parallel.add(crossover2p);
        AbstractStage<IntegerChromosome> mutation = new SimpleMutator<IntegerChromosome>(0.2);
        algorithm.addStage(selection);
        algorithm.addStage(parallel);
        algorithm.addStage(mutation);
        algorithm.setBiggerIsBetter(false);
        algorithm.addGenerationEventListener(this);
    }

    public void run(int[] target, int precision) {
        algorithm.setTarget(target);
        algorithm.setPrecision(precision);
        algorithm.evolve();
        Population.Statistics stats = algorithm.getCurrentPopulation().getStatistics();
        GeneticAlgorithm.Statistics algostats = algorithm.getStatistics();
        System.out.println();
        System.out.print("Target:[");
        for (int i = 0; i < target.length; ++i) {
            System.out.print(target[i] + (i < target.length - 1 ? " " : ""));
        }
        System.out.println("]");
        System.out.println();
        System.out.println("Solution: ");
        System.out.println(stats.getLegalLowestIndividual().getChromosome());
        System.out.println(stats.getLegalLowestIndividual());
        System.out.format("found in %d ms and %d generations.\n", algostats.getExecutionTime(), algostats.getGenerations());
        System.out.println();
    }

    public void onGeneration(GeneticAlgorithm ga, long time) {
        Statistics stat = ga.getCurrentPopulation().getStatistics();
        System.out.println("Current generation: " + ga.getGeneration());
        System.out.println("\tBest score: " + stat.getLegalLowestScore());
        System.out.println("\tAvg score: " + stat.getLegalScoreAvg());
    }

    private static void randomize(int[] sample) {
        for (int i = 0; i < sample.length; i++) sample[i] = Random.getInstance().nextInt(0, MAX_INT + 1);
    }

    public static void main(String[] args) {
        Utils.printHeader();
        System.out.println();
        System.out.println("TUTORIAL 2:");
        System.out.println("This algorithm aims to autonomously find a vector of integers that best matches with a target vector.");
        System.out.println();
        Random.getInstance().setStandardSeed();
        PatternProblem problem = new PatternProblem();
        int[] target = new int[CHROMOSOME_LENGTH];
        randomize(target);
        problem.run(target, 2);
        randomize(target);
        problem.run(target, 0);
    }
}
