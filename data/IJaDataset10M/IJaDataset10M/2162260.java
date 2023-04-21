package org.hibernate.search.store;

import java.io.File;
import java.io.IOException;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import org.apache.lucene.store.FSDirectory;
import org.slf4j.Logger;
import org.hibernate.search.spi.BuildContext;
import org.hibernate.search.SearchException;
import org.hibernate.search.util.FileHelper;
import org.hibernate.search.util.LoggerFactory;

public class FSMasterDirectoryProvider implements DirectoryProvider<FSDirectory> {

    private static final String CURRENT1 = "current1";

    private static final String CURRENT2 = "current2";

    private static final String[] CURRENT_DIR_NAME = { null, CURRENT1, CURRENT2 };

    private static final Logger log = LoggerFactory.make();

    private final Timer timer = new Timer(true);

    private volatile int current;

    private FSDirectory directory;

    private String indexName;

    private BuildContext context;

    private long copyChunkSize;

    private File sourceDir;

    private File indexDir;

    private String directoryProviderName;

    private Properties properties;

    private TriggerTask task;

    private Lock directoryProviderLock;

    public void initialize(String directoryProviderName, Properties properties, BuildContext context) {
        this.properties = properties;
        this.directoryProviderName = directoryProviderName;
        sourceDir = DirectoryProviderHelper.getSourceDirectory(directoryProviderName, properties, true);
        log.debug("Source directory: {}", sourceDir.getPath());
        indexDir = DirectoryProviderHelper.getVerifiedIndexDir(directoryProviderName, properties, true);
        log.debug("Index directory: {}", indexDir.getPath());
        try {
            indexName = indexDir.getCanonicalPath();
            directory = DirectoryProviderHelper.createFSIndex(indexDir, properties);
        } catch (IOException e) {
            throw new SearchException("Unable to initialize index: " + directoryProviderName, e);
        }
        copyChunkSize = DirectoryProviderHelper.getCopyBufferSize(directoryProviderName, properties);
        this.context = context;
        current = 0;
    }

    public void start() {
        int currentLocal = 0;
        this.directoryProviderLock = this.context.getDirectoryProviderLock(this);
        this.context = null;
        try {
            if (new File(sourceDir, CURRENT1).exists()) {
                currentLocal = 2;
            } else if (new File(sourceDir, CURRENT2).exists()) {
                currentLocal = 1;
            } else {
                log.debug("Source directory for '{}' will be initialized", indexName);
                currentLocal = 1;
            }
            String currentString = Integer.valueOf(currentLocal).toString();
            File subDir = new File(sourceDir, currentString);
            FileHelper.synchronize(indexDir, subDir, true, copyChunkSize);
            new File(sourceDir, CURRENT1).delete();
            new File(sourceDir, CURRENT2).delete();
            new File(sourceDir, CURRENT_DIR_NAME[currentLocal]).createNewFile();
            log.debug("Current directory: {}", currentLocal);
        } catch (IOException e) {
            throw new SearchException("Unable to initialize index: " + directoryProviderName, e);
        }
        task = new FSMasterDirectoryProvider.TriggerTask(indexDir, sourceDir);
        long period = DirectoryProviderHelper.getRefreshPeriod(properties, directoryProviderName);
        timer.scheduleAtFixedRate(task, period, period);
        this.current = currentLocal;
    }

    public FSDirectory getDirectory() {
        @SuppressWarnings("unused") int readCurrentState = current;
        return directory;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || !(obj instanceof FSMasterDirectoryProvider)) return false;
        FSMasterDirectoryProvider other = (FSMasterDirectoryProvider) obj;
        @SuppressWarnings("unused") int readCurrentState = other.current;
        readCurrentState = this.current;
        return indexName.equals(other.indexName);
    }

    @Override
    public int hashCode() {
        @SuppressWarnings("unused") int readCurrentState = current;
        int hash = 11;
        return 37 * hash + indexName.hashCode();
    }

    public void stop() {
        @SuppressWarnings("unused") int readCurrentState = current;
        timer.cancel();
        task.stop();
        try {
            directory.close();
        } catch (Exception e) {
            log.error("Unable to properly close Lucene directory {}" + directory.getDirectory(), e);
        }
    }

    private class TriggerTask extends TimerTask {

        private final ExecutorService executor;

        private final FSMasterDirectoryProvider.CopyDirectory copyTask;

        public TriggerTask(File source, File destination) {
            executor = Executors.newSingleThreadExecutor();
            copyTask = new FSMasterDirectoryProvider.CopyDirectory(source, destination);
        }

        public void run() {
            if (copyTask.inProgress.compareAndSet(false, true)) {
                executor.execute(copyTask);
            } else {
                log.info("Skipping directory synchronization, previous work still in progress: {}", indexName);
            }
        }

        public void stop() {
            executor.shutdownNow();
        }
    }

    private class CopyDirectory implements Runnable {

        private final File source;

        private final File destination;

        private final AtomicBoolean inProgress = new AtomicBoolean(false);

        public CopyDirectory(File source, File destination) {
            this.source = source;
            this.destination = destination;
        }

        public void run() {
            directoryProviderLock.lock();
            try {
                long start = System.nanoTime();
                int oldIndex = current;
                int index = oldIndex == 1 ? 2 : 1;
                File destinationFile = new File(destination, Integer.valueOf(index).toString());
                try {
                    log.trace("Copying {} into {}", source, destinationFile);
                    FileHelper.synchronize(source, destinationFile, true, copyChunkSize);
                    current = index;
                } catch (IOException e) {
                    log.error("Unable to synchronize source of " + indexName, e);
                    return;
                }
                if (!new File(destination, CURRENT_DIR_NAME[oldIndex]).delete()) {
                    log.warn("Unable to remove previous marker file from source of {}", indexName);
                }
                try {
                    new File(destination, CURRENT_DIR_NAME[index]).createNewFile();
                } catch (IOException e) {
                    log.warn("Unable to create current marker in source of " + indexName, e);
                }
                log.trace("Copy for {} took {} ms", indexName, TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start));
            } finally {
                directoryProviderLock.unlock();
                inProgress.set(false);
            }
        }
    }
}
