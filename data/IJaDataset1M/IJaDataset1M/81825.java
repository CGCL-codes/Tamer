package org.sti2.elly.basics;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.sti2.elly.api.basics.IAtomicRole;
import org.sti2.elly.api.basics.IPredicate;
import org.sti2.elly.api.util.IDescriptionVisitor;
import org.sti2.elly.api.util.IDescriptionVisitorEx;
import org.sti2.elly.api.util.IVisitor;

/**
 * This interface defines an atomic Role.
 * <p>
 * A atomic Role is a role that is not constructed with a Role r
 * &isin; N<sub>R</sub><sup>S</sup> &cup; N<sub>R</sub><sup>N</sup>. 
 * 
 * @author Daniel Winkler
 */
public class AtomicRole implements IAtomicRole {

    private final IPredicate predicate;

    /**
	 * The Constructor.
	 * 
	 * @param predicate The predicate that is used for representing this role; must be of arity 2 and not null.
	 * @throws IllegalArgumentException if the predicate is not of arity 2 or null.
	 */
    AtomicRole(IPredicate predicate) {
        if (predicate == null) throw new IllegalArgumentException("Predicate must not be null");
        if (predicate.getArity() != 2) throw new IllegalArgumentException("Predicate's arity must be 2");
        this.predicate = predicate;
    }

    @Override
    public IPredicate getPredicate() {
        return this.predicate;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        AtomicRole otherRole = (AtomicRole) obj;
        return new EqualsBuilder().append(this.predicate, otherRole.predicate).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(557, 31).append(this.predicate).toHashCode();
    }

    @Override
    public String toString() {
        return getPredicate().toString();
    }

    @Override
    public void accept(IVisitor visitor) {
        visitor.visitAtomicRole(this);
    }

    @Override
    public void accept(IDescriptionVisitor visitor) {
        visitor.visitAtomicRole(this);
    }

    @Override
    public <O> O accept(IDescriptionVisitorEx<O> visitor) {
        return visitor.visitAtomicRole(this);
    }
}
