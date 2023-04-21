package com.phloc.commons.concurrent;

import java.util.concurrent.ExecutorService;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

/**
 * Factory for creating {@link ExecutorService} instances.
 * 
 * @author philip
 */
public interface IExecutorServiceFactory {

    /**
   * Get an {@link ExecutorService} for the given number of parallel tasks. It
   * is up to the implementation to interpret the value or not. The number of
   * parallel tasks can therefore considered a hint to the implementation.
   * 
   * @param nParallelTasks
   *        The number of parallel tasks to perform. Needs to be &gt; 0.
   * @return A non-<code>null</code> {@link ExecutorService} object.
   */
    @Nonnull
    ExecutorService getExecutorService(@Nonnegative int nParallelTasks);
}
