package de.oklemenz.meta.ga.helper.reparation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import de.oklemenz.meta.ga.GeneticAlgorithm;
import de.oklemenz.meta.ga.api.AParameter;
import de.oklemenz.meta.ga.api.Gene;
import de.oklemenz.meta.ga.api.GenoType;
import de.oklemenz.meta.ga.api.Individual;
import de.oklemenz.meta.ga.api.Parameter;
import de.oklemenz.meta.ga.api.Random;
import de.oklemenz.meta.ga.helper.DefaultReparation;

public class IntegerListReparation<F extends Comparable<F>> extends DefaultReparation<F, Integer, Integer> {

    private static final long serialVersionUID = 3546927991364990258L;

    private Random<F, Integer, Integer> random;

    protected AParameter<F, Integer, Integer> minValue;

    protected AParameter<F, Integer, Integer> maxValue;

    protected Parameter<F, Integer, Integer> values;

    /**
     * Default constructor
     * 
     * @param geneticAlgorithm The genetic algorithm
     * @since MetaGA 1.0 
     */
    public IntegerListReparation(GeneticAlgorithm<F, Integer, Integer> geneticAlgorithm) {
        super(geneticAlgorithm);
        random = getGeneticAlgorithm().getRandom();
        minValue = getGeneticAlgorithm().getAParameter(GeneticAlgorithm.GA_ALLELE_MIN_VALUE);
        maxValue = getGeneticAlgorithm().getAParameter(GeneticAlgorithm.GA_ALLELE_MAX_VALUE);
        values = getGeneticAlgorithm().getParameter(GeneticAlgorithm.GA_ALLELE_VALUES);
    }

    public void execute(Individual<F, Integer, Integer> individual) {
        GenoType<F, Integer, Integer> genoType = individual.getChromosome().getGenoType();
        int aMin = 0;
        if (minValue != null) {
            aMin = minValue.getAValue();
        }
        int aMax = genoType.getGeneCount();
        if (maxValue != null) {
            aMax = maxValue.getAValue() + 1;
        }
        Integer[] aValues = null;
        if (values != null) {
            aValues = (Integer[]) values.getValue();
            if (minValue == null) {
                aMin = aValues[0];
            }
            if (maxValue == null) {
                aMax = aValues[aValues.length - 1] + 1;
            }
        }
        Set<Integer> freeValues = new HashSet<Integer>();
        for (int i = aMin; i < aMax; i++) {
            if (values == null) {
                freeValues.add(i);
            } else {
                freeValues.add(aValues[i - aMin]);
            }
        }
        for (Gene<F, Integer, Integer> gene : genoType) {
            int value = gene.getAllele().getValue();
            freeValues.remove(value);
        }
        List<Integer> freeValueList = new ArrayList<Integer>(freeValues);
        boolean[] pos = new boolean[aMax];
        for (Gene<F, Integer, Integer> gene : genoType) {
            int value = gene.getAllele().getValue();
            boolean found = false;
            if (aValues != null) {
                for (int j = 0; j < aValues.length; j++) {
                    if (value == aValues[j]) {
                        found = true;
                        break;
                    }
                }
            } else {
                found = true;
            }
            if (value >= aMin && value < aMax && !pos[value] && found) {
                pos[value] = true;
                continue;
            }
            int index = random.nextIntExc(freeValueList.size());
            gene.getAllele().repair(freeValueList.get(index));
            freeValueList.remove(index);
        }
    }

    public String toString() {
        return "integerListReparation:";
    }
}
