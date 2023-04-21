package net.sf.eBus.util;

import java.util.concurrent.Executor;

/**
 * Performs direct, synchronous runnable task execution in the
 * current thread. This class allows an application to maintain
 * an {@code java.util.Executor} reference and instantiate the
 * appropriate executor subclass which performs either
 * synchronous or asynchronous execution. The application can
 * switch between different task execution styles without
 * changing how it uses the {@code Executor} instance.
 * <p>
 * (Aside: this class should be in java.util.concurrent to
 * provide a complete Executor implementation suite but is
 * not part of the standard Java class library. Hence it
 * being part of the eBus library).
 *
 * @author charlesr
 */
public class DirectExecutor implements Executor {

    /**
     * Creates a new instance of DirectExecutor.
     */
    public DirectExecutor() {
    }

    /**
     * Immediately executes the task in the current thread.
     * @param task Execute this task.
     */
    @Override
    public void execute(final Runnable task) {
        task.run();
        return;
    }
}
