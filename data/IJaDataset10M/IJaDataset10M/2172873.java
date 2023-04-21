package com.rapidminer.operator.features;

/**
 * Keeps the <i>n</i> best individuals and kicks out the rest.
 * 
 * @author Simon Fischer, Ingo Mierswa
 */
public class KeepBest implements PopulationOperator {

    private int bestN;

    public KeepBest(int bestN) {
        this.bestN = bestN;
    }

    /** The default implementation returns true for every generation. */
    public boolean performOperation(int generation) {
        return true;
    }

    public void operate(Population pop) {
        if (pop.getNumberOfIndividuals() > bestN) {
            pop.sort();
            while (pop.getNumberOfIndividuals() > bestN) pop.remove(0);
        }
    }
}
