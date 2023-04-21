package net.persister.event;

import net.persister.persistentContext.InnerPersistentContext;

/**
 * @author Park, chanwook
 *
 */
public abstract class Event {

    protected InnerPersistentContext innerPersistentContext;

    protected Class entityClass;

    public InnerPersistentContext getInnerPersistentContext() {
        return innerPersistentContext;
    }

    public Class getEntityClass() {
        return entityClass;
    }
}
