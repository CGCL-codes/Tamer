package net.sf.jsfcomp.hibernatetrace.statistics;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;

/**
 * @author Mert Caliskan
 * 
 * Wrapper class for statistics of a given sessionFactory 
 */
public class HTStatistics implements Serializable {

    private long startTime;

    private long sessionOpenCount;

    private long sessionCloseCount;

    private long connectCount;

    private long flushCount;

    private long transactionCount;

    private long commitedTransactionCount;

    private long prepareStatementCount;

    private long closeStatementCount;

    private long totalEntityFetchCount;

    private long totalEntityLoadCount;

    private long totalEntityInsertCount;

    private long totalEntityUpdateCount;

    private long totalEntityDeleteCount;

    private long totalEntityOptimisticFailureCount;

    private long totalQueryExecutionCount;

    private long totalQueryExecutionMaxTime;

    private long totalQueryCacheHitCount;

    private long totalQueryCacheMissCount;

    private long totalQueryCachePutCount;

    private long totalCollectionLoadCount;

    private long totalCollectionFetchCount;

    private long totalCollectionUpdateCount;

    private long totalCollectionRemoveCount;

    private long totalCollectionRecreateCount;

    private long totalSecondLevelCacheHitCount;

    private long totalSecondLevelCacheMissCount;

    private long totalSecondLevelCachePutCount;

    private List queryStatistics = new LinkedList();

    private List entityStatistics = new LinkedList();

    private List secondLevelCacheStatistics = new LinkedList();

    private List collectionStatistics = new LinkedList();

    public HTStatistics(SessionFactory sessionFactory) {
        Statistics statistics = sessionFactory.getStatistics();
        setStartTime(statistics.getStartTime());
        setSessionOpenCount(statistics.getSessionOpenCount());
        setSessionCloseCount(statistics.getSessionCloseCount());
        setConnectCount(statistics.getConnectCount());
        setFlushCount(statistics.getFlushCount());
        setTransactionCount(statistics.getTransactionCount());
        setCommitedTransactionCount(statistics.getSuccessfulTransactionCount());
        setPrepareStatementCount(statistics.getPrepareStatementCount());
        setCloseStatementCount(statistics.getCloseStatementCount());
        setTotalEntityFetchCount(statistics.getEntityFetchCount());
        setTotalEntityLoadCount(statistics.getEntityLoadCount());
        setTotalEntityInsertCount(statistics.getEntityInsertCount());
        setTotalEntityUpdateCount(statistics.getEntityUpdateCount());
        setTotalEntityDeleteCount(statistics.getEntityDeleteCount());
        setTotalEntityOptimisticFailureCount(statistics.getOptimisticFailureCount());
        setTotalQueryExecutionCount(statistics.getQueryExecutionCount());
        setTotalQueryExecutionMaxTime(statistics.getQueryExecutionMaxTime());
        setTotalQueryCacheHitCount(statistics.getQueryCacheHitCount());
        setTotalQueryCacheMissCount(statistics.getQueryCacheMissCount());
        setTotalQueryCachePutCount(statistics.getQueryCachePutCount());
        setTotalCollectionLoadCount(statistics.getCollectionLoadCount());
        setTotalCollectionFetchCount(statistics.getCollectionFetchCount());
        setTotalCollectionUpdateCount(statistics.getCollectionUpdateCount());
        setTotalCollectionRemoveCount(statistics.getCollectionRemoveCount());
        setTotalCollectionRecreateCount(statistics.getCollectionRecreateCount());
        setTotalSecondLevelCacheHitCount(statistics.getSecondLevelCacheHitCount());
        setTotalSecondLevelCacheMissCount(statistics.getSecondLevelCacheMissCount());
        setTotalSecondLevelCachePutCount(statistics.getSecondLevelCachePutCount());
        String[] queries = statistics.getQueries();
        for (int i = 0; i < queries.length; i++) {
            String query = queries[i];
            HTQueryStatistics queryStatistics = new HTQueryStatistics(query, statistics.getQueryStatistics(query));
            getQueryStatistics().add(queryStatistics);
        }
        String[] entityNames = statistics.getEntityNames();
        for (int i = 0; i < entityNames.length; i++) {
            String entityName = entityNames[i];
            HTEntityStatistics entityStatistics = new HTEntityStatistics(entityName, statistics.getEntityStatistics(entityName));
            getEntityStatistics().add(entityStatistics);
        }
        String[] secondLevelCacheRegionNames = statistics.getSecondLevelCacheRegionNames();
        for (int i = 0; i < secondLevelCacheRegionNames.length; i++) {
            String regionName = secondLevelCacheRegionNames[i];
            HTSecondLevelCacheStatistics secondLevelCacheStatistics = new HTSecondLevelCacheStatistics(regionName, statistics.getSecondLevelCacheStatistics(regionName));
            getSecondLevelCacheStatistics().add(secondLevelCacheStatistics);
        }
        String[] collectionRoleNames = statistics.getCollectionRoleNames();
        for (int i = 0; i < collectionRoleNames.length; i++) {
            String roleName = collectionRoleNames[i];
            HTCollectionStatistics collectionStatistics = new HTCollectionStatistics(roleName, statistics.getCollectionStatistics(roleName));
            getCollectionStatistics().add(collectionStatistics);
        }
    }

    public long getConnectCount() {
        return connectCount;
    }

    public void setConnectCount(long connectCount) {
        this.connectCount = connectCount;
    }

    public long getFlushCount() {
        return flushCount;
    }

    public void setFlushCount(long flushCount) {
        this.flushCount = flushCount;
    }

    public long getSessionCloseCount() {
        return sessionCloseCount;
    }

    public void setSessionCloseCount(long sessionCloseCount) {
        this.sessionCloseCount = sessionCloseCount;
    }

    public long getSessionOpenCount() {
        return sessionOpenCount;
    }

    public void setSessionOpenCount(long sessionOpenCount) {
        this.sessionOpenCount = sessionOpenCount;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getCloseStatementCount() {
        return closeStatementCount;
    }

    public void setCloseStatementCount(long closeStatementCount) {
        this.closeStatementCount = closeStatementCount;
    }

    public long getCommitedTransactionCount() {
        return commitedTransactionCount;
    }

    public void setCommitedTransactionCount(long commitedTransactionCount) {
        this.commitedTransactionCount = commitedTransactionCount;
    }

    public long getPrepareStatementCount() {
        return prepareStatementCount;
    }

    public void setPrepareStatementCount(long prepareStatementCount) {
        this.prepareStatementCount = prepareStatementCount;
    }

    public long getTransactionCount() {
        return transactionCount;
    }

    public void setTransactionCount(long transactionCount) {
        this.transactionCount = transactionCount;
    }

    public List getQueryStatistics() {
        return queryStatistics;
    }

    public void setQueryStatistics(List queryStatistics) {
        this.queryStatistics = queryStatistics;
    }

    public List getEntityStatistics() {
        return entityStatistics;
    }

    public void setEntityStatistics(List entityStatistics) {
        this.entityStatistics = entityStatistics;
    }

    public List getSecondLevelCacheStatistics() {
        return secondLevelCacheStatistics;
    }

    public void setSecondLevelCacheStatistics(List secondLevelCacheStatistics) {
        this.secondLevelCacheStatistics = secondLevelCacheStatistics;
    }

    public List getCollectionStatistics() {
        return collectionStatistics;
    }

    public void setCollectionStatistics(List collectionStatistics) {
        this.collectionStatistics = collectionStatistics;
    }

    public long getTotalCollectionFetchCount() {
        return totalCollectionFetchCount;
    }

    public void setTotalCollectionFetchCount(long totalCollectionFetchCount) {
        this.totalCollectionFetchCount = totalCollectionFetchCount;
    }

    public long getTotalCollectionLoadCount() {
        return totalCollectionLoadCount;
    }

    public void setTotalCollectionLoadCount(long totalCollectionLoadCount) {
        this.totalCollectionLoadCount = totalCollectionLoadCount;
    }

    public long getTotalCollectionRecreateCount() {
        return totalCollectionRecreateCount;
    }

    public void setTotalCollectionRecreateCount(long totalCollectionRecreateCount) {
        this.totalCollectionRecreateCount = totalCollectionRecreateCount;
    }

    public long getTotalCollectionRemoveCount() {
        return totalCollectionRemoveCount;
    }

    public void setTotalCollectionRemoveCount(long totalCollectionRemoveCount) {
        this.totalCollectionRemoveCount = totalCollectionRemoveCount;
    }

    public long getTotalCollectionUpdateCount() {
        return totalCollectionUpdateCount;
    }

    public void setTotalCollectionUpdateCount(long totalCollectionUpdateCount) {
        this.totalCollectionUpdateCount = totalCollectionUpdateCount;
    }

    public long getTotalEntityDeleteCount() {
        return totalEntityDeleteCount;
    }

    public void setTotalEntityDeleteCount(long totalEntityDeleteCount) {
        this.totalEntityDeleteCount = totalEntityDeleteCount;
    }

    public long getTotalEntityFetchCount() {
        return totalEntityFetchCount;
    }

    public void setTotalEntityFetchCount(long totalEntityFetchCount) {
        this.totalEntityFetchCount = totalEntityFetchCount;
    }

    public long getTotalEntityInsertCount() {
        return totalEntityInsertCount;
    }

    public void setTotalEntityInsertCount(long totalEntityInsertCount) {
        this.totalEntityInsertCount = totalEntityInsertCount;
    }

    public long getTotalEntityLoadCount() {
        return totalEntityLoadCount;
    }

    public void setTotalEntityLoadCount(long totalEntityLoadCount) {
        this.totalEntityLoadCount = totalEntityLoadCount;
    }

    public long getTotalEntityUpdateCount() {
        return totalEntityUpdateCount;
    }

    public void setTotalEntityUpdateCount(long totalEntityUpdateCount) {
        this.totalEntityUpdateCount = totalEntityUpdateCount;
    }

    public long getTotalEntityOptimisticFailureCount() {
        return totalEntityOptimisticFailureCount;
    }

    public void setTotalEntityOptimisticFailureCount(long totalEntityOptimisticFailureCount) {
        this.totalEntityOptimisticFailureCount = totalEntityOptimisticFailureCount;
    }

    public long getTotalSecondLevelCacheHitCount() {
        return totalSecondLevelCacheHitCount;
    }

    public void setTotalSecondLevelCacheHitCount(long totalSecondLevelCacheHitCount) {
        this.totalSecondLevelCacheHitCount = totalSecondLevelCacheHitCount;
    }

    public long getTotalSecondLevelCacheMissCount() {
        return totalSecondLevelCacheMissCount;
    }

    public void setTotalSecondLevelCacheMissCount(long totalSecondLevelCacheMissCount) {
        this.totalSecondLevelCacheMissCount = totalSecondLevelCacheMissCount;
    }

    public long getTotalSecondLevelCachePutCount() {
        return totalSecondLevelCachePutCount;
    }

    public void setTotalSecondLevelCachePutCount(long totalSecondLevelCachePutCount) {
        this.totalSecondLevelCachePutCount = totalSecondLevelCachePutCount;
    }

    public long getTotalQueryCacheHitCount() {
        return totalQueryCacheHitCount;
    }

    public void setTotalQueryCacheHitCount(long totalQueryCacheHitCount) {
        this.totalQueryCacheHitCount = totalQueryCacheHitCount;
    }

    public long getTotalQueryCacheMissCount() {
        return totalQueryCacheMissCount;
    }

    public void setTotalQueryCacheMissCount(long totalQueryCacheMissCount) {
        this.totalQueryCacheMissCount = totalQueryCacheMissCount;
    }

    public long getTotalQueryCachePutCount() {
        return totalQueryCachePutCount;
    }

    public void setTotalQueryCachePutCount(long totalQueryCachePutCount) {
        this.totalQueryCachePutCount = totalQueryCachePutCount;
    }

    public long getTotalQueryExecutionCount() {
        return totalQueryExecutionCount;
    }

    public void setTotalQueryExecutionCount(long totalQueryExecutionCount) {
        this.totalQueryExecutionCount = totalQueryExecutionCount;
    }

    public long getTotalQueryExecutionMaxTime() {
        return totalQueryExecutionMaxTime;
    }

    public void setTotalQueryExecutionMaxTime(long totalQueryExecutionMaxTime) {
        this.totalQueryExecutionMaxTime = totalQueryExecutionMaxTime;
    }
}
