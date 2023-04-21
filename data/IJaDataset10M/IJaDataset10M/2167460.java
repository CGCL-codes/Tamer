package de.oklemenz.meta.ga.helper.problem;

import de.oklemenz.meta.ga.GeneticAlgorithm;
import de.oklemenz.meta.ga.api.Allele;
import de.oklemenz.meta.ga.api.DoubleParameter;
import de.oklemenz.meta.ga.api.Fitness;
import de.oklemenz.meta.ga.api.Gene;
import de.oklemenz.meta.ga.api.GenoType;
import de.oklemenz.meta.ga.api.PhenoType;
import de.oklemenz.meta.ga.helper.DefaultProblem;

public class KnapSackProblem extends DefaultProblem<Double, Integer, Boolean> {

    private static final long serialVersionUID = 3833189146452636720L;

    public static final String GA_KNAPSACK_UTILIZATION = "ga.knapsack.utilization";

    public static final String GA_KNAPSACK_VALUE = "ga.knapsack.value";

    public static final String GA_KNAPSACK_CAPACITY = "ga.knapsack.capacity";

    private double[] util;

    private double[] value;

    private double capacity;

    private DoubleParameter<Double, Integer, Boolean> penaltyFactor;

    /**
     * Default constructor
     * 
     * @param geneticAlgorithm The genetic algorithm
     * @since MetaGA 1.0 
     */
    public KnapSackProblem(GeneticAlgorithm<Double, Integer, Boolean> geneticAlgorithm) {
        super((GeneticAlgorithm<Double, Integer, Boolean>) geneticAlgorithm);
        util = (double[]) getGeneticAlgorithm().getParameter(GA_KNAPSACK_UTILIZATION).getValue();
        value = (double[]) getGeneticAlgorithm().getParameter(GA_KNAPSACK_VALUE).getValue();
        capacity = getGeneticAlgorithm().getDoubleParameter(GA_KNAPSACK_CAPACITY).getDoubleValue();
        penaltyFactor = getGeneticAlgorithm().getDoubleParameter(GeneticAlgorithm.GA_PENALTY_FACTOR);
    }

    public Fitness<Double, Integer, Boolean> compute(PhenoType<Double, Integer, Boolean> phenoType) {
        GenoType<Double, Integer, Boolean> genoType = phenoType.getGenoType();
        int c = 0;
        double d = 0;
        double usedUtil = 0;
        for (Gene<Double, Integer, Boolean> gene : genoType) {
            Allele<Double, Integer, Boolean> allel = gene.getAllele();
            if (allel.getValue()) {
                usedUtil += util[c];
                d += value[c];
            }
            c++;
        }
        if (usedUtil > capacity) {
            d -= penaltyFactor.getDoubleValue() * (usedUtil - capacity);
        }
        return getGeneticAlgorithm().createFitness(phenoType.getIndividual(), d);
    }

    public boolean isValid(PhenoType<Double, Integer, Boolean> phenoType) {
        GenoType<Double, Integer, Boolean> genoType = phenoType.getGenoType();
        int c = 0;
        double usedUtil = 0;
        for (Gene<Double, Integer, Boolean> gene : genoType) {
            Allele<Double, Integer, Boolean> allel = gene.getAllele();
            if (allel.getValue()) {
                usedUtil += util[c];
            }
            c++;
        }
        return usedUtil <= capacity;
    }

    public String toString() {
        return "knapSackProblem:";
    }
}
