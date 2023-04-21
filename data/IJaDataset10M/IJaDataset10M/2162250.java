package org.dishevelled.cluster.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.dishevelled.cluster.Cluster;

/**
 * Cluster implementation.
 *
 * @param <E> value type
 * @author  Michael Heuer
 * @version $Revision$ $Date$
 */
final class ClusterImpl<E> implements Cluster<E> {

    /** Members. */
    private final List<E> members;

    /** Exemplar. */
    private final E exemplar;

    /**
     * Create a new cluster implementation with the specified list of members
     * and no exemplar.
     *
     * @param members list of members, must not be null
     */
    ClusterImpl(final List<? extends E> members) {
        this(members, null);
    }

    /**
     * Create a new cluster implementation with the specified list of member
     * values and specified exemplar.
     *
     * @param members list of member values, must not be null and must contain
     *    at least one value
     * @param exemplar exemplar, if not null must be contained in specified list
     *    of member values
     */
    ClusterImpl(final List<? extends E> members, final E exemplar) {
        if (members == null) {
            throw new IllegalArgumentException("members must not be null");
        }
        if (members.size() < 1) {
            throw new IllegalArgumentException("members must contain at least one value");
        }
        if ((exemplar != null) && (!members.contains(exemplar))) {
            throw new IllegalArgumentException("exemplar must be contained in members");
        }
        this.members = Collections.unmodifiableList(new ArrayList<E>(members));
        this.exemplar = exemplar;
    }

    /** {@inheritDoc} */
    public int size() {
        return members.size();
    }

    /** {@inheritDoc} */
    public boolean isSingleton() {
        return (size() == 1);
    }

    /** {@inheritDoc} */
    public E exemplar() {
        return exemplar;
    }

    /** {@inheritDoc} */
    public List<E> members() {
        return members;
    }

    /** {@inheritDoc} */
    public Iterator<E> iterator() {
        return members.iterator();
    }
}
