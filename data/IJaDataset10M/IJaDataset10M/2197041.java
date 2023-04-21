package org.armedbear.j;

public final class GarbageCollectionTask extends IdleThreadTask {

    private long lastRunMillis;

    public GarbageCollectionTask() {
        setIdle(1000);
        setRunnable(runnable);
    }

    private final synchronized long getLastRunMillis() {
        return lastRunMillis;
    }

    private final synchronized void setLastRunMillis(long when) {
        lastRunMillis = when;
    }

    private final Runnable runnable = new Runnable() {

        public void run() {
            if (Dispatcher.getLastEventMillis() > getLastRunMillis()) {
                Runtime.getRuntime().gc();
                setLastRunMillis(System.currentTimeMillis());
            }
        }
    };
}
