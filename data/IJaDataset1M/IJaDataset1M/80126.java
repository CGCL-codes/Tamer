package de.searchworkorange.indexcrawler.crawler.indexer;

import java.util.concurrent.CopyOnWriteArrayList;
import org.apache.log4j.Level;
import de.searchworkorange.indexcrawler.Document.FileDocumentFactory;
import de.searchworkorange.indexcrawler.Document.exception.CanNotReadFromInputStreamException;
import de.searchworkorange.indexcrawler.Document.exception.CountingNotSetException;
import de.searchworkorange.indexcrawler.Document.exception.NoSimpleDocumentAvailableException;
import de.searchworkorange.indexcrawler.configuration.ConfigurationCollection;
import de.searchworkorange.indexcrawler.configuration.StaticConfiguration;
import de.searchworkorange.indexcrawler.crawler.statistic.RunningThreadsReporter;
import de.searchworkorange.lib.logger.LoggerCollection;
import de.searchworkorange.lib.misc.Semaphore;

/**
 * 
 * @author Sascha Kriegesmann kriegesmann at vaxnet.de
 */
public abstract class IndexerListenerWithSemaphore extends Semaphore {

    private static final boolean CLASSDEBUG = true;

    protected ConfigurationCollection config = null;

    protected CopyOnWriteArrayList<Thread> threadList = null;

    private int maxThreads;

    private int runningThreads;

    private Semaphore mutex = null;

    private Semaphore sema = null;

    protected RunningThreadsReporter reporter = null;

    private int openFiles = 0;

    /**
     * 
     * @param loggerCol
     * @param config
     * @param reporter
     * @param maxThreads
     */
    public IndexerListenerWithSemaphore(LoggerCollection loggerCol, ConfigurationCollection config, RunningThreadsReporter reporter, int maxThreads) {
        super(loggerCol, "IndexListenerSemaphore", maxThreads, StaticConfiguration.isWAIT_NOTIFY_DEBUG());
        if (reporter == null || config == null) {
            throw new IllegalArgumentException();
        } else {
            this.config = config;
            this.reporter = reporter;
            mutex = new Semaphore(loggerCol, "mutex", 1, StaticConfiguration.isWAIT_NOTIFY_DEBUG());
            sema = new Semaphore(loggerCol, "semaphore", 0, StaticConfiguration.isWAIT_NOTIFY_DEBUG());
            if (maxThreads < 0) {
                maxThreads = 0;
            }
            this.maxThreads = maxThreads;
            threadList = new CopyOnWriteArrayList<Thread>();
        }
    }

    /**
     * !! without synchronized 
     * @return CopyOnWriteArrayList<Thread>
     */
    public CopyOnWriteArrayList<Thread> getRunningThreads() {
        return threadList;
    }

    /**
     * 
     * @return int
     */
    protected int getThreadCount() {
        return threadList.size();
    }

    /**
     * 
     * @param threadToAdd
     */
    public synchronized void addThread(Thread threadToAdd) {
        loggerCol.logDebug(CLASSDEBUG, this.getClass().getName(), Level.DEBUG, "[add] Thread:" + threadToAdd + " runningThreads:" + runningThreads + " maxThreads:" + maxThreads);
        while (runningThreads == maxThreads) {
            try {
                loggerCol.logDebug(StaticConfiguration.isWAIT_NOTIFY_DEBUG(), this.getClass().getName(), Level.INFO, "[WAIT]:" + this);
                wait(3000);
                loggerCol.logDebug(StaticConfiguration.isWAIT_NOTIFY_DEBUG(), this.getClass().getName(), Level.INFO, "[AWAKE]:" + this);
            } catch (InterruptedException ex) {
                loggerCol.logException(CLASSDEBUG, this.getClass().getName(), Level.FATAL, ex);
            }
        }
        mutex.p();
        threadList.add(threadToAdd);
        mutex.v();
        sema.v();
        runningThreads++;
    }

    /**
     *
     * @param threadToRemove
     */
    public synchronized void removeThread(Thread threadToRemove) {
        loggerCol.logDebug(CLASSDEBUG, this.getClass().getName(), Level.DEBUG, "[remove] Thread:" + threadToRemove + " runningThreads:" + runningThreads + " maxThreads:" + maxThreads + " threadListSize:" + threadList.size());
        while (threadList.isEmpty()) {
            sema.p();
        }
        mutex.p();
        if (reporter.isStreamTrafficCounting()) {
            if (threadToRemove instanceof FileDocumentFactory) {
                FileDocumentFactory fdf = (FileDocumentFactory) threadToRemove;
                if (config.isStreamTrafficCounting()) {
                    try {
                        reporter.sumTrafficCount(fdf.getTrafficCount());
                    } catch (CanNotReadFromInputStreamException ex) {
                        loggerCol.logException(CLASSDEBUG, this.getClass().getName(), Level.FATAL, ex);
                    } catch (NoSimpleDocumentAvailableException ex) {
                        loggerCol.logException(CLASSDEBUG, this.getClass().getName(), Level.FATAL, ex);
                    } catch (CountingNotSetException ex) {
                        loggerCol.logException(CLASSDEBUG, this.getClass().getName(), Level.FATAL, ex);
                    }
                }
            }
        }
        if (threadList.contains(threadToRemove)) {
            threadList.remove(threadToRemove);
        }
        mutex.v();
        runningThreads--;
        notify();
    }

    /**
     * 
     * @return synchronized long
     */
    public long getPossibleOpenFilesPerSingleThread() {
        if (runningThreads > 0) {
            long div = (long) config.getMAX_FILES_COUNT() / (long) runningThreads;
            if (div + openFiles >= config.getMAX_FILES_COUNT()) {
                return div;
            } else {
                return config.getMAX_FILES_COUNT() - openFiles;
            }
        } else {
            return 0;
        }
    }

    /**
     *
     * @return synchronized long
     */
    public long getMaxOpenFilesPerSingleThread() {
        if (config.getMaxThreads() > 0) {
            return (long) config.getMAX_FILES_COUNT() / (long) config.getMaxThreads();
        } else {
            return 0;
        }
    }

    public void decrementOpenFiles() {
        if (openFiles > 0) {
            openFiles--;
        }
    }

    public void incrementOpenFiles() {
        if (openFiles <= config.getMAX_FILES_COUNT()) {
            openFiles++;
        }
    }

    /**
     *
     * @return synchronized int
     */
    public synchronized int getOpenFiles() {
        return openFiles;
    }

    protected void removeIndexListenerFromRunningThreadsReporter() {
        reporter.removeReporterListener(this);
    }
}
