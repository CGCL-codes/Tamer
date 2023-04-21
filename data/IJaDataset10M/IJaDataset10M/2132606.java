package com.goodcodeisbeautiful.archtea.app;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.goodcodeisbeautiful.archtea.config.ArchteaMainConfig;
import com.goodcodeisbeautiful.archtea.config.ArchteaPropertyFactory;
import com.goodcodeisbeautiful.archtea.config.DefaultConfigManager;
import com.goodcodeisbeautiful.archtea.search.ArchteaIndexer;
import com.goodcodeisbeautiful.archtea.util.ArchteaException;
import com.goodcodeisbeautiful.util.NextTime;

/**
 * @author hata
 *
 */
public class ArchteaIndexerService implements ArchteaService {

    /** log */
    private static final Log log = LogFactory.getLog(ArchteaIndexerService.class);

    private ExecutorService _pool;

    private ArchteaIndexer _indexer;

    private volatile boolean _stop;

    private volatile long _maxWaitForStopThread;

    private volatile boolean _optimizeFlag;

    Future<Object> _indexerResult;

    Future<Object> _optimizingResult;

    public void init(Object args) throws ArchteaException {
        try {
            _stop = false;
            _pool = Executors.newFixedThreadPool(2);
            _maxWaitForStopThread = ArchteaPropertyFactory.DEFAULT_MAX_WAIT_INDEXER_STOP;
        } catch (Exception e) {
            _stop = true;
            throw new ArchteaException(e);
        }
    }

    public void startup() throws ArchteaException {
        _indexerResult = _pool.submit(new IndexerCaller());
        _optimizingResult = _pool.submit(new OptimizingFlagCaller());
    }

    public void shutdown() throws ArchteaException {
        try {
            synchronized (this) {
                _stop = true;
                if (_indexer != null) {
                    _indexer.stop();
                }
                this.notifyAll();
            }
            try {
                if (_indexerResult != null) {
                    _indexerResult.get(_maxWaitForStopThread, TimeUnit.MILLISECONDS);
                }
            } catch (ExecutionException e) {
                log.warn("Shutdown thread failure. ", e);
            } catch (TimeoutException e) {
                log.warn("Shutdown thread failure. ", e);
            }
            if (_optimizingResult != null) {
                _optimizingResult.cancel(true);
            }
        } catch (InterruptedException e) {
            if (log.isInfoEnabled()) log.info("Interrupted in shutdown()", e);
        } finally {
            _indexer = null;
            _indexerResult = null;
            _optimizingResult = null;
        }
    }

    public void restart() throws ArchteaException {
    }

    void sleepMillis(long millis) throws InterruptedException {
        long sec = millis / 1000;
        for (long i = 0; i < sec; i++) {
            if (_stop) break;
            Thread.sleep(1000);
        }
    }

    class IndexerCaller implements Callable<Object> {

        public Object call() throws Exception {
            if (log.isInfoEnabled()) log.info("Start indexer thread.");
            Calendar now = Calendar.getInstance();
            try {
                while (!_stop) {
                    ArchteaMainConfig mainConfig = DefaultConfigManager.getInstance().getMainConfig();
                    String schedule = mainConfig.getProperty(ArchteaPropertyFactory.PROP_KEY_INDEXER_SCHEDULE);
                    _maxWaitForStopThread = mainConfig.getLong(ArchteaPropertyFactory.PROP_KEY_MAX_WAIT_INDEXER_STOP, ArchteaPropertyFactory.DEFAULT_MAX_WAIT_INDEXER_STOP);
                    long indexerInterval = mainConfig.getLong(ArchteaPropertyFactory.PROP_KEY_INDEXER_INTERVAL, ArchteaPropertyFactory.DEFAULT_INDEXER_INTERVAL);
                    if (_stop) break;
                    _indexer = ArchteaIndexer.newInstance(mainConfig);
                    if (_optimizeFlag) {
                        _indexer.setOptimize(true);
                        _optimizeFlag = false;
                    }
                    _indexer.runIndexer();
                    Calendar currentTime = Calendar.getInstance();
                    now = currentTime.getTimeInMillis() > now.getTimeInMillis() ? currentTime : now;
                    if (schedule != null && schedule.length() > 0) {
                        NextTime nt = new NextTime(schedule);
                        Calendar next = nt.toNext(now);
                        if (log.isInfoEnabled()) log.info("Next indexer will start at " + toCalendarString(next));
                        sleepMillis(next.getTimeInMillis() - currentTime.getTimeInMillis());
                        now = Calendar.getInstance();
                        now.roll(Calendar.MINUTE, true);
                    } else {
                        sleepMillis(indexerInterval);
                    }
                }
            } catch (Exception e) {
                if (log.isErrorEnabled()) log.error("Exception happened in Indexer Thread", e);
            } finally {
                if (log.isInfoEnabled()) log.info("Stop indexer thread.");
            }
            return null;
        }
    }

    class OptimizingFlagCaller implements Callable<Object> {

        public Object call() throws Exception {
            if (log.isInfoEnabled()) log.info("Start optimize flag thread.");
            Calendar now = Calendar.getInstance();
            try {
                while (!_stop) {
                    ArchteaMainConfig mainConfig = DefaultConfigManager.getInstance().getMainConfig();
                    String schedule = mainConfig.getProperty(ArchteaPropertyFactory.PROP_KEY_OPTIMIZE_SCHEDULE);
                    if (schedule != null && schedule.length() > 0) {
                        NextTime nt = new NextTime(schedule);
                        Calendar next = nt.toNext(now);
                        now = Calendar.getInstance();
                        if (next.getTimeInMillis() > now.getTimeInMillis()) {
                            long sleepMillsecond = next.getTimeInMillis() - now.getTimeInMillis();
                            sleepMillis(sleepMillsecond);
                            if (!_optimizeFlag) {
                                _optimizeFlag = true;
                                if (log.isInfoEnabled()) log.info("Set optimize to true. A next indexer will run optimize(). + " + sleepMillsecond);
                            } else {
                                sleepMillis(2000);
                            }
                            now = Calendar.getInstance();
                        }
                        now.roll(Calendar.MINUTE, true);
                    } else {
                        sleepMillis(60 * 1000);
                    }
                }
            } catch (Exception e) {
                if (log.isErrorEnabled()) log.error("Exception happened in OptimizingFlagThread", e);
            } finally {
                if (log.isInfoEnabled()) log.info("Stop optimize flag thread.");
            }
            return null;
        }
    }

    private String toCalendarString(Calendar cal) {
        return new SimpleDateFormat("yyyy/MM/dd HH:mm EEE").format(cal.getTime());
    }
}
