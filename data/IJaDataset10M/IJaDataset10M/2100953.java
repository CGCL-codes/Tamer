package org.datanucleus.jpa.metamodel;

import javax.persistence.metamodel.Type;

/**
 * Implementation of JPA2 Metamodel "Type".
 */
public class TypeImpl<X> implements Type<X> {

    public final Class<X> cls;

    protected TypeImpl(Class<X> cls) {
        this.cls = cls;
    }

    public Class<X> getJavaType() {
        return cls;
    }

    public javax.persistence.metamodel.Type.PersistenceType getPersistenceType() {
        return PersistenceType.BASIC;
    }

    public String toString() {
        return cls.getName();
    }
}
