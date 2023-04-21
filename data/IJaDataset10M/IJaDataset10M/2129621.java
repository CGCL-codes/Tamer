package com.rapidminer.operator.features;

/**
 * Ensures that every individual appears only once.
 * 
 * @author Simon Fischer, Ingo Mierswa
 *          Exp $
 */
public class RedundanceRemoval implements PopulationOperator {

    /** The default implementation returns true for every generation. */
    public boolean performOperation(int generation) {
        return true;
    }

    public void operate(Population pop) {
        for (int i = 0; i < pop.getNumberOfIndividuals() - 1; i++) {
            for (int j = pop.getNumberOfIndividuals() - 1; j > i; j--) {
                if (pop.get(i).equals(pop.get(j))) pop.remove(j);
            }
        }
    }
}
