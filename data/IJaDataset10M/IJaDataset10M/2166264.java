package net.grinder.util.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Create suitable {@Executor} implementations.
 *
 * @author Philip Aston
 * @version $Revision:$
 */
public class ExecutorFactory {

    private static class NamedThreadFactory implements ThreadFactory {

        private final ThreadGroup m_group;

        private final AtomicInteger m_threadNumber = new AtomicInteger(1);

        NamedThreadFactory(String name) {
            m_group = new ThreadGroup(name);
            m_group.setDaemon(true);
        }

        public Thread newThread(Runnable runnable) {
            final String threadName = m_group.getName() + "-" + m_threadNumber.getAndIncrement();
            return new Thread(m_group, runnable, threadName);
        }
    }

    /**
   * Create a fixed size thread pool.
   *
   * @param name
   *          Pool name.
   * @param numberOfThreads
   *          The number of threads.
   * @return The thread pool.
   */
    public static ExecutorService createThreadPool(String name, int numberOfThreads) {
        return Executors.newFixedThreadPool(numberOfThreads, new NamedThreadFactory(name));
    }

    /**
   * Create a cached thread pool.
   *
   * @param name
   *          Pool name.
   * @return The thread pool.
   */
    public static ExecutorService createCachedThreadPool(String name) {
        return Executors.newCachedThreadPool(new NamedThreadFactory(name));
    }
}
