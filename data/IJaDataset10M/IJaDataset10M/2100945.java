package org.jgrapht.experimental.isomorphism.comparators;

import org.jgrapht.experimental.equivalence.*;

/**
 * Comparator which defines two groups of integers. Odds (mod2=1) and
 * Evens(mod2=0). Works only on Integers.
 * 
 * @author Assaf
 * @since Jul 22, 2005
 */
public class OddEvenGroupComparator implements EquivalenceComparator<Integer, Object> {

    public boolean equivalenceCompare(Integer arg1, Integer arg2, Object context1, Object context2) {
        int int1 = arg1.intValue();
        int int2 = arg2.intValue();
        boolean result = ((int1 % 2) == (int2 % 2));
        return result;
    }

    public int equivalenceHashcode(Integer arg1, Object context) {
        int int1 = arg1.intValue();
        return int1 % 2;
    }
}
