package com.ibm.realtime.flexotask.scheduling;

/**
 * Object that allows the scheduler to run and collect an individual Flexotask.
 */
public interface FlexotaskController extends Runnable {

    /**
   * Garbage-collect the heap of this flexotask.  The scheduler calls this method, and must ensure that
   * the flexotask isn't both running and being collected at the same time.
   * @return true if collection occured, false if it did not occur due to contention from an
   *   external thread.  This can only happen (currently) with transactional exotasks.
   */
    public boolean collect();

    /**
   * Run the flexotask code body.  The scheduler calls this method, and must ensure that the flexotask
   *   isn't both running and being collected at the same time.  Also, that the input ports of
   *   the flexotask are specified before running and that the output ports aren't read until the
   *   flexotask returns.
   */
    public void run();
}
