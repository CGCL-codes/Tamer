package xjava;

public class Timer implements Runnable {

    private TimerInterface tI = null;

    private long interval = 0;

    private boolean tick = true;

    public Timer(TimerInterface ti, long interv) {
        tI = ti;
        interval = interv;
        start();
    }

    public void run() {
        long last = 0;
        while (tick) {
            try {
                Thread.sleep(interval);
            } catch (InterruptedException ex) {
            }
            if (tick) tI.tick();
        }
    }

    public void setInterval(long interval) {
        this.interval = interval;
    }

    public void start() {
        tick = true;
        new Thread(this).start();
    }

    public void stop() {
        tick = false;
    }
}
