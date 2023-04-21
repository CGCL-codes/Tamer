package org.armedbear.j;

import java.util.Iterator;
import java.util.Vector;

public final class DirectoryCache {

    private static final int timeout = 1800000;

    private static DirectoryCache cache;

    private Vector entries = new Vector();

    public static synchronized DirectoryCache getDirectoryCache() {
        if (cache == null) {
            cache = new DirectoryCache();
            IdleThread idleThread = IdleThread.getInstance();
            if (idleThread != null) idleThread.maybeAddTask(PruneDirectoryCacheTask.getInstance());
        }
        return cache;
    }

    public synchronized String getListing(File file) {
        String netPath = file.netPath();
        for (int i = entries.size(); i-- > 0; ) {
            DirectoryCacheEntry entry = (DirectoryCacheEntry) entries.get(i);
            if (entry.getFile().netPath().equals(netPath)) {
                if (entry.getWhen() + timeout < System.currentTimeMillis()) {
                    Log.debug("removing cache entry for " + entry.getFile().netPath());
                    entries.remove(i);
                    return null;
                }
                return entry.getListing();
            }
        }
        return null;
    }

    public synchronized void put(File file, String listing) {
        String netPath = file.netPath();
        for (int i = entries.size(); i-- > 0; ) {
            DirectoryCacheEntry entry = (DirectoryCacheEntry) entries.get(i);
            if (entry.getFile().netPath().equals(netPath)) {
                entries.remove(i);
                break;
            }
        }
        if (listing != null && listing.length() > 0) {
            entries.add(new DirectoryCacheEntry(file, listing, System.currentTimeMillis()));
        }
    }

    public synchronized void purge(String hostname) {
        for (int i = entries.size(); i-- > 0; ) {
            DirectoryCacheEntry entry = (DirectoryCacheEntry) entries.get(i);
            if (entry.getFile().getHostName().equals(hostname)) {
                Log.debug("removing cache entry for " + entry.getFile().netPath());
                entries.remove(i);
            }
        }
    }

    public synchronized void purge(File file) {
        String netPath = file.netPath();
        for (int i = entries.size(); i-- > 0; ) {
            DirectoryCacheEntry entry = (DirectoryCacheEntry) entries.get(i);
            if (entry.getFile().netPath().equals(netPath)) {
                Log.debug("removing cache entry for " + netPath);
                entries.remove(i);
            }
        }
    }

    private static class PruneDirectoryCacheTask extends IdleThreadTask {

        private static PruneDirectoryCacheTask instance;

        private long lastRun;

        private PruneDirectoryCacheTask() {
            lastRun = System.currentTimeMillis();
            setIdle(300000);
            setRunnable(runnable);
        }

        private static synchronized PruneDirectoryCacheTask getInstance() {
            if (instance == null) instance = new PruneDirectoryCacheTask();
            return instance;
        }

        private final Runnable runnable = new Runnable() {

            public void run() {
                if (System.currentTimeMillis() - lastRun > 300000) {
                    long now = System.currentTimeMillis();
                    synchronized (cache) {
                        Iterator it = cache.entries.iterator();
                        while (it.hasNext()) {
                            DirectoryCacheEntry entry = (DirectoryCacheEntry) it.next();
                            if (entry.getWhen() + timeout < now) it.remove();
                        }
                    }
                    lastRun = now;
                }
            }
        };
    }
}
