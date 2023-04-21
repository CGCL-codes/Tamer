package org.tigr.cloe.model.traceManager;

import org.tigr.seq.display.AppUtil;
import org.tigr.seq.seqdata.IBaseAssemblySequence;
import org.tigr.seq.seqdata.display.IAssemblyDisplayPreferences;

/**
     * A thread that spawns and then watches TraceFetcherThreads.
     * This way we can monitor the progress of TraceFetcherThreads
     * and kill them if they take too long or if the user cancels.
     * @author dkatzel
     *
     *
     */
public class TraceFetcherWatcher implements Runnable {

    TraceFetcherThread watchedThread;

    public TraceFetcherWatcher() {
    }

    public TraceFetcherWatcher(IBaseAssemblySequence seq, IAssemblyDisplayPreferences displayPrefs) {
        watchedThread = new TraceFetcherThread(seq, displayPrefs);
    }

    public void setWatchedThread(TraceFetcherThread t) {
        watchedThread = t;
    }

    public void run() {
        watchedThread.start();
        try {
            watchedThread.join(30000);
        } catch (Exception e) {
            System.out.println("interrupting thread");
            watchedThread.setDead(true);
            watchedThread.interrupt();
        } finally {
            if (watchedThread.isDead()) {
                AppUtil.notifyTraceKilled(toString());
            } else {
                AppUtil.notifyTraceFinsih(this);
            }
        }
    }

    public String toString() {
        return watchedThread.toString();
    }

    public TraceFetcherThread getWatchedThread() {
        return watchedThread;
    }
}
