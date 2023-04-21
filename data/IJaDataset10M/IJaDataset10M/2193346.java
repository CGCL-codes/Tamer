package com.rapidminer.operator.features.selection;

import java.util.LinkedList;
import java.util.List;
import com.rapidminer.operator.features.Individual;
import com.rapidminer.operator.features.Population;
import com.rapidminer.operator.features.PopulationOperator;

/**
 * Adds iteratively the next feature according to given attribute name array.
 * 
 * @author Ingo Mierswa
 *          ingomierswa Exp $
 */
public class IterativeFeatureAdding implements PopulationOperator {

    private int[] indices;

    private int counter;

    public IterativeFeatureAdding(int[] attributeIndices, int counter) {
        this.indices = attributeIndices;
        this.counter = counter;
    }

    /** The default implementation returns true for every generation. */
    public boolean performOperation(int generation) {
        return true;
    }

    public void operate(Population pop) {
        List<Individual> result = new LinkedList<Individual>();
        for (int i = 0; i < pop.getNumberOfIndividuals(); i++) {
            if (counter < indices.length) {
                double[] weights = pop.get(i).getWeightsClone();
                weights[indices[counter]] = 1.0d;
                result.add(new Individual(weights));
            }
        }
        pop.clear();
        pop.addAllIndividuals(result);
        counter++;
    }
}
