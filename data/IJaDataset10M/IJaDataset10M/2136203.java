package ec.multiobjective.spea2;

import ec.*;
import ec.util.*;
import ec.multiobjective.*;
import ec.simple.*;

/**
 * This subclass of SimpleEvaluator evaluates the population, then computes auxiliary fitness
 * data of each subpopulation.
 */
public class SPEA2Evaluator extends SimpleEvaluator {

    public void evaluatePopulation(final EvolutionState state) {
        super.evaluatePopulation(state);
        for (int x = 0; x < state.population.subpops.length; x++) {
            Individual[] inds = state.population.subpops[x].individuals;
            computeAuxiliaryData(state, inds);
        }
    }

    /** Computes the strength of individuals, then the raw fitness (wimpiness) and kth-closest sparsity
        measure.  Finally, computes the final fitness of the individuals.  */
    public void computeAuxiliaryData(EvolutionState state, Individual[] inds) {
        double[][] distances = calculateDistances(state, inds);
        for (int y = 0; y < inds.length; y++) {
            int myStrength = 0;
            for (int z = 0; z < inds.length; z++) if (((SPEA2MultiObjectiveFitness) inds[y].fitness).paretoDominates((MultiObjectiveFitness) inds[z].fitness)) myStrength++;
            ((SPEA2MultiObjectiveFitness) inds[y].fitness).strength = myStrength;
        }
        int kTH = (int) Math.sqrt(inds.length);
        for (int y = 0; y < inds.length; y++) {
            double fitness = 0;
            for (int z = 0; z < inds.length; z++) {
                if (((SPEA2MultiObjectiveFitness) inds[z].fitness).paretoDominates((MultiObjectiveFitness) inds[y].fitness)) {
                    fitness += ((SPEA2MultiObjectiveFitness) inds[z].fitness).strength;
                }
            }
            SPEA2MultiObjectiveFitness indYFitness = ((SPEA2MultiObjectiveFitness) inds[y].fitness);
            double kthDistance = Math.sqrt(orderStatistics(distances[y], kTH, state.random[0]));
            indYFitness.kthNNDistance = 1.0 / (2 + kthDistance);
            indYFitness.fitness = fitness + indYFitness.kthNNDistance;
        }
    }

    /** Returns a matrix of sum squared distances from each individual to each other individual. */
    public double[][] calculateDistances(EvolutionState state, Individual[] inds) {
        double[][] distances = new double[inds.length][inds.length];
        for (int y = 0; y < inds.length; y++) {
            distances[y][y] = 0;
            for (int z = y + 1; z < inds.length; z++) {
                distances[z][y] = distances[y][z] = ((SPEA2MultiObjectiveFitness) inds[y].fitness).sumSquaredObjectiveDistance((SPEA2MultiObjectiveFitness) inds[z].fitness);
            }
        }
        return distances;
    }

    /** Returns the kth smallest element in the array.  Note that here k=1 means the smallest element in the array (not k=0).
        Uses a randomized sorting technique, hence the need for the random number generator. */
    double orderStatistics(double[] array, int kth, MersenneTwisterFast rng) {
        return randomizedSelect(array, 0, array.length - 1, kth, rng);
    }

    double randomizedSelect(double[] array, int p, int r, int i, MersenneTwisterFast rng) {
        if (p == r) return array[p];
        int q = randomizedPartition(array, p, r, rng);
        int k = q - p + 1;
        if (i <= k) return randomizedSelect(array, p, q, i, rng); else return randomizedSelect(array, q + 1, r, i - k, rng);
    }

    int randomizedPartition(double[] array, int p, int r, MersenneTwisterFast rng) {
        int i = rng.nextInt(r - p + 1) + p;
        double tmp = array[i];
        array[i] = array[p];
        array[p] = tmp;
        return partition(array, p, r);
    }

    int partition(double[] array, int p, int r) {
        double x = array[p];
        int i = p - 1;
        int j = r + 1;
        while (true) {
            do j--; while (array[j] > x);
            do i++; while (array[i] < x);
            if (i < j) {
                double tmp = array[i];
                array[i] = array[j];
                array[j] = tmp;
            } else return j;
        }
    }
}
