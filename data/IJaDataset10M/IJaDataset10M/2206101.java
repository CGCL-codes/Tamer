package org.datanucleus.management.runtime;

/**
 * Query runtime statistics
 */
public interface QueryRuntimeMBean {

    /**
     * The total number of queries that are executing
     * @return the total
     */
    long getQueryActiveTotalCount();

    /**
     * The total number of queries that failed executing
     * @return the total
     */
    long getQueryErrorTotalCount();

    /**
     * The total number of queries executed
     * @return the total
     */
    long getQueryExecutionTotalCount();

    /**
     * Lowest execution time
     * @return Lowest execution time in milleseconds
     */
    long getQueryExecutionTimeLow();

    /**
     * Highest execution time
     * @return Highest execution time in milleseconds
     */
    long getQueryExecutionTimeHigh();

    /**
     * execution total time
     * @return execution total time in milleseconds
     */
    long getQueryExecutionTotalTime();

    /**
     * Simple Moving Average execution time of transactions
     * @return Average execution time of transactions in milleseconds
     */
    long getQueryExecutionTimeAverage();
}
