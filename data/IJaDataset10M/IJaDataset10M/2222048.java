package at.jku.semwiq.mediator.registry.monitor;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import at.jku.semwiq.mediator.registry.DataSourceRegistry;
import at.jku.semwiq.mediator.registry.model.DataSource;

/**
 * @author dorgon
 *
 */
public abstract class UpdateWorkerBase implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(UpdateWorkerBase.class);

    protected final DataSource ds;

    protected final DataSourceMonitorImpl monitor;

    protected final DataSourceRegistry registry;

    protected static final Set<DataSource> currentlyUpdating = new HashSet<DataSource>();

    public UpdateWorkerBase(DataSource ds, DataSourceMonitorImpl monitor, DataSourceRegistry reg) {
        this.ds = ds;
        this.monitor = monitor;
        this.registry = reg;
    }

    private void setUpdating(DataSource ds, boolean state) {
        if (state) currentlyUpdating.add(ds); else currentlyUpdating.remove(ds);
    }

    public static boolean isUpdating(DataSource ds) {
        return currentlyUpdating.contains(ds);
    }

    public void run() {
        boolean currentlyUpdating;
        synchronized (this) {
            currentlyUpdating = isUpdating(ds);
            if (!currentlyUpdating) setUpdating(ds, true);
        }
        if (!currentlyUpdating) doRun(); else log.warn("Attempt to start another update for " + ds + " but update is already running...");
    }

    /** safe run */
    private void doRun() {
        log.info("Updating " + ds + "...");
        try {
            doWork();
        } catch (Exception e) {
            log.error("Failed to run data source update.", e);
        } finally {
            monitor.notifyListenersAboutUpdate(ds);
            setUpdating(ds, false);
        }
    }

    /** must be implemented by workers, called by run() */
    public abstract void doWork();

    /**
	 * @param interval
	 */
    protected void scheduleNextUpdate(int interval) {
        Calendar next = Calendar.getInstance();
        next.add(Calendar.SECOND, interval);
        if (log.isDebugEnabled()) log.debug("Next update of " + ds + ": " + next.getTime());
    }

    /**
	 * @param ds
	 * @param b
	 */
    protected void setUnavailable(DataSource ds, boolean b) {
        ds.requestExclusiveWriteLock();
        try {
            ds.setUnavailable(b);
        } catch (Exception x) {
            log.error("Failed setting unavailability state for data source.", x);
        } finally {
            ds.returnExclusiveWriteLock();
        }
    }
}
