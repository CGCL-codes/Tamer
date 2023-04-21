package de.lema.appender;

import org.apache.log4j.Logger;

public final class EventGenerator2 {

    private EventGenerator2() {
    }

    private static final Logger LOG = Logger.getLogger(EventGenerator2.class);

    public static void main(String[] args) {
        for (int i = 0; i < 200; i++) {
            LOG.info("Prog2," + i, new Exception("bla1"));
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < 100; i++) {
            LOG.info("Prog3," + i, new Exception("bla1"));
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
