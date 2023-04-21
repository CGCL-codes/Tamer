package org.datanucleus.api.jpa.criteria;

import java.util.Collection;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.PluralJoin;
import javax.persistence.metamodel.PluralAttribute;
import org.datanucleus.api.jpa.metamodel.PluralAttributeImpl;

/**
 * Implementation of JPA2 Criteria "PluralJoin".
 * 
 * @param <Z> The source type
 * @param <C> The collection type
 * @param <E> The element type of the collection
 */
public class PluralJoinImpl<Z, C, E> extends JoinImpl<Z, E> implements PluralJoin<Z, C, E> {

    /**
     * @param parent Parent component
     * @param attr The attribute being joined to
     * @param joinType Type of join
     */
    public PluralJoinImpl(FromImpl parent, PluralAttributeImpl<? super Z, Collection<E>, E> attr, JoinType joinType) {
        super(parent, attr, joinType);
    }

    public PluralAttribute<? super Z, C, E> getModel() {
        return (PluralAttribute<? super Z, C, E>) attribute.getType();
    }
}
