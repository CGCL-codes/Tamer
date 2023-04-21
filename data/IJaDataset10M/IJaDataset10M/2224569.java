package com.markatta.hund.core;

import com.markatta.hund.model.AbstractItem;
import com.markatta.hund.model.Status;
import com.markatta.hund.util.GenericsUtil;
import java.util.Date;

/**
 * Common interface for executing some kind of check
 * 
 * @author johan
 */
public abstract class WorkItem<T extends AbstractItem> {

    private long entityId;

    private Class<T> entityClass = GenericsUtil.getTypeClass(getClass(), 0);

    private String label;

    /**
     * @return the id of the entity the work item is for
     */
    public long getEntityId() {
        return entityId;
    }

    public Class<T> getEntityClass() {
        return entityClass;
    }

    public void init(T entity) {
        this.entityId = entity.getId();
        setupLabel(entity);
    }

    protected abstract void setupLabel(T entity);

    public void check(T entity) {
        long before = new Date().getTime();
        Status result = executePlugin(entity);
        long after = new Date().getTime();
        result.setTiming(after - before);
        entity.setStatus(result);
    }

    public abstract Status executePlugin(T entity);

    public String getLabel() {
        return label;
    }

    protected void setLabel(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return getClass().getName() + "[entityId:" + entityId + "]";
    }
}
