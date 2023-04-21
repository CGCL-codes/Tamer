package jdc.util;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Timer class
 * author atadatah
 */
public class checkTime {

    Timer timer;

    String name = "";

    boolean notStopped = true;

    public checkTime(int seconds_delay, Object checker, String _name) {
        name = _name;
        timer = new Timer();
        timer.schedule(new checkTask(checker), seconds_delay * 1000);
    }

    public checkTime(int seconds_delay, Object checker) {
        timer = new Timer();
        timer.schedule(new checkTask(checker), seconds_delay * 1000);
    }

    public checkTime(int seconds_delay, int seconds_period, Object checker) {
        timer = new Timer();
        timer.schedule(new checkTaskRepeat(checker), seconds_delay * 1000, seconds_period * 1000);
    }

    public checkTime(int seconds_delay, int seconds_period, Object checker, String _name) {
        name = _name;
        timer = new Timer();
        timer.schedule(new checkTaskRepeat(checker), seconds_delay * 1000, seconds_period * 1000);
    }

    public String getName() {
        return (name.equals("")) ? "" : name;
    }

    public void dismiss() {
        notStopped = false;
    }

    private class checkTask extends TimerTask {

        private Object toset;

        public checkTask(Object _toset) {
            toset = _toset;
        }

        public void run() {
            if (notStopped) {
                ((base_action) toset).action("");
                timer.cancel();
            }
        }
    }

    private class checkTaskRepeat extends TimerTask {

        private Object toset;

        public checkTaskRepeat(Object _toset) {
            toset = _toset;
        }

        public void run() {
            if (notStopped) {
                ((base_action) toset).action(this);
            }
        }
    }
}
