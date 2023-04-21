package qs;

import java.math.BigInteger;
import mathutils.*;

/**
 *  A class to represent factors from the factor base with the log of their product
 * This  has got to be sorted by the log field. 
 * (ie the sum(log(FB[factorIndicies])
 *  @author Matthew Painter
 */
public class SortingTuple implements Comparable {

    private float log;

    private int[] factorIndicies;

    /**
     *  Construct a new SortingTuple object
	 *  @param factorIndicies - indicies of factors in the factor base
     *  @param log - the log of the product of the factors represented by the indicies
     */
    public SortingTuple(int[] factorIndicies, BigInteger[] factorBase) {
        this.factorIndicies = factorIndicies;
        float sum = 0;
        for (int x = 0; x < factorIndicies.length; x++) {
            sum += BigIntegerFunctions.log(factorBase[factorIndicies[x]]);
        }
        this.log = sum;
    }

    /**
     *  Return the log value
     */
    public float getLog() {
        return log;
    }

    /**
     *  Return the factor indicies
     */
    public int[] getFactorIndicies() {
        return factorIndicies;
    }

    /**
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
    public int compareTo(Object o) {
        SortingTuple t = (SortingTuple) o;
        if (this.log - t.log < 0) return -1;
        if (this.log - t.log > 0) return 1;
        return 0;
    }
}
