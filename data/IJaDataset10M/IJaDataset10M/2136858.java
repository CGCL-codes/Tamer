package uk.org.ponder.genetic;

import java.util.Comparator;

/**
 * The class 
 * 
 * @author Bosmon
 */
public class IndividualComparator implements Comparator {

    public int compare(Object indo1, Object indo2) {
        Individual ind1 = (Individual) indo1;
        Individual ind2 = (Individual) indo2;
        if (ind1.selfitness > ind2.selfitness) return -1; else if (ind1.selfitness == ind2.selfitness) return 0; else return 1;
    }
}
