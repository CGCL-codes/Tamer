package it.newinstance.watchdog.engine;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 * @author Luigi R. Viggiano
 * @version $Revision: 105 $
 * @since 27-nov-2005
 */
public class DefaultServer implements Server {

    private static Logger log = Logger.getLogger(DefaultServer.class);

    private Context context;

    private Thread serverThread;

    private ThreadGroup threadGroup;

    private List<MonitorEntry> monitorEntries;

    private State status = State.OFF;

    private long killTimeoutMillis = 5000L;

    private static enum State {

        ON, OFF
    }

    private class MonitorEntry {

        private Monitor monitor;

        private Thread thread;

        private MonitorEntry(Monitor monitor) {
            if (monitor == null) throw new NullPointerException();
            this.monitor = monitor;
        }

        public Monitor getMonitor() {
            return monitor;
        }

        public Thread getThread() {
            if (thread == null) thread = new Thread(getThreadGroup(), monitor, "Monitor: " + monitor);
            return thread;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || !(obj instanceof MonitorEntry)) return false;
            MonitorEntry that = (MonitorEntry) obj;
            return this.monitor.equals(that.monitor);
        }

        @Override
        public int hashCode() {
            return monitor.hashCode();
        }
    }

    public DefaultServer() {
    }

    private ThreadGroup getThreadGroup() {
        synchronized (this) {
            if (threadGroup == null) {
                threadGroup = new ThreadGroup("Monitors-" + System.currentTimeMillis());
                threadGroup.setDaemon(true);
            }
            return threadGroup;
        }
    }

    public void register(Monitor monitor) {
        synchronized (this) {
            assertInactive();
            MonitorEntry entry = new MonitorEntry(monitor);
            if (monitorEntries == null) monitorEntries = new ArrayList<MonitorEntry>();
            monitorEntries.add(entry);
        }
    }

    public boolean isStarted() {
        synchronized (this) {
            return State.ON == status;
        }
    }

    public void setKillTimeoutMillis(long killTimeoutMillis) {
        this.killTimeoutMillis = killTimeoutMillis;
    }

    private void assertInactive() throws IllegalStateException {
        synchronized (this) {
            if (isStarted()) throw new IllegalStateException("Server is already started");
        }
    }

    public void unregister(Monitor monitor) {
        synchronized (this) {
            assertInactive();
            MonitorEntry entry = new MonitorEntry(monitor);
            if (monitorEntries != null) monitorEntries.remove(entry);
        }
    }

    public void start() {
        synchronized (this) {
            assertInactive();
            if (monitorEntries == null) {
                log.warn("no monitors");
                return;
            }
            serverThread = new Thread(new Runnable() {

                public void run() {
                    synchronized (DefaultServer.this) {
                        for (MonitorEntry entry : monitorEntries) {
                            try {
                                entry.getMonitor().init(getContext());
                                entry.getThread().start();
                            } catch (InitializationException e) {
                                log.error("monitor initialization error", e);
                                throw new RuntimeException(e);
                            }
                        }
                        status = State.ON;
                        try {
                            log.info("running");
                            DefaultServer.this.wait();
                            log.info("shutdown process initialized");
                            destroy();
                        } catch (InterruptedException e) {
                            interrupted("running", e);
                        }
                        status = State.OFF;
                        log.info("shutdown process completed");
                    }
                }
            }, "Server: " + this);
            serverThread.setDaemon(false);
            serverThread.start();
        }
    }

    protected Context getContext() throws InitializationException {
        if (context == null) {
            context = new BaseContext();
            context.init(this);
        }
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void stop() {
        synchronized (this) {
            if (!isStarted()) {
                log.warn("not running, cannot stop");
                return;
            }
            log.info("shutting down");
            notify();
        }
        try {
            serverThread.join();
            log.info("shutdown completed");
        } catch (InterruptedException e) {
            log.info("interrupted while shutting down", e);
            Thread.currentThread().interrupt();
        }
    }

    private void destroy() {
        synchronized (this) {
            for (MonitorEntry entry : monitorEntries) {
                Monitor monitor = entry.getMonitor();
                Thread thread = entry.getThread();
                if (thread.isAlive()) {
                    log.debug("interrupting thread " + monitor);
                    thread.interrupt();
                    try {
                        thread.join(killTimeoutMillis);
                        if (thread.isAlive()) {
                            log.warn("monitor " + monitor + " not responding to interrupt" + " request in " + killTimeoutMillis + " millis.");
                        } else {
                            if (log.isDebugEnabled()) log.debug("monitor " + monitor + " gracefully terminated.");
                        }
                    } catch (InterruptedException e) {
                        interrupted("destroying monitors", e);
                        break;
                    }
                } else {
                    log.debug("thread " + monitor + " already stopped");
                }
                monitor.destroy();
                if (log.isDebugEnabled()) log.debug(monitor + " destroyed");
            }
            try {
                getContext().destroy();
            } catch (WatchDogException e) {
                log.error("error during context destroy", e);
                throw new RuntimeException(e);
            }
        }
    }

    private void interrupted(String currentStatus, InterruptedException e) {
        log.error("interrupted while " + currentStatus, e);
        Thread.currentThread().interrupt();
    }
}
