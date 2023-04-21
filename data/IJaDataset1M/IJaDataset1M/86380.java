package com.handjoys;

import com.handjoys.conf.ConfigReader;
import com.handjoys.conf.ConfigParam;
import com.handjoys.logger.FileLogger;

public class EventWriter extends Thread {

    private GameServer gs;

    private Thread workers[];

    public EventWriter(GameServer gs) {
        this.gs = gs;
    }

    public void initWriter() {
        int outQueueThreads = 1;
        workers = new Thread[outQueueThreads];
        for (int i = 0; i < outQueueThreads; i++) {
            workers[i] = new Thread(this, "eventwriter_" + i);
            workers[i].setDaemon(true);
            workers[i].start();
            FileLogger.info("EventWriter threads init, Create thread: " + workers[i]);
        }
    }

    public void run() {
        while (!gs.IS_SHUTTING_DOWN) {
            gs.writeOutgoingMessage();
        }
    }
}
