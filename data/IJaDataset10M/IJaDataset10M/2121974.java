package org.loadunit.results;

/**
 * Class that holds results data for a LoadunitTest.
 *  
 * @author bche
 *
 */
public class LoadunitResultsData {

    private String m_sName;

    private PerformanceResults m_perfResults;

    private ThroughputResults m_throughputResults;

    /**
     * Constructor
     * @param sName the name of the test for which this LoadunitResultsData
     * will store results
     */
    public LoadunitResultsData(String sName) {
        m_sName = sName;
        m_perfResults = new PerformanceResults();
        m_throughputResults = new ThroughputResults();
    }

    /**
     * Gets the name of the test for which this LoadunitResults is
     * storing results
     * @return the name of the test for which this LoadunitResults is
     * storing results
     */
    public String getName() {
        return m_sName;
    }

    /**
     * Gets the performance results 
     * @return the performance results
     */
    public PerformanceResults getPerformanceResults() {
        return m_perfResults;
    }

    /**
     * gets the throughput results
     * @return the throughput results
     */
    public ThroughputResults getThroughputResults() {
        return m_throughputResults;
    }

    /**
     * Adds a throughput result point
     * @param iUsers the number of users for this result
     * @param dThroughput the throughput for this result
     */
    public void addThroughputResultPoint(int iUsers, double dThroughput) {
        m_throughputResults.addResultPoint(new ResultPoint(iUsers, dThroughput));
    }

    /**
     * Adds performance data
     * @param data the performance data to add
     */
    public void addPerfData(PerformanceData data) {
        m_perfResults.addPerfData(data);
    }

    /**
     * Signals the contained PerformanceResults to make a result point
     * from its performance data     
     */
    public void makePerfResultPoint() {
        m_perfResults.makeResultPoint();
    }
}
