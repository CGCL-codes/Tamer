package com.ibm.realtime.flexotask;

/**
 * Interface to be implemented either by the system or by a distributer component to provide time
 *   information to the scheduler and to TimeAware Flexotasks (see {@link TimeAware}).
 */
public interface FlexotaskTimer {

    /**
   * Provide an arbitrary-origin high-precision time
   * @return a time in nanoseconds from an arbitrary origin
   */
    public long nanoTime();
}
