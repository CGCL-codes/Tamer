package mil.army.usace.ehlschlaeger.rgik.util;

import java.util.List;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Fake ExecutorService that runs all jobs in the caller's thread. Use this to
 * make it easy to switch between fully-concurrent and fully-serial execution.
 * 
 * @author William R. Zwicky
 */
public class NullExecutorService extends AbstractExecutorService {

    boolean dead = false;

    public NullExecutorService() {
    }

    /**
     * Does nothing, and <b>does not block</b>. If we block, your program would
     * never resume, as there are no other threads to unblock it. 'false' is to
     * be interpreted as 'timeout expired', so we return false if shutdown() was
     * never called.
     * 
     * @param timeout
     *            ignored
     * @param unit
     *            ignored
     * 
     * @return true if shutdown() was ever called; false if not
     */
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return dead;
    }

    public boolean isShutdown() {
        return dead;
    }

    public boolean isTerminated() {
        return dead;
    }

    public void shutdown() {
        dead = true;
    }

    public List<Runnable> shutdownNow() {
        dead = true;
        return null;
    }

    /**
     * Run command in caller's thread right now.  Blocks until complete.
     */
    public void execute(Runnable command) {
        if (dead) throw new RejectedExecutionException(getClass().getSimpleName() + " has been shut down"); else command.run();
    }
}
