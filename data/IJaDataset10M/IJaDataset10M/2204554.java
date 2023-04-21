package org.jbfilter.core.fcomps.logic;

import org.jbfilter.core.FilterComponent;
import org.jbfilter.core.FilterComponentContainer;

/**
 * @author Marcus Adrian
 *
 * @param <E> the beans' type
 */
public interface LogicFilterComponent<E> extends FilterComponent<E>, FilterComponentContainer<E> {

    /**
	 * The logic to apply.
	 * @return
	 */
    Logic getLogic();
}
