package org.cojen.tupl;

import java.lang.ref.WeakReference;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * 
 *
 * @author Brian S O'Neill
 */
final class Checkpointer implements Runnable {

    /**
     * Create for running in a dedicated thread.
     */
    static Checkpointer create(Database db, long rateNanos) {
        if (rateNanos < 0) {
            throw new IllegalArgumentException();
        }
        return new Checkpointer(db, rateNanos);
    }

    /**
     * Start running checkpointer task.
     */
    static Checkpointer start(Database db, long rateNanos, ScheduledExecutorService executor) throws RejectedExecutionException {
        if (rateNanos < 0) {
            throw new IllegalArgumentException();
        }
        if (rateNanos == 0) {
            rateNanos = 1;
        }
        Checkpointer c = new Checkpointer(db, -1);
        c.mTask = executor.scheduleAtFixedRate(c, rateNanos, rateNanos, TimeUnit.NANOSECONDS);
        return c;
    }

    private final WeakReference<Database> mDatabaseRef;

    private final long mRateNanos;

    private volatile ScheduledFuture mTask;

    private volatile boolean mCanceled;

    private Checkpointer(Database db, long rateNanos) {
        mDatabaseRef = new WeakReference<Database>(db);
        mRateNanos = rateNanos;
    }

    @Override
    public void run() {
        try {
            long lastDurationNanos = 0;
            while (true) {
                if (mRateNanos >= 0) {
                    long delayMillis = (mRateNanos - lastDurationNanos) / 1000000L;
                    if (delayMillis > 0) {
                        Thread.sleep(delayMillis);
                    }
                }
                Database db = mDatabaseRef.get();
                if (db == null) {
                    cancel();
                    return;
                }
                System.out.println("checkpoint...");
                long startNanos = System.nanoTime();
                db.checkpoint();
                long endNanos = System.nanoTime();
                System.out.println("...done");
                if (mRateNanos < 0) {
                    return;
                }
                lastDurationNanos = endNanos - startNanos;
            }
        } catch (Exception e) {
            if (!mCanceled) {
                Utils.uncaught(e);
            }
            cancel();
        }
    }

    void cancel() {
        mCanceled = true;
        mDatabaseRef.clear();
        ScheduledFuture task = mTask;
        if (task != null) {
            task.cancel(false);
        }
    }
}
