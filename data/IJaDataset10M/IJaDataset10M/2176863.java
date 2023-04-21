package de.oklemenz.meta.ga.helper.selection;

import java.util.List;
import de.oklemenz.meta.ga.GeneticAlgorithm;
import de.oklemenz.meta.ga.api.Couple;
import de.oklemenz.meta.ga.api.Crowd;
import de.oklemenz.meta.ga.api.Individual;
import de.oklemenz.meta.ga.api.Pair;
import de.oklemenz.meta.ga.helper.DefaultSelection;

public class RankSelection<F extends Comparable<F>, L extends Comparable<L>, A extends Comparable<A>> extends DefaultSelection<F, L, A> {

    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = -7576698672038493717L;

    private Crowd<F, L, A> cachedCrowd;

    private List<Pair<Double, Individual<F, L, A>>> cachedDistribution;

    /**
     * Default constructor
     * 
     * @param geneticAlgorithm The genetic algorithm
     * @since MetaGA 1.0 
     */
    public RankSelection(GeneticAlgorithm<F, L, A> geneticAlgorithm) {
        super(geneticAlgorithm);
    }

    private List<Pair<Double, Individual<F, L, A>>> getDistribution(Crowd<F, L, A> crowd) {
        if (crowd.equals(cachedCrowd) && cachedDistribution != null) {
            return cachedDistribution;
        } else {
            return cachedDistribution = getGeneticAlgorithm().getDistribution().getRankAccumulated(crowd, true);
        }
    }

    public Crowd<F, L, A> executeForN(Crowd<F, L, A> crowd, int n) {
        List<Pair<Double, Individual<F, L, A>>> distribution = getDistribution(crowd);
        Crowd<F, L, A> newCrowd = getGeneticAlgorithm().createCrowd();
        while (newCrowd.size() != n) {
            Double randomValue = random.nextDouble();
            Individual<F, L, A> individual = null;
            for (Pair<Double, Individual<F, L, A>> pair : distribution) {
                Double value = pair.getLeft();
                if (value.compareTo(randomValue) < 0) {
                    individual = pair.getRight();
                } else {
                    newCrowd.add(individual);
                    if (selectOnce.getBooleanValue()) {
                        crowd.remove(individual);
                    }
                    break;
                }
            }
        }
        return newCrowd;
    }

    public Couple<F, L, A> executeForTwo(Crowd<F, L, A> crowd) {
        List<Pair<Double, Individual<F, L, A>>> distribution = getDistribution(crowd);
        Individual<F, L, A> individual1 = null;
        Individual<F, L, A> individual2 = null;
        while (individual1 == null || individual2 == null || individual1 == individual2) {
            Double randomValue = random.nextDouble();
            Individual<F, L, A> individual = null;
            for (Pair<Double, Individual<F, L, A>> pair : distribution) {
                Double value = pair.getLeft();
                if (value.compareTo(randomValue) < 0) {
                    individual = pair.getRight();
                } else {
                    if (individual1 == null) {
                        individual1 = individual;
                    } else {
                        individual2 = individual;
                    }
                    if (selectOnce.getBooleanValue()) {
                        crowd.remove(individual);
                    }
                    break;
                }
            }
        }
        return getGeneticAlgorithm().createCouple(individual1, individual2);
    }

    public Individual<F, L, A> executeForOne(Crowd<F, L, A> crowd) {
        List<Pair<Double, Individual<F, L, A>>> distribution = getDistribution(crowd);
        Double randomValue = random.nextDouble();
        Individual<F, L, A> individual = null;
        for (Pair<Double, Individual<F, L, A>> pair : distribution) {
            Double value = pair.getLeft();
            if (value.compareTo(randomValue) < 0) {
                individual = pair.getRight();
            } else {
                break;
            }
        }
        if (selectOnce.getBooleanValue()) {
            crowd.remove(individual);
        }
        return individual;
    }

    public String toString() {
        return "rankSelection:";
    }
}
