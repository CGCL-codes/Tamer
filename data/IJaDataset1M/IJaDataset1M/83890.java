package info.olteanu.utils.watchdog;

import info.olteanu.interfaces.*;

public class WatchDog extends Thread {

    private Object value;

    private NotificationRecipient action;

    private int time;

    private boolean canceled = false;

    public WatchDog(int time, NotificationRecipient action, Object value) {
        super();
        this.time = time;
        this.action = action;
        this.value = value;
        setDaemon(true);
    }

    public void cancel() {
        canceled = true;
        interrupt();
    }

    public void run() {
        try {
            Thread.sleep(time);
            if (!canceled) action.notify(value);
        } catch (InterruptedException e) {
        }
    }
}
