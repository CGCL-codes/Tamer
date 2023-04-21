package org.dyndns.fichtner.theoryofsets.tasks;

import java.util.Set;
import org.dyndns.fichtner.theoryofsets.TheoryOfSets;

/**
 * Intersection of the sets A and B, denoted A \cap B, is the set whose members
 * are members of both A and B. The intersection of {1, 2, 3} and {2, 3, 4} is
 * the set {2, 3}.
 * 
 * @author Peter Fichtner
 */
public class IntersectionSetTask extends AbstractTheoryOfSetsTask {

    @Override
    protected Set<String> calculate(Set<String> setA, Set<String> setB) {
        return TheoryOfSets.intersection(setA, setB);
    }
}
