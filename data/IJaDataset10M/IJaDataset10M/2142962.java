package net.sf.warpcore.domperignon.api;

public class Timer extends Thread {

    private long sleepTime;

    private Timable target;

    public Timer(Timable target, long sleepTime) {
        this.target = target;
        this.sleepTime = sleepTime;
    }

    public void run() {
        while (true) {
            try {
                sleep(sleepTime);
            } catch (InterruptedException e) {
            }
            target.time();
        }
    }
}
