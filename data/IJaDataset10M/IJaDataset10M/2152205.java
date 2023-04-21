package org.datanucleus.jpa.criteria;

import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.SetJoin;
import javax.persistence.metamodel.SetAttribute;
import org.datanucleus.jpa.metamodel.SetAttributeImpl;

/**
 * Implementation of JPA2 Criteria "SetJoin".
 * 
 * @param <Z> The source type of the join
 * @param <E> The element type of the target Set
 */
public class SetJoinImpl<Z, E> extends PluralJoinImpl<Z, java.util.Set<E>, E> implements SetJoin<Z, E> {

    /**
     * Constructor for a set join.
     * @param parent The parent
     * @param attr The attribute being joined
     * @param joinType Type of join
     */
    public SetJoinImpl(FromImpl parent, SetAttributeImpl attr, JoinType joinType) {
        super(parent, attr, joinType);
    }

    public SetAttribute<? super Z, E> getModel() {
        return (SetAttribute<? super Z, E>) attribute;
    }
}
