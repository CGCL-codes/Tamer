package com.newsbeef.fetcher;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;
import org.springframework.scheduling.timer.DelegatingTimerTask;
import com.newsbeef.dao.FeedInfoDao;
import com.newsbeef.model.FeedInfo;

public class DbUrlFinder implements Runnable, UrlFinder {

    private BlockingQueue<FeedInfo> urlQueue;

    private boolean isRunning;

    private long dbCheckPeriod = 30 * 60 * 1000;

    private FeedInfoDao feedInfoDao;

    public DbUrlFinder(BlockingQueue<FeedInfo> urlQueue) {
        this.urlQueue = urlQueue;
    }

    public void setUrlQueue(BlockingQueue<FeedInfo> urlQueue) {
        this.urlQueue = urlQueue;
    }

    public void setDbCheckPeriod(long period) {
        this.dbCheckPeriod = period;
    }

    public void start() {
        long delay = 0;
        TimerTask task = new DelegatingTimerTask(this);
        Timer timer = new Timer("UrlFinder", true);
        timer.schedule(task, delay, dbCheckPeriod);
    }

    private synchronized boolean isRunning() {
        return isRunning;
    }

    private synchronized void setRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }

    public void setFeedInfoDao(FeedInfoDao dao) {
        this.feedInfoDao = dao;
    }

    public void run() {
        if (!isRunning()) {
            setRunning(true);
            try {
                List<FeedInfo> feedInfos = feedInfoDao.findSchedulableFeedInfos();
                urlQueue.addAll(feedInfos);
            } finally {
                setRunning(false);
            }
        }
    }
}
