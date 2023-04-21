package org.openthinclient.nfsd;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;

/**
 * @author levigo
 */
public class CacheCleaner {

    private static final Logger logger = Logger.getLogger(CacheCleaner.class);

    private static class Janitor implements Runnable {

        private final BlockingQueue<NFSFile> taintQueue;

        private final Set<NFSFile> dirtyFilesSet = new HashSet<NFSFile>();

        private final Set<NFSFile> openFilesSet = new HashSet<NFSFile>();

        private boolean shutdownRequested;

        public Janitor(BlockingQueue<NFSFile> taintQueue) {
            this.taintQueue = taintQueue;
            final Thread t = new Thread(this, "CacheCleaner");
            t.setDaemon(true);
            t.setPriority(Thread.NORM_PRIORITY - 1);
            t.start();
        }

        private static long EXPIRY_INTERVAL = 5000;

        private static final int MAX_OPEN_FILES = 256;

        public void run() {
            while (!shutdownRequested) try {
                final NFSFile file = taintQueue.poll(EXPIRY_INTERVAL, TimeUnit.MILLISECONDS);
                if (null != file) {
                    dirtyFilesSet.add(file);
                    if (file.isChannelOpen()) {
                        openFilesSet.add(file);
                        if (openFilesSet.size() >= MAX_OPEN_FILES) watermarkCacheFlush();
                    }
                } else expire();
            } catch (final InterruptedException e) {
            }
        }

        private synchronized void expire() {
            logger.debug("Running expiry");
            for (final Iterator<NFSFile> i = dirtyFilesSet.iterator(); i.hasNext(); ) {
                final NFSFile file = i.next();
                synchronized (file) {
                    if (file.getLastAccessTimestamp() + file.getExport().getCacheTimeout() < System.currentTimeMillis()) try {
                        if (logger.isDebugEnabled()) logger.debug("Flushing cache for " + file.getFile());
                        file.flushCache();
                        openFilesSet.remove(file);
                        i.remove();
                    } catch (final IOException e) {
                        logger.warn("Got exception flushing cache for " + file.getFile());
                    }
                }
            }
            if (dirtyFilesSet.size() == 0 && openFilesSet.size() != 0) openFilesSet.clear();
            if (logger.isDebugEnabled()) logger.debug("Expiry done. Dirty files remaining: " + dirtyFilesSet.size() + " open files remaining: " + openFilesSet.size());
        }

        private synchronized void watermarkCacheFlush() {
            logger.info("Number of open files: " + openFilesSet.size() + ". Forcing flush of cache.");
            final List<NFSFile> sortedList = new ArrayList<NFSFile>(dirtyFilesSet);
            Collections.sort(sortedList, new Comparator<NFSFile>() {

                public int compare(NFSFile f1, NFSFile f2) {
                    return (int) (f1.getLastAccessTimestamp() - f2.getLastAccessTimestamp());
                }
            });
            final int lowWaterMark = MAX_OPEN_FILES * 2 / 3;
            for (final Iterator<NFSFile> i = sortedList.iterator(); i.hasNext(); ) {
                final NFSFile file = i.next();
                synchronized (file) {
                    try {
                        if (logger.isDebugEnabled()) logger.debug("Flushing cache for file of age " + (System.currentTimeMillis() - file.getLastAccessTimestamp()));
                        file.flushCache();
                        openFilesSet.remove(file);
                        dirtyFilesSet.remove(file);
                    } catch (final IOException e) {
                        logger.warn("Got exception flushing cache for " + file.getFile());
                    }
                }
                if (openFilesSet.size() < lowWaterMark) break;
            }
        }
    }

    private static final BlockingQueue<NFSFile> taintQueue = new LinkedBlockingQueue<NFSFile>();

    private static final Janitor janitor = new Janitor(taintQueue);

    public static void registerDirtyFile(NFSFile file) {
        if (null != file) if (file.isChannelOpen()) taintQueue.offer(file);
    }
}
