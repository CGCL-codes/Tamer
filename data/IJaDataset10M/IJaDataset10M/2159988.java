package at.support;

import java.util.EventListener;
import java.util.TimerTask;

/**
 * A runnable timer task takes a runnable object as input and uses that
 * as the body for a scheduled timer task. This enables AmbientTalk/2 code
 * to symbiotically provide an AmbientTalk object implementing java.lang.Runnable
 * to serve as a timer task.
 * 
 * @author tvcutsem
 */
public class RunnableTimerTask extends TimerTask {

    public interface AsyncRunnable extends EventListener {

        public void run();
    }

    ;

    private final AsyncRunnable runnable_;

    public RunnableTimerTask(AsyncRunnable r) {
        runnable_ = r;
    }

    public void run() {
        runnable_.run();
    }
}
