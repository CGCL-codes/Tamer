package org.tctemplate;

import org.apache.log4j.Logger;
import org.apache.log4j.LogManager;

public class ProduceTask implements Runnable {

    private static final Logger logger = LogManager.getLogger(ProduceTask.class);

    private volatile boolean stop = false;

    private final Producer producer;

    public ProduceTask(Producer producer) {
        this.producer = producer;
    }

    public void stop() {
        stop = true;
    }

    public void run() {
        while (!stop) {
            try {
                producer.produce();
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                logger.error("interrupted", e);
            }
        }
    }
}
