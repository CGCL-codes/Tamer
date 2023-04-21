package org.waveprotocol.wave.client.scheduler;

/**
 * Interface for {@link FinalTaskRunnerImpl}
 * @author danilatos@google.com (Daniel Danilatos)
 */
public interface FinalTaskRunner {

    /**
   * Schedule a task for synchronous execution at the end of the current event
   * cycle.
   *
   * @param task
   */
    void scheduleFinally(Scheduler.Task task);
}
