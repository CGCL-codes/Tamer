package com.phloc.commons.callback;

/**
 * A simple interface that looks like {@link java.lang.Runnable} but may throw
 * an exception on its execution.<br>
 * Note: It is not possible to extend {@link java.lang.Runnable} directly, as
 * derived interfaces are not allowed to add exception specifications.
 * 
 * @author philip
 */
public interface IThrowingRunnable {

    /**
   * Run it.
   * 
   * @throws Exception
   *         In case something goes wrong.
   */
    void run() throws Exception;
}
