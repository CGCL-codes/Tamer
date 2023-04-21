package pl.org.minions.stigma.globals;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Singleton class holding one global {@link Timer}. It is
 * mainly used to minimize {@link Timer} threads count.
 */
public final class GlobalTimer {

    private static Timer timer = new Timer("globalTimer", true);

    private static final int PURGING_PERIOD = 60 * 1000;

    private GlobalTimer() {
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                timer.purge();
            }
        }, PURGING_PERIOD, PURGING_PERIOD);
    }

    /**
     * Returns global {@link Timer} instance.
     * @return global {@link Timer} instance.
     */
    public static Timer getTimer() {
        return timer;
    }
}
