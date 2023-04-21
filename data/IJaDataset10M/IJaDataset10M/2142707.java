package org.loadunit;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import junit.extensions.TestDecorator;
import junit.framework.TestResult;
import org.apache.log4j.Logger;
import org.loadunit.results.LoadunitResultsData;
import org.loadunit.results.PerformanceData;
import org.loadunit.results.ResultPoint;
import org.loadunit.testcase.LoadunitTestCase;
import EDU.oswego.cs.dl.util.concurrent.CountDown;
import EDU.oswego.cs.dl.util.concurrent.Latch;
import EDU.oswego.cs.dl.util.concurrent.PooledExecutor;

/**
 * A test that performs a ramping load test 
 * 
 * @author bche
 *
 */
public class LoadunitTest extends TestDecorator {

    private int m_iIterations = 10;

    private int m_iMaxUsers = 50;

    private int m_iUsersPerRampup = 10;

    private int m_iDelay = 5000;

    private File m_outputFolder = null;

    private boolean m_bStopForBottleneck = false;

    private PooledExecutor m_pool = null;

    private LinkedList m_prevThroughputs = new LinkedList();

    private int m_iStrikes = 0;

    private LoadunitResultsData m_results;

    private static final Logger s_log = Logger.getLogger(LoadunitTest.class);

    /**
     * Constructor
     * @param test the test case to load test
     * @param iMaxUsers the max number of concurrent users to which to ramp and test 
     * @param iIterations number of iterations to test at each ramping stemp
     * @param iUsersPerRampup the number of users to add after reach ramping step
     * @param iDelay number of miliseconds to wait between requests
     */
    public LoadunitTest(LoadunitTestCase test, int iMaxUsers, int iIterations, int iUsersPerRampup, int iDelay) {
        this(test, iMaxUsers, iIterations, iUsersPerRampup, iDelay, false);
    }

    /**
     * Constructor
     * @param test the test case to load test
     * @param iMaxUsers the max number of concurrent users to which to ramp and test 
     * @param iIterations number of iterations to test at each ramping stemp
     * @param iUsersPerRampup the number of users to add after reach ramping step
     * @param iDelay number of miliseconds to wait between requests
     * @param bStopForBottleneck whether to stop the test as soon as a bottleneck
     * is detected
     */
    public LoadunitTest(LoadunitTestCase test, int iMaxUsers, int iIterations, int iUsersPerRampup, int iDelay, boolean bStopForBottleneck) {
        super(test);
        m_iMaxUsers = iMaxUsers;
        m_iIterations = iIterations;
        m_iUsersPerRampup = iUsersPerRampup;
        m_iDelay = iDelay;
        m_bStopForBottleneck = bStopForBottleneck;
        String sName = Util.getNameDate("LoadunitTest_" + test.getName());
        m_results = new LoadunitResultsData(sName);
    }

    /**
     * Returns a LoadunitTest using Loadunit's default test parameters
     * as specified in its configuration file
     * @param test the test case to make into a LoadunitTest
     * @return a LoadunitTest using Loadunit's default test parameters
     * as specified in its configuration file
     */
    public static LoadunitTest getDefaultLoadunitTest(LoadunitTestCase test) {
        LoadunitTest LoadunitTest = new LoadunitTest(test, LoadunitConfig.getMaxUsers(), LoadunitConfig.getIterations(), LoadunitConfig.getUsersPerRampup(), LoadunitConfig.getDelay(), true);
        return LoadunitTest;
    }

    public void setOutputFolder(File folder, boolean bCreateSingleResultsFolder) {
        m_outputFolder = folder;
    }

    public void setOutputFolder(File folder) {
        setOutputFolder(folder, false);
    }

    public File getOutputFolder() {
        return m_outputFolder;
    }

    /**
     * Performs a Loadunit test and returns the throughput for the test 
     * @param test the test to run 
     * @param result test test's result
     * @param iUsers number of uesrs to test
     * @param iIterations number of iterations to run the test
     * @param iDelay delay between iterations 
     * @return the throughput of the test
     */
    private double doTest(LoadunitTestCase test, TestResult result, int iUsers, int iIterations, int iDelay) {
        Latch begin = new Latch();
        CountDown done = new CountDown(iUsers);
        CountDown preDone = new CountDown(iUsers);
        Latch postBegin = new Latch();
        CountDown postDone = new CountDown(iUsers);
        for (int i = 0; i < iUsers; i++) {
            User u = new User(preDone, begin, done, postBegin, postDone, test, result, iIterations, iDelay, iUsers);
            try {
                m_pool.execute(u);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            preDone.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long lStart = System.currentTimeMillis();
        begin.release();
        try {
            done.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long lEnd = System.currentTimeMillis();
        postBegin.release();
        try {
            postDone.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        m_results.makePerfResultPoint();
        double dSeconds = (lEnd - lStart) / 1000;
        double dRequests = iUsers * iIterations;
        double dThroughput = dRequests / dSeconds;
        if (s_log.isInfoEnabled()) {
            s_log.info("time " + dSeconds + " seconds");
            s_log.info("----------");
            s_log.info(iUsers + " Users Throughput: " + dThroughput + " requests/second");
            s_log.info("----------");
        }
        return dThroughput;
    }

    public void run(TestResult result) {
        m_pool = new PooledExecutor(m_iMaxUsers);
        m_pool.setKeepAliveTime(-1);
        m_pool.createThreads(m_iMaxUsers);
        s_log.info("running " + fTest.toString() + " up to " + m_iMaxUsers + " users.  Delay: " + m_iDelay + "ms. Iterations: " + m_iIterations + ". users per rampup: " + m_iUsersPerRampup);
        LoadunitTestCase test = (LoadunitTestCase) fTest;
        double dThroughput;
        for (int i = m_iUsersPerRampup; i <= m_iMaxUsers; i += m_iUsersPerRampup) {
            s_log.info("testing " + i + " users...");
            dThroughput = doTest(test, result, i, m_iIterations, m_iDelay);
            m_results.addThroughputResultPoint(i, dThroughput);
            if (m_bStopForBottleneck && detectBottleneck(dThroughput)) {
                s_log.info("Stopping loadunit test due to bottleneck");
                break;
            }
        }
        ResultPoint maxThroughput = m_results.getThroughputResults().getMaxResult();
        double dMaxThroughput = maxThroughput.getY();
        assertTrue("Minimum Required Throughput not reached.  Highest throughput reached: " + dMaxThroughput, dMaxThroughput >= test.getMinThroughput());
        ResultPoint maxPerf = m_results.getPerformanceResults().getMaxResult();
        double dMaxPerf = maxPerf.getY();
        assertTrue("Maximum Average Allowed-Performance Time exceeded.  Max Time: " + dMaxPerf, dMaxPerf < test.getMaxTime());
        if (s_log.isInfoEnabled()) {
            s_log.info("Max Throughput: " + dMaxThroughput + " at " + maxThroughput.getX() + " users");
            s_log.info("Max average time: " + dMaxPerf + "ms at " + maxPerf.getX() + " users");
        }
        LoadunitSuite.addResultsData(m_results);
        m_pool.shutdownNow();
        m_pool = null;
    }

    private static double standardDev(Double[] samples) {
        double dMean = mean(samples);
        double dSum = 0;
        for (int i = 0; i < samples.length; i++) {
            dSum += Math.pow(samples[i].doubleValue() - dMean, 2);
        }
        return Math.sqrt(dSum / samples.length);
    }

    private static double mean(Double[] samples) {
        double dSum = 0;
        for (int i = 0; i < samples.length; i++) {
            dSum += samples[i].doubleValue();
        }
        return dSum / samples.length;
    }

    private boolean detectBottleneck(double dThroughput) {
        int iSize = 3;
        if (m_prevThroughputs.size() < iSize) {
            m_prevThroughputs.add(new Double(dThroughput));
            return false;
        }
        Double[] samples = (Double[]) m_prevThroughputs.toArray(new Double[iSize]);
        double dMean = mean(samples);
        double dStdDev = standardDev(samples);
        m_prevThroughputs.add(new Double(dThroughput));
        m_prevThroughputs.remove(0);
        if (s_log.isDebugEnabled()) {
            s_log.debug("mean: " + dMean + ".  standard deviation: " + dStdDev);
        }
        if (Math.round(dThroughput) <= Math.round(dMean + dStdDev)) {
            m_iStrikes++;
            if (m_iStrikes < 3) {
                if (s_log.isDebugEnabled()) {
                    s_log.debug("bottleneck strike " + m_iStrikes);
                }
                return false;
            } else {
                if (s_log.isDebugEnabled()) {
                    s_log.debug("found bottleneck");
                }
                return true;
            }
        } else {
            if (s_log.isDebugEnabled()) {
                s_log.debug("no bottleneck");
            }
            return false;
        }
    }

    /**
     * @return
     */
    public int getIterations() {
        return m_iIterations;
    }

    /**
     * @return
     */
    public int getMaxUsers() {
        return m_iMaxUsers;
    }

    /**
     * @return
     */
    public int getUsersPerRampup() {
        return m_iUsersPerRampup;
    }

    /**
     * @return
     */
    public int getDelay() {
        return m_iDelay;
    }

    public boolean getStopForBottleneck() {
        return m_bStopForBottleneck;
    }

    public void setStopForBottleneck(boolean bStop) {
        m_bStopForBottleneck = bStop;
    }

    public LoadunitResultsData getResultsData() {
        return m_results;
    }

    /**
     * A virtual user for running a test          
     */
    private class User implements Runnable {

        private final Latch m_startSignal;

        private final CountDown m_done;

        private final CountDown m_preDone;

        private final Latch m_postBegin;

        private final CountDown m_postDone;

        private final LoadunitTestCase m_test;

        private final TestResult m_result;

        private final int m_iIters;

        private final int m_iDelay;

        private final int m_iTotalUsers;

        private final ArrayList m_dataList;

        /**
         * constructor for a virtual user
         * @param preDone barrier for waiting for preRuns to finish
         * @param runBegin latch for coordinating start with other users
         * @param runDone CountDown barrier for coordinating ending with other users
         * @param postBegin latch for coordinating postTest start
         * @param postDone barrier for waiting for postRuns to finish
         * @param test the test for the user to run
         * @param result the result for the test
         * @param iIters number of times to run the test
         * @param iDelay delay in ms between each test run
         * @param iTotalUsers the total number of concurrent users with which this
         * user is being run
         */
        User(CountDown preDone, Latch runBegin, CountDown runDone, Latch postBegin, CountDown postDone, LoadunitTestCase test, TestResult result, int iIters, int iDelay, int iTotalUsers) {
            m_startSignal = runBegin;
            m_done = runDone;
            m_preDone = preDone;
            m_postBegin = postBegin;
            m_postDone = postDone;
            m_test = test;
            m_result = result;
            m_iIters = iIters;
            m_iDelay = iDelay;
            m_iTotalUsers = iTotalUsers;
            m_dataList = new ArrayList();
            Thread.yield();
        }

        public void preRun() {
            m_test.preTest();
        }

        /**
         * This method behaves differently on each run
         * On the first run, it runs the preRun.  On the second run,
         * the main run.  On the third run, the post run.  So,
         * this method needs to be called 3 times
         */
        public void run() {
            preRun();
            m_preDone.release();
            try {
                m_startSignal.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            doRun();
            m_done.release();
            try {
                m_postBegin.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            postRun();
            m_postDone.release();
        }

        public void postRun() {
            for (int i = 0; i < m_dataList.size(); i++) {
                m_results.addPerfData((PerformanceData) m_dataList.get(i));
            }
            m_dataList.clear();
            m_test.postTest();
        }

        private void doRun() {
            for (int i = 0; i < m_iIters; i++) {
                long lStart = System.currentTimeMillis();
                m_test.run(m_result);
                long lEnd = System.currentTimeMillis();
                long lTime = lEnd - lStart;
                if (s_log.isInfoEnabled()) {
                    s_log.info(m_iTotalUsers + " Total Users. User " + this.hashCode() + " time: " + lTime + " miliseconds");
                }
                PerformanceData data = new PerformanceData(System.currentTimeMillis(), m_iTotalUsers, this.hashCode(), lTime);
                m_dataList.add(data);
                try {
                    Thread.sleep(m_iDelay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
