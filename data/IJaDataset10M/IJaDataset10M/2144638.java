package org.gromurph.util.swingworker;

/**
 * TimedCallable runs a Callable function for a given length of time.
 * The function is run in its own thread. If the function completes
 * in time, its result is returned; otherwise the thread is interrupted
 * and an InterruptedException is thrown.
 * <p>
 * Note: TimedCallable will always return within the given time limit
 * (modulo timer inaccuracies), but whether or not the worker thread
 * stops in a timely fashion depends on the interrupt handling in the
 * Callable function's implementation. 
 *
 * @author  Joseph Bowbeer
 * @version 1.0
 *
* <p>[<a href="http://gee.cs.oswego.edu/dl"> Introduction to this package. </a>]
 */
public class TimedCallable extends ThreadFactoryUser implements Callable {

    private final Callable function;

    private final long millis;

    public TimedCallable(Callable function, long millis) {
        this.function = function;
        this.millis = millis;
    }

    public Object call() throws Exception {
        FutureResult result = new FutureResult();
        Thread thread = getThreadFactory().newThread(result.setter(function));
        thread.start();
        try {
            return result.timedGet(millis);
        } catch (InterruptedException ex) {
            thread.interrupt();
            throw ex;
        }
    }
}
