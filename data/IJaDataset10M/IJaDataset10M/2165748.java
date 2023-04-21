package net.sf.kerner.utils.collections.set.impl;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import net.sf.kerner.utils.collections.set.SetFactory;

/**
 * 
 * A {@link net.sf.kerner.utils.collections.set.SetFactory SetFactory} that
 * returns a {@link java.util.LinkedHashSet LinkedHashSet} as
 * {@link java.util.Set Set} implementation.
 * 
 * @author <a href="mailto:alex.kerner.24@googlemail.com">Alexander Kerner</a>
 * @version 2010-12-16
 * 
 * @param <E>
 *            type of elements contained by the {@code Set}
 */
public class LinkedHashSetFactory<E> implements SetFactory<E> {

    /** 
	 * 
	 */
    public Set<E> createCollection() {
        return new LinkedHashSet<E>();
    }

    public Set<E> createCollection(Collection<? extends E> template) {
        return new LinkedHashSet<E>(template);
    }
}
