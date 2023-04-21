package org.apache.harmony.kernel.vm;

import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 * <p>
 * This class must be implemented by the VM to support the Threading subsystem.
 * </p>
 */
public class Threads {

    private static final Threads INSTANCE = new Threads();

    /**
     * <p>
     * Retrieves an instance of the Threads service.
     * </p>
     * 
     * @return An instance of Threads.
     */
    public static Threads getInstance() {
        return AccessController.doPrivileged(new PrivilegedAction<Threads>() {

            public Threads run() {
                return INSTANCE;
            }
        });
    }

    private Threads() {
        super();
    }

    /**
     * <p>
     * Unparks the {@link Thread} that's passed.
     * </p>
     * 
     * @param thread The {@link Thread} to unpark.
     */
    public void unpark(Thread thread) {
        return;
    }

    /**
     * <p>
     * Park the {@link Thread#currentThread() current thread} for the specified
     * number of nanoseconds.
     * </p>
     * 
     * @param nanoseconds The number of nanoseconds to park the current thread.
     */
    public void parkFor(long nanoseconds) {
    }

    /**
     * <p>
     * Park the {@link Thread#currentThread() current thread} until the
     * specified time, as defined by {@link System#currentTimeMillis()}.
     * </p>
     * 
     * @param time The absolute time to park the current thread until.
     */
    public void parkUntil(long time) {
    }
}
