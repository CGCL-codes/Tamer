package org.jgentleframework.services.objectpooling;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;
import org.jgentleframework.utils.ReflectUtils;
import org.jgentleframework.utils.data.TimestampObjectBean;

/**
 * The Class PoolUtils.
 * 
 * @author Quoc Chung - mailto: <a
 *         href="mailto:skydunkpro@yahoo.com">skydunkpro@yahoo.com</a>
 * @date Apr 9, 2009
 */
public final class PoolStaticUtils {

    /**
	 * Provides a shared idle object eviction timer for all pools. This class
	 * wraps the standard {@link Timer} and keeps track of how many pools are
	 * using it. If no pools are using the timer, it is canceled. This prevents
	 * a thread being left running which, in application server environments,
	 * can lead to memory leads and/or prevent applications from shutting down
	 * or reloading cleanly.
	 * <p>
	 * This class has package scope to prevent its inclusion in the pool public
	 * API. The class declaration below should *not* be changed to public.
	 */
    static class EvictionTimer {

        /** The _timer. */
        private static Timer timer;

        /** The _usage count. */
        private static int usageCount;

        /**
		 * Remove the specified eviction task from the timer.
		 * 
		 * @param task
		 *            Task to be scheduled
		 */
        static synchronized void cancel(TimerTask task) {
            task.cancel();
            usageCount--;
            if (usageCount == 0) {
                timer.cancel();
                timer = null;
            }
        }

        /**
		 * Add the specified eviction task to the timer. Tasks that are added
		 * with a call to this method *must* call {@link #cancel(TimerTask)} to
		 * cancel the task to prevent memory and/or thread leaks in application
		 * server environments.
		 * 
		 * @param task
		 *            Task to be scheduled
		 * @param delay
		 *            Delay in milliseconds before task is executed
		 * @param period
		 *            Time in milliseconds between executions
		 */
        static synchronized void schedule(TimerTask task, long delay, long period) {
            if (null == timer) {
                timer = new Timer(true);
            }
            usageCount++;
            timer.schedule(task, delay, period);
        }

        /**
		 * Instantiates a new eviction timer.
		 */
        private EvictionTimer() {
        }
    }

    /**
	 * Call <code>addObject()</code> on <code>pool</code> <code>count</code>
	 * number of times.
	 * 
	 * @param pool the pool to prefill.
	 * @param count the number of idle objects to add.
	 * 
	 * @throws Exception the exception
	 */
    public static void initIdleObject(final BasePooling pool, final int count) throws Exception {
        if (pool == null) {
            throw new IllegalArgumentException("pool must not be null.");
        }
        for (int i = 0; i < count; i++) {
            pool.addObject();
        }
    }

    /**
	 * Calculates deficit.
	 * 
	 * @param basePool
	 *            the base pool
	 * @return the int
	 */
    private static int calculateDeficit(BasePooling basePool) {
        synchronized (basePool) {
            int objectDeficit = basePool.getMinIdle() - basePool.getNumIdle();
            if (basePool.getMaxPoolSize() > 0) {
                int growLimit = Math.max(0, basePool.getMaxPoolSize() - basePool.getNumActive() - basePool.getNumIdle());
                objectDeficit = Math.min(objectDeficit, growLimit);
            }
            return objectDeficit;
        }
    }

    /**
	 * Check to see if we are below our minimum number of objects if so enough
	 * to bring us back to our minimum.
	 * 
	 * @param basePool
	 *            the base pool
	 * @throws Exception
	 *             when {@link Pool#addObject()} fails.
	 */
    public static void ensureMinIdle(BasePooling basePool) throws Throwable {
        int objectDeficit = calculateDeficit(basePool);
        for (int j = 0; j < objectDeficit && calculateDeficit(basePool) > 0; j++) {
            basePool.addObject();
        }
    }

    /**
	 * Gets the num tests.
	 * 
	 * @param basePool
	 *            the base pool
	 * @return the num tests
	 */
    private static int getNumTests(AbstractBasePooling basePool) {
        int numTestsPerEvictionRun = basePool.getNumTestsPerEvictionRun();
        if (numTestsPerEvictionRun >= 0) {
            return Math.min(numTestsPerEvictionRun, basePool.getNumIdle());
        } else {
            return (int) (Math.ceil((double) basePool.getNumIdle() / Math.abs((double) numTestsPerEvictionRun)));
        }
    }

    /**
	 * Perform <code>numTests</code> idle object eviction tests, evicting
	 * examined objects that meet the criteria for eviction. If
	 * <code>testWhileIdle</code> is true, examined objects are validated when
	 * visited (and removed if invalid); otherwise only objects that have been
	 * idle for more than <code>minEvicableIdletimeMillis</code> are removed.
	 * <p>
	 * Successive activations of this method examine objects in in sequence,
	 * cycling through objects in oldest-to-youngest order.
	 * 
	 * @param pool
	 *            the pool
	 * @throws Exception
	 *             if the pool is closed or eviction fails.
	 */
    public static synchronized void evict(AbstractBaseFactory pool, boolean lifo) throws Exception {
        synchronized (pool) {
            pool.assertDisable();
            if (pool.isEmpty()) {
                int numTest = 0;
                int numTestsPerRun = getNumTests(pool);
                TimestampObjectBean<Object> pair = null;
                if (!lifo) {
                    if (ReflectUtils.isCast(Queue.class, pool.pool)) {
                        for (Iterator<TimestampObjectBean<Object>> iterator = pool.pool.iterator(); iterator.hasNext(); ) {
                            numTest++;
                            if (numTest > numTestsPerRun) break; else {
                                try {
                                    pair = (TimestampObjectBean<Object>) iterator.next();
                                } catch (NoSuchElementException e) {
                                    break;
                                }
                                evict(pair, pool);
                            }
                        }
                    }
                } else {
                    if (ReflectUtils.isCast(Stack.class, pool.pool)) {
                        Stack<TimestampObjectBean<Object>> stack = (Stack<TimestampObjectBean<Object>>) pool.pool;
                        while (stack.size() > 0) {
                            numTest++;
                            if (numTest > numTestsPerRun) break; else {
                                try {
                                    pair = (TimestampObjectBean<Object>) stack.get(stack.size() - numTest);
                                } catch (ArrayIndexOutOfBoundsException e) {
                                    break;
                                }
                                evict(pair, pool);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
	 * Evict.
	 * 
	 * @param pair
	 *            the pair
	 * @param pool
	 *            the pool
	 */
    private static void evict(TimestampObjectBean<Object> pair, AbstractBaseFactory pool) {
        synchronized (pool) {
            if (pair != null) {
                boolean remove = false;
                final long idleTimeMilis = System.currentTimeMillis() - pair.getTstamp();
                if ((pool.getMinEvictableIdleTime() > 0) && (idleTimeMilis > pool.getMinEvictableIdleTime())) {
                    remove = true;
                } else if ((pool.getSoftMinEvictableIdleTime() > 0) && (idleTimeMilis > pool.getSoftMinEvictableIdleTime()) && (pool.getNumIdle() > pool.getMinIdle())) {
                    remove = true;
                }
                if (pool.isTestWhileIdle() && !remove) {
                    boolean active = false;
                    try {
                        pool.activatesObject(pair.getValue());
                        active = true;
                    } catch (Exception e) {
                        remove = true;
                    }
                    if (active) {
                        try {
                            pool.validatesObject(pair.getValue());
                            pool.deactivateObject(pair.getValue());
                        } catch (Throwable e) {
                            remove = true;
                        }
                    }
                }
                if (remove) {
                    pool.pool.remove(pair);
                    try {
                        pool.destroyObject(pair.getValue());
                    } catch (Throwable e) {
                    }
                }
            }
        }
    }

    /**
	 * Start the eviction thread or service, or when <i>delay</i> is
	 * non-positive, stop it if it is already running.
	 * 
	 * @param evictor
	 *            the evictor
	 * @param delay
	 *            milliseconds between evictor runs.
	 * @param basePool
	 *            the base pool
	 * @param lifo
	 *            if is <code>'last in first out'</code>
	 */
    public static void startEvictor(Evictor evictor, long delay, AbstractBaseFactory basePool, boolean lifo) {
        synchronized (basePool) {
            if (null != evictor) {
                EvictionTimer.cancel(evictor);
                evictor = null;
            }
            if (delay > 0) {
                evictor = new Evictor(basePool, lifo);
                EvictionTimer.schedule(evictor, delay, delay);
            }
        }
    }
}
