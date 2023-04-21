package de.fzi.herakles.strategy.component;

import java.util.Set;
import de.fzi.herakles.commons.reasoner.ReasonerAdapter;

/**
 * Elementary strategy component to perform a selection of reasoners from a pool of reasoners.
 * @author bock
 *
 */
public interface Selector {

    /**
	 * Set the pool of reasoners to select from
	 * @param reasoners set of {@link ReasonerAdapter}s
	 */
    public void setReasonerPool(Set<ReasonerAdapter> reasoners);

    /**
	 * Does the actual selection.
	 * @return set of selected {@link ReasonerAdapter}s
	 */
    public Set<ReasonerAdapter> select();
}
