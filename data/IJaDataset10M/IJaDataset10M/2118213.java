package org.plazmaforge.framework.service.erm;

import java.io.Serializable;

public class ERMGenericEntityService<E, PK extends Serializable> extends AbstractERMEntityService<E, Serializable> implements ERMEntityService<E, Serializable> {

    private Class<E> entityClass;

    public Class<E> getEntityClass() {
        return entityClass;
    }

    public void setEntityClass(Class<E> entityClass) {
        this.entityClass = entityClass;
    }
}
