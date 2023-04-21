package jenes.tutorials.problem3;

import jenes.population.Fitness;
import jenes.GeneticAlgorithm;
import jenes.utils.Random;
import jenes.algorithms.SimpleGA;
import jenes.chromosome.IntegerChromosome;
import jenes.chromosome.PermutationChromosome;
import jenes.population.Individual;
import jenes.population.Population;
import jenes.population.Population.Statistics.Group;
import jenes.stage.AbstractStage;
import jenes.stage.operator.common.TournamentSelector;
import jenes.tutorials.utils.Utils;

/**
 * Tutorial showing how to implement problem specific operators.
 * The problem faced in this example is the well known Tavel Salesman Problem (TSP)
 *
 * This class specifies the problem.
 *
 * @version 2.0
 * @since 1.0
 */
public class TravelSalesmanProblem {

    public static final int POPULATION_SIZE = 1000;

    private static int GENERATION_LIMIT = 2000;

    public static final int MAX_DISTANCE = 10;

    private TSPGA algorithm;

    private int cities;

    private double[][] map;

    public static void main(String[] args) {
        Utils.printHeader();
        System.out.println();
        System.out.println("TUTORIAL 3:");
        System.out.println("The Travel Salesman Problem, a classics.");
        System.out.println();
        Random.getInstance().setStandardSeed();
        System.out.println("Case 1: 10 cities in circle");
        double[][] m1 = simpleMap(10);
        TravelSalesmanProblem tsp1 = new TravelSalesmanProblem(m1);
        tsp1.solve();
        System.out.println("Case 2: 30 cities in circle");
        double[][] m2 = simpleMap(30);
        TravelSalesmanProblem tsp2 = new TravelSalesmanProblem(m2);
        tsp2.solve();
        System.out.println("Case 3: 30 cities at random");
        double[][] m3 = randomMap(30);
        TravelSalesmanProblem tsp3 = new TravelSalesmanProblem(m3);
        tsp3.solve();
        System.out.println("Case 4: An application of PermutationChromosome");
        tsp2.solvePC();
    }

    public TravelSalesmanProblem(double[][] matrix) {
        cities = matrix[0].length;
        map = matrix;
        IntegerChromosome chrom = new IntegerChromosome(cities, 0, cities - 1);
        for (int i = 0; i < cities; ++i) {
            chrom.setValue(i, i < cities - 1 ? i + 1 : 0);
        }
        Individual<IntegerChromosome> sample = new Individual<IntegerChromosome>(chrom);
        Population<IntegerChromosome> pop = new Population<IntegerChromosome>(sample, POPULATION_SIZE);
        algorithm = new TSPGA(matrix, pop, GENERATION_LIMIT);
        algorithm.setRandomization(true);
        AbstractStage<IntegerChromosome> selection = new TournamentSelector<IntegerChromosome>(1);
        AbstractStage<IntegerChromosome> crossover = new TSPCityCenteredCrossover(0.8);
        AbstractStage<IntegerChromosome> mutation = new TSPScrambleMutator(0.02);
        algorithm.addStage(selection);
        algorithm.addStage(crossover);
        algorithm.addStage(mutation);
        algorithm.setElitism(10);
    }

    public void solve() {
        algorithm.evolve();
        Population.Statistics stats = algorithm.getCurrentPopulation().getStatistics();
        GeneticAlgorithm.Statistics algostats = algorithm.getStatistics();
        Group legals = stats.getGroup(Population.LEGALS);
        System.out.println(legals.get(0));
        System.out.format("found in %d ms and %d generations.\n", algostats.getExecutionTime(), algostats.getGenerations());
        System.out.println();
        Utils.printStatistics(stats);
    }

    public void solvePC() {
        Individual<PermutationChromosome> sample = new Individual<PermutationChromosome>(new PermutationChromosome(cities));
        Population<PermutationChromosome> pop = new Population<PermutationChromosome>(sample, POPULATION_SIZE);
        Fitness<PermutationChromosome> fitness = new Fitness<PermutationChromosome>(false) {

            @Override
            public void evaluate(Individual<PermutationChromosome> individual) {
                PermutationChromosome chrom = individual.getChromosome();
                double count = 0;
                int size = chrom.length();
                for (int i = 0; i < size - 1; i++) {
                    int val1 = chrom.getElementAt(i);
                    int val2 = chrom.getElementAt(i + 1);
                    count += TravelSalesmanProblem.this.map[val1][val2];
                }
                count += TravelSalesmanProblem.this.map[size - 1][0];
                individual.setScore(count);
            }
        };
        SimpleGA<PermutationChromosome> sga = new SimpleGA<PermutationChromosome>(fitness, pop, GENERATION_LIMIT);
        sga.setElitism(10);
        sga.setMutationProbability(0.02);
        sga.evolve();
        Population.Statistics stats = sga.getCurrentPopulation().getStatistics();
        GeneticAlgorithm.Statistics algostats = sga.getStatistics();
        Group legals = stats.getGroup(Population.LEGALS);
        System.out.println(legals.get(0));
        System.out.format("found in %d ms and %d generations.\n", algostats.getExecutionTime(), algostats.getGenerations());
        System.out.println();
    }

    public static double[][] simpleMap(int cities) {
        double[][] matrix = new double[cities][cities];
        matrix[0][0] = 0;
        for (int i = 1; i <= cities / 2; ++i) {
            matrix[0][i] = i;
            matrix[0][cities - i] = i;
        }
        for (int i = 1; i < cities; ++i) {
            for (int j = 0; j < cities; ++j) {
                matrix[i][(i + j) % cities] = matrix[0][j];
            }
        }
        return matrix;
    }

    public static double[][] randomMap(int cities) {
        double[][] matrix = new double[cities][cities];
        for (int i = 0; i < cities; ++i) {
            for (int j = 0; j < cities; ++j) {
                matrix[i][j] = i != j ? Random.getInstance().nextDouble(MAX_DISTANCE) : 0;
            }
        }
        return matrix;
    }
}
