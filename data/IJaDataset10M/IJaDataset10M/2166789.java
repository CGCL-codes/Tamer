package net.stickycode.scheduler;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * Contract for off loading the execution of a job to an application define background processor
 */
public interface BackgroundExecutor {

    /**
   * Add the given runnable to a queue for execution
   */
    void execute(Runnable job);

    /**
   * Submit a {@link Callable} for execution later.
   * 
   * @see ExecutorService
   */
    <T> Future<T> submit(Callable<T> task);
}
