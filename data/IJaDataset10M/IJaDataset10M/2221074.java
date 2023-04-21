package com.efsol.util;

import java.util.Date;

public class CachedMapMonitor extends Thread {

    private CachedMap map;

    private long interval;

    private boolean running = false;

    private long nextStop = 0;

    public CachedMapMonitor(CachedMap map, long interval) {
        this.map = map;
        this.interval = interval;
    }

    public void end() {
        running = false;
    }

    public void run() {
        running = true;
        nextStop = new Date().getTime();
        while (running) {
            nextStop += interval;
            long pause = nextStop - new Date().getTime();
            if (pause > 0) {
                try {
                    Thread.sleep(pause);
                } catch (InterruptedException e) {
                }
            }
            if (running) {
                map.refresh();
            }
        }
    }
}
