package org.dyndns.fichtner.theoryofsets.ant.tasks;

import java.util.Collection;
import java.util.Set;
import org.dyndns.fichtner.theoryofsets.DefaultTosSet;

/**
 * Intersection of the sets A and B, denoted A \cap B, is the set whose members
 * are members of both A and B. The intersection of {1, 2, 3} and {2, 3, 4} is
 * the set {2, 3}.
 * 
 * @author Peter Fichtner
 */
public class IntersectionSetTask extends AbstractTheoryOfSetsTask {

    @Override
    protected Collection<String> calculate(Set<String> setA, Set<String> setB) {
        return new DefaultTosSet<String>(setA).intersection(new DefaultTosSet<String>(setB)).getData();
    }
}
