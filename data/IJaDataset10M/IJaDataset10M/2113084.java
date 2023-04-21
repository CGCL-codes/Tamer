package net.geoffs.jPhotoArchive;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import net.geoffs.jPhotoArchive.ArchiverBase.JobResults;

public class JpaExecutor {

    private final ThreadPoolExecutor tpe;

    private static final int CORE_POOL_SIZE = 4;

    private static final int MAX_POOL_SIZE = 4;

    private static final long KEEP_ALIVE_TIME = 60;

    public JpaExecutor() {
        BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>();
        tpe = new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.SECONDS, workQueue);
    }

    public void submit(final Runnable runnable, final JobResults results) {
        try {
            tpe.execute(runnable);
        } catch (Exception e) {
            System.out.println("ThreadPoolExecutor.execute() threw an Exception.");
            results.addError(e);
            e.printStackTrace();
        }
    }

    public void shutdown(final JobResults results) {
        System.out.println("Shutting down the queue.");
        tpe.shutdown();
        long timeout = MAX_POOL_SIZE * 60;
        try {
            System.out.println("Waiting for Queue to terminate.");
            boolean normalTermination = tpe.awaitTermination(timeout, TimeUnit.SECONDS);
            System.out.println("Queue is terminated with " + normalTermination);
            if (!normalTermination) {
                results.addError("ThreadPoolExecutor.awaitTermination() indicated a timeout occured.");
            }
        } catch (Exception e) {
            System.out.println("ThreadPoolExecutor.awaitTermination() threw an Exception");
            results.addError(e);
            e.printStackTrace();
        }
    }
}
