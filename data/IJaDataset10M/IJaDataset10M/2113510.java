package com.avaje.ebean.collection;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Holds sets of additions and deletions from a 'owner' List Set or Map.
 * <p>
 * These sets of additions and deletions are used to support persisting
 * ManyToMany relationships. The additions becoming inserts into the
 * intersection table and the removals becoming deletes from the intersection
 * table.
 * </p>
 */
class ModifyHolder<E> implements Serializable {

    private static final long serialVersionUID = 2572572897923801083L;

    /**
	 * Deletions list for manyToMany persistence.
	 */
    Set<E> modifyDeletions = new HashSet<E>();

    /**
	 * Additions list for manyToMany persistence.
	 */
    Set<E> modifyAdditions = new HashSet<E>();

    /**
	 * Used by BeanList.addAll() methods.
	 */
    void modifyAdditionAll(Collection<? extends E> c) {
        if (c != null) {
            modifyAdditions.addAll(c);
        }
    }

    void modifyAddition(E bean) {
        if (bean != null) {
            modifyAdditions.add(bean);
        }
    }

    @SuppressWarnings("unchecked")
    void modifyRemoval(Object bean) {
        if (bean != null) {
            modifyDeletions.add((E) bean);
        }
    }

    Set<E> getModifyAdditions() {
        return modifyAdditions;
    }

    Set<E> getModifyRemovals() {
        return modifyDeletions;
    }
}
