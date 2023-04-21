package org.dllearner.refinementoperators;

import java.util.List;
import java.util.Set;
import org.dllearner.core.owl.Description;

/**
 * Adapter for {@link RefinementOperator} interface.
 * 
 * @author Jens Lehmann
 *
 */
public abstract class RefinementOperatorAdapter implements RefinementOperator {

    @Override
    public abstract Set<Description> refine(Description description);

    @Override
    public Set<Description> refine(Description description, int maxLength) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<Description> refine(Description description, int maxLength, List<Description> knownRefinements) {
        throw new UnsupportedOperationException();
    }
}
