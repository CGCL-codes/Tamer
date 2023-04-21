package net.sf.asyncobjects.util;

import java.util.concurrent.Semaphore;
import net.sf.asyncobjects.AsyncObject;
import net.sf.asyncobjects.Promise;

/**
 * <p>
 * This is an asynchronous version of {@link Semaphore}. It maintain internal
 * balance of permits. The method {@link #release(int)} increases the amount of
 * permits and the method {@link #acquire(int)} reduces the amount. However the
 * method acquire reduces amount of permits and resolves its result promise only
 * when amount of permits will be at least zero after substracting the permits.
 * </p>
 * 
 * <p>
 * The method {@link #drainPermits()} resets the amount of permits to zero when
 * all previously enqueued {@link #acquire(int)} completed.
 * </p>
 * 
 * <p>
 * Ivocations of the methods {@link #acquire(int)} and {@link #drainPermits()}
 * are ordered. the later invocation will not be executed until the first one is
 * complete. In contrast to it, the {@link #release(int)} methods are not
 * queued. They are executed as soon as they are received.
 * </p>
 * 
 * @author const
 */
public interface ASemaphore extends AsyncObject {

    /**
	 * @return when aquired one permit
	 */
    Promise<Void> acquire();

    /**
	 * Acquire a number of permits
	 * 
	 * @param number
	 *            amount of permits to aquired
	 * @return when permits are aquired
	 */
    Promise<Void> acquire(int number);

    /**
	 * Release a permit
	 */
    void release();

    /**
	 * Release a number of permits
	 * 
	 * @param number
	 *            a number of permits
	 */
    void release(int number);

    /**
	 * This is amount of permits that are released but have not yet been
	 * acquired with a {@link #acquire(int)} call. If there are pending
	 * {@link #acquire(int)} call, this could happen due to several reasons:
	 * <ul>
	 * <li>the {@link #acquire(int)} call could be not yet awaken.</li>
	 * <li>the argument of {@link #acquire(int)} is greater than amount of
	 * available permits.</li>
	 * </ul>
	 * 
	 * @return amount of currently available permits.
	 */
    Promise<Integer> availablePermits();

    /**
	 * @return return an permits balance. The sum of permits requested by
	 *         pending {@link #acquire(int)} calls is substracted from available
	 *         permits.
	 */
    Promise<Integer> permitsBalance();

    /**
	 * Drain all available permits. Note that this method waits until at least
	 * zero permits available. If less than zero permits available, the method
	 * blocks.
	 * 
	 * @return amount of permits that were available.
	 */
    Promise<Integer> drainPermits();
}
