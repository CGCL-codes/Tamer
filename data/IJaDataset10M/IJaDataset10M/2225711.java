package moea.commons.assigner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import ext.number.ValReal;
import moea.commons.Individual;
import moea.commons.Population;
import moea.commons.comparator.ComparatorFitness;
import moea.commons.comparator.ComparatorPareto;
import moea.commons.comparator.ComparatorSigmas;

public class AssignerSpea2<I extends Individual> {

    protected int K;

    public AssignerSpea2(int K) {
        this.K = K;
    }

    public void assignFitness(Population<I> pop) {
        int i, j, popSize = pop.size();
        int strength[] = new int[popSize];
        int raw[] = new int[popSize];
        double density[] = new double[popSize];
        double sigma;
        I indI;
        ComparatorPareto comparator = ComparatorPareto.getInstance();
        int compare;
        double fitness;
        for (i = 0; i < popSize; ++i) {
            strength[i] = 0;
            raw[i] = 0;
            density[i] = 0;
        }
        for (i = 0; i < popSize; ++i) {
            indI = pop.get(i);
            for (j = 0; j < popSize; ++j) {
                if (i == j) continue;
                compare = comparator.compare(indI, pop.get(j));
                if (compare == -1) strength[i]++;
            }
        }
        for (i = 0; i < popSize; ++i) {
            indI = pop.get(i);
            for (j = 0; j < popSize; ++j) {
                if (i == j) continue;
                compare = comparator.compare(indI, pop.get(j));
                if (compare == 1) raw[i] += strength[j];
            }
        }
        for (i = 0; i < popSize; ++i) {
            sigma = calculateSigma(i, pop);
            density[i] = 1 / (sigma + 2);
            fitness = raw[i] + density[i];
            pop.get(i).setProperty("fitness", new ValReal(fitness));
        }
    }

    public Population<I> reduceByFitness(Population<I> pop) {
        Population<I> result = new Population<I>();
        I indI;
        for (int i = 0; i < pop.size(); ++i) {
            indI = pop.get(i);
            if (indI.getProperty("fitness").getValue().doubleValue() < 1) result.add(indI);
        }
        return result;
    }

    public void expand(Population<I> pop, Population<I> all, int nElems) {
        int i = 0, count = 0, allSize = all.size();
        I indI;
        Collections.sort(all, ComparatorFitness.getInstance());
        for (i = 0; i < allSize; ++i) {
            indI = all.get(i);
            if (indI.getProperty("fitness").getValue().doubleValue() >= 1) {
                pop.add(indI);
                count++;
                if (count == nElems) break;
            }
        }
    }

    public Population<I> reduce(Population<I> pop, int maxSize) {
        int i, min;
        ArrayList<ArrayList<Double>> allSigmas = new ArrayList<ArrayList<Double>>();
        ComparatorSigmas comparator = ComparatorSigmas.getInstance();
        HashSet<Integer> erased = new HashSet<Integer>();
        int toErase = pop.size() - maxSize;
        for (i = 0; i < pop.size(); i++) allSigmas.add(calculateSigmas(i, pop));
        while (erased.size() < toErase) {
            min = 0;
            while (erased.contains(min)) min++;
            for (i = 0; i < pop.size(); i++) {
                if (i == min || erased.contains(i)) continue;
                if (comparator.compare(allSigmas.get(i), allSigmas.get(min)) == -1) min = i;
            }
            erased.add(min);
        }
        Population<I> result = new Population<I>();
        for (i = 0; i < pop.size(); i++) {
            if (!erased.contains(i)) result.add(pop.get(i));
        }
        return result;
    }

    private double calculateSigma(int i, Population<I> pop) {
        return calculateSigmas(i, pop).get(K);
    }

    private ArrayList<Double> calculateSigmas(int i, Population<I> pop) {
        int popSize = pop.size();
        int j;
        double distance;
        ArrayList<Double> distancesToI = new ArrayList<Double>();
        I indI = pop.get(i);
        for (j = 0; j < popSize; j++) {
            distance = indI.getObjectiveVector().euclideanDistance(pop.get(j).getObjectiveVector());
            distancesToI.add(distance);
        }
        Collections.sort(distancesToI);
        return distancesToI;
    }
}
