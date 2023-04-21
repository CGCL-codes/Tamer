package xbird.util.concurrent;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * <DIV lang="en"></DIV>
 * <DIV lang="ja"></DIV>
 * 
 * @author Makoto YUI (yuin405+xbird@gmail.com)
 */
public class DaemonThread extends Thread {

    private static final Log LOG = LogFactory.getLog(DaemonThread.class);

    private final Daemon task;

    private boolean running = false;

    private final Object pauseLock = new Object();

    private boolean paused = false;

    public DaemonThread(Daemon task) {
        super();
        if (task == null) {
            throw new IllegalArgumentException();
        }
        this.task = task;
        this.setDaemon(true);
    }

    public DaemonThread(String name, Daemon task) {
        super(name);
        if (task == null) {
            throw new IllegalArgumentException();
        }
        this.task = task;
        this.setDaemon(true);
    }

    public DaemonThread(RunnableDaemon target, String name) {
        super(target, name);
        if (target == null) {
            throw new IllegalArgumentException();
        }
        this.task = target;
        this.setDaemon(true);
    }

    public DaemonThread(RunnableDaemon target) {
        super(target);
        if (target == null) {
            throw new IllegalArgumentException();
        }
        this.task = target;
        this.setDaemon(true);
    }

    @Override
    public synchronized void start() {
        if (running) {
            throw new IllegalStateException("start called twice");
        }
        super.start();
        this.running = true;
    }

    @Override
    public void interrupt() {
        super.interrupt();
        this.running = false;
    }

    public boolean isRunning() {
        return running;
    }

    @Override
    public void run() {
        if (onStartup()) {
            runDaemon();
        }
        while (true) {
            try {
                synchronized (pauseLock) {
                    pauseLock.wait(interval());
                    this.paused = true;
                }
            } catch (InterruptedException e) {
                LOG.debug(Thread.currentThread().getName() + " was interrupted");
            }
            runDaemon();
        }
    }

    protected boolean onStartup() {
        return task.runOnStartup();
    }

    protected long interval() {
        return task.getInterval();
    }

    protected void runDaemon() {
        task.run();
    }

    public void wakeup() {
        if (paused) {
            synchronized (pauseLock) {
                pauseLock.notifyAll();
                this.paused = false;
            }
        }
    }
}
