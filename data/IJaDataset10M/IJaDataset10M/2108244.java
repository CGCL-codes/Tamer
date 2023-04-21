package org.jcvi.vapor;

import java.util.concurrent.CountDownLatch;

/**
 * A <code>CleanupThread</code> is responsible for cleaning up the state of a {@link Vapor}
 * invokation after all work has been completed.
 *
 * @author jsitz@jcvi.org
 */
public class CleanupThread extends Thread {

    private final CountDownLatch lock;

    private final Vapor vapor;

    public CleanupThread(CountDownLatch lock, Vapor vapor) {
        super();
        this.lock = lock;
        this.vapor = vapor;
    }

    @Override
    public void run() {
        try {
            this.lock.await();
        } catch (final InterruptedException e) {
            this.vapor.getLogger().warn("Vapor cleanup thread interrupted while waiting for completion.");
        }
        this.vapor.shutdownAllExecutors();
        this.vapor.getLogger().info("Run complete.");
    }
}
