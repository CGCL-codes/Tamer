package org.moxy.util;

public class Ping extends Thread {

    private long pause;

    private Pingable target;

    public Ping(Pingable target, long pause) {
        this.target = target;
        this.pause = pause;
    }

    public void run() {
        while (true) {
            try {
                sleep(pause);
            } catch (InterruptedException e) {
            }
            target.ping();
        }
    }
}
