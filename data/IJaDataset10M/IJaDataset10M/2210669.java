package net.jamcache;

import java.util.Timer;
import java.util.TimerTask;

class CacheTaskScheduler {

    private final Timer timer = new Timer();

    private final long interval;

    CacheTaskScheduler(long interval) {
        if (interval <= 0) throw new IllegalArgumentException("interval <= 0");
        this.interval = interval;
    }

    public void start() {
        timer.schedule(new TimerTask() {

            public void run() {
            }
        }, interval);
    }

    public void cancel() {
        timer.cancel();
    }
}
