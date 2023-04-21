package org.jbfilter.hibernate.fcomps.logic;

import org.jbfilter.core.fcomps.logic.LogicFilterComponent;
import org.jbfilter.hibernate.FilterComponentHibernate;

/**
 * The library's variant of {@link LogicFilterComponent}
 * @author Marcus Adrian
 * @param <E> the beans' type
 * @see LogicFilterComponent
 */
public interface LogicFilterComponentHibernate<E> extends LogicFilterComponent<E>, FilterComponentHibernate<E> {
}
