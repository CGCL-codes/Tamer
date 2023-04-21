package edu.udo.cs.ai.nemoz.model.links;

import edu.udo.cs.ai.nemoz.model.entities.Entity;
import edu.udo.cs.ai.nemoz.model.user.User;

/**
 * TODO Enter a type comment here!
 *
 * @author oflasch
 */
public abstract class Link<S extends Entity<S>, T extends Entity<T>> implements Comparable<Link<S, T>> {

    protected final S source;

    protected final T target;

    protected final User owner;

    public Link(final S source, final T target, final User owner) {
        if (source == null) throw new IllegalArgumentException("link source must not be null");
        if (target == null) throw new IllegalArgumentException("link target must not be null");
        if (owner == null) throw new IllegalArgumentException("owner must not be null");
        this.source = source;
        this.target = target;
        this.owner = owner;
    }

    public S getSource() {
        return source;
    }

    public T getTarget() {
        return target;
    }

    public User getOwner() {
        return owner;
    }

    @Override
    public boolean equals(final Object rhs) {
        if (rhs == this) return true;
        if (!(rhs instanceof Link)) return false;
        final Link rhsLink = (Link) rhs;
        return source.equals(rhsLink.getSource()) && target.equals(rhsLink.getTarget()) && owner.equals(rhsLink.getOwner());
    }

    @Override
    public int hashCode() {
        return source.hashCode() * target.hashCode();
    }

    public int compareTo(final Link<S, T> rhs) {
        final int sourceCompareResult = source.compareTo(rhs.getSource());
        if (sourceCompareResult == 0) {
            final int targetCompareResult = target.compareTo(rhs.getTarget());
            if (targetCompareResult == 0) return owner.compareTo(rhs.getOwner()); else return targetCompareResult;
        } else {
            return sourceCompareResult;
        }
    }

    @Override
    public String toString() {
        return "L:" + source + "->" + target + " (" + owner + ")";
    }
}
