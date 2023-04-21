package de.forsthaus.backend.model;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @author bbruhns
 * 
 */
public class HibernateEntityStatistics implements Comparable<HibernateEntityStatistics> {

    public HibernateEntityStatistics(String entityName, int loadCount, int updateCount, int insertCount, int deleteCount, int fetchCount, int optimisticFailureCount, HibernateStatistics hibernateStatistics) {
        super();
        this.entityName = entityName;
        this.loadCount = loadCount;
        this.updateCount = updateCount;
        this.insertCount = insertCount;
        this.deleteCount = deleteCount;
        this.fetchCount = fetchCount;
        this.optimisticFailureCount = optimisticFailureCount;
        this.hibernateStatistics = hibernateStatistics;
    }

    public HibernateEntityStatistics() {
        super();
    }

    private long id;

    private String entityName;

    private int loadCount;

    private int updateCount;

    private int insertCount;

    private int deleteCount;

    private int fetchCount;

    private int optimisticFailureCount;

    private HibernateStatistics hibernateStatistics;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getLoadCount() {
        return loadCount;
    }

    public void setLoadCount(int loadCount) {
        this.loadCount = loadCount;
    }

    public int getUpdateCount() {
        return updateCount;
    }

    public void setUpdateCount(int updateCount) {
        this.updateCount = updateCount;
    }

    public int getInsertCount() {
        return insertCount;
    }

    public void setInsertCount(int insertCount) {
        this.insertCount = insertCount;
    }

    public int getDeleteCount() {
        return deleteCount;
    }

    public void setDeleteCount(int deleteCount) {
        this.deleteCount = deleteCount;
    }

    public int getFetchCount() {
        return fetchCount;
    }

    public void setFetchCount(int fetchCount) {
        this.fetchCount = fetchCount;
    }

    public int getOptimisticFailureCount() {
        return optimisticFailureCount;
    }

    public void setOptimisticFailureCount(int optimisticFailureCount) {
        this.optimisticFailureCount = optimisticFailureCount;
    }

    public HibernateStatistics getHibernateStatistics() {
        return hibernateStatistics;
    }

    public void setHibernateStatistics(HibernateStatistics hibernateStatistics) {
        this.hibernateStatistics = hibernateStatistics;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    @Override
    public int compareTo(HibernateEntityStatistics o) {
        long anotherId = o.getId();
        return (id < anotherId ? -1 : (id == anotherId ? 0 : 1));
    }

    public String toString() {
        return new ToStringBuilder(this).append("id", getId()).toString();
    }
}
