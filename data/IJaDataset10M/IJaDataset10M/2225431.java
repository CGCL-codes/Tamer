package org.palo.api.impl;

import org.palo.api.Consolidation;
import org.palo.api.Element;

/**
 * <code>ConsolidationInfoImpl</code>
 *
 * @author Stepan Rutz
 * @version $ID$
 */
class ConsolidationImpl implements Consolidation {

    static final ConsolidationImpl create(ConnectionImpl connection, Element parent, Element element, double weight) {
        return new ConsolidationImpl(parent, element, weight);
    }

    private final Element parent;

    private final Element child;

    private final double weight;

    private ConsolidationImpl(Element parent, Element child, double weight) {
        this.parent = parent;
        this.child = child;
        this.weight = weight;
    }

    private final CompoundKey createKey() {
        return new CompoundKey(new Object[] { ConsolidationImpl.class, this.parent, this.child, new Double(weight) });
    }

    public final Element getParent() {
        return parent;
    }

    public final Element getChild() {
        return child;
    }

    public final double getWeight() {
        return weight;
    }

    public final boolean equals(Object obj) {
        if (!(obj instanceof ConsolidationImpl)) return false;
        ConsolidationImpl other = (ConsolidationImpl) obj;
        return createKey().equals(other.createKey());
    }

    public final int hashCode() {
        return createKey().hashCode();
    }
}
