package org.voidness.oje2d;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author void
 */
public class Timer {

    public interface TimerCallback {

        void onTick(long elapsedTime);
    }

    private List<TimerCallback> callbacks;

    private long lastTime;

    private long currentTime;

    private long elapsedTime;

    public Timer() {
        callbacks = new ArrayList<TimerCallback>();
        lastTime = currentTime = Utils.getTime();
    }

    public void addCallback(TimerCallback timerCallback) {
        callbacks.add(timerCallback);
    }

    public void tick() {
        currentTime = Utils.getTime();
        elapsedTime = currentTime - lastTime;
        for (TimerCallback callback : callbacks) {
            callback.onTick(elapsedTime);
        }
        lastTime = currentTime;
    }
}
