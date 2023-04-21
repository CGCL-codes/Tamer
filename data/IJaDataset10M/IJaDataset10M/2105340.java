package org.iqual.chaplin.objekty2009.semaphore2.v2;

import static org.iqual.chaplin.DynaCastUtils.$disjoin;
import static org.iqual.chaplin.DynaCastUtils.$join;
import org.iqual.chaplin.FromContext;

/**
 * @author Zbynek Slajchrt
* @since 29.10.2009 21:19:38
*/
public abstract class TrafficLightsImpl implements TrafficLights, Runnable {

    private Thread thread;

    private boolean stopped = true;

    private boolean outOfOrder;

    private final Object cyclicHandler;

    private final Object uniHandler;

    @FromContext
    private long interval = 1000;

    public TrafficLightsImpl(Object cyclicHandler, Object uniHandler) {
        this.cyclicHandler = cyclicHandler;
        this.uniHandler = uniHandler;
    }

    public synchronized void start() {
        if (!stopped) {
            return;
        }
        thread = new Thread(this);
        stopped = false;
        thread.start();
    }

    public synchronized void stop() {
        stopped = true;
        if (thread != null) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isRunning() {
        return !stopped;
    }

    @FromContext
    abstract void handleClick();

    public void run() {
        while (!stopped) {
            handleClick();
            try {
                Thread.sleep(interval);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void setInterval(long ms) {
        interval = ms;
    }

    public long getInterval() {
        return interval;
    }

    public void setOutOfOrderMode(boolean status) {
        outOfOrder = status;
        if (status) {
            $disjoin(cyclicHandler);
            $join(uniHandler, this);
        } else {
            $disjoin(uniHandler);
            $join(cyclicHandler, this);
        }
    }

    public boolean isOutOfOrder() {
        return outOfOrder;
    }
}
