package com.continuent.bristlecone.benchmark;

import java.util.Properties;

/**
 * Denotes a benchmark scenario.  One instance of the scenario implementation 
 * class is instantiated for each separate thread in the benchmark run. 
 * @author rhodges
 */
public interface Scenario {

    /**
   * Initialize the scenario instance ready data for the run.  This method 
   * is called on all instances after calling setters.  If any initialize() 
   * method throws an exception, the run is terminated. 
   * 
   * @throws Exception Thrown in the even 
   */
    public void initialize(Properties properties) throws Exception;

    /** 
   * Perform data preparation for the entire scenario run.  This is called 
   * once across all scenario instances.  One scenario instance is selected at 
   * random to run this call.  If this method throws an Exception, the run is
   * terminated. 
   * 
   * @throws Exception Thrown in the event of a problem with initialization
   */
    public void globalPrepare() throws Exception;

    /**
   * Initialize an individual scenario for execution.  This runs before the 
   * scenario is scheduled to run on a thread and hence before the first
   * call to iterate().  If any scenario method throws an exception, the 
   * entire run is terminated. 
   * 
   * @throws Exception Thrown in the event of a problem with initialization
   */
    public void prepare() throws Exception;

    /** 
   * Runs a single scenario iteration.  The iteration method should perform
   * a single operation and then return.  If this method throws an Exception
   * the thread is terminated. 
   * 
   * @param iterationCount Current iteration count, starting with 1 and 
   * incremented with each additional iteration
   * @throws Exception Thrown in the event of a problem with execution 
   */
    public void iterate(long iterationCount) throws Exception;

    /**
   * Release resources for an individual scenario.  This is call is made
   * after the last call to iterate().  Exceptions are logged but do not 
   * prevent further cleanup from occurring.  
   * 
   * @throws Exception Thrown in the even that cleanup could not be accomplished
   */
    public void cleanup() throws Exception;

    /**
   * Release resources for the entire scenario run.  This call is made once 
   * after all scenario cleanup() methods have run.  One scenario instance is 
   * selected at random to run this call.  Exceptions are logged. 
   * 
   * @throws Exception
   */
    public void globalCleanup() throws Exception;
}
