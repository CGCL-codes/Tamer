package org.perfmon4j.util;

import java.util.TimerTask;

public abstract class FailSafeTimerTask extends TimerTask {

    private static final Logger logger = LoggerFactory.initLogger(FailSafeTimerTask.class);

    public final void run() {
        try {
            failSafeRun();
        } catch (ThreadDeath td) {
            throw td;
        } catch (Throwable th) {
            logger.logError("Throwable caught in TimerTask", th);
        }
    }

    public abstract void failSafeRun();
}
