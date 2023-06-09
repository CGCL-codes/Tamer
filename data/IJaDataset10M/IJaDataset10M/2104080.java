package org.limewire.mojito.db;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.limewire.mojito.Context;
import org.limewire.mojito.routing.RouteTable;
import org.limewire.mojito.settings.DatabaseSettings;
import org.limewire.mojito.statistics.DatabaseStatisticContainer;

/**
 * Removes expired values from the local database.
 */
public class DatabaseCleaner implements Runnable {

    private static final Log LOG = LogFactory.getLog(DatabaseCleaner.class);

    private final Context context;

    private final DatabaseStatisticContainer databaseStats;

    private ScheduledFuture future;

    public DatabaseCleaner(Context context) {
        this.context = context;
        databaseStats = context.getDatabaseStats();
    }

    /**
     * Starts the <code>DatabaseCleaner</code>.
     */
    public synchronized void start() {
        if (future == null) {
            long delay = DatabaseSettings.DATABASE_CLEANER_PERIOD.getValue();
            long initialDelay = delay;
            future = context.getDHTExecutorService().scheduleWithFixedDelay(this, initialDelay, delay, TimeUnit.MILLISECONDS);
        }
    }

    /**
     * Stops the <code>DatabaseCleaner</code>.
     */
    public synchronized void stop() {
        if (future != null) {
            future.cancel(true);
            future = null;
        }
    }

    /**
     * Removes all expired <code>DHTValueEntity</code> from the <code>Database</code>.
     */
    private void cleanupDatabase() {
        EvictorManager evictorManager = context.getEvictorManager();
        RouteTable routeTable = context.getRouteTable();
        Database database = context.getDatabase();
        synchronized (database) {
            for (DHTValueEntity entity : database.values()) {
                if (evictorManager.isExpired(routeTable, entity)) {
                    if (LOG.isTraceEnabled()) {
                        LOG.trace(entity + " is expired!");
                    }
                    database.remove(entity.getPrimaryKey(), entity.getSecondaryKey());
                    databaseStats.EXPIRED_VALUES.incrementStat();
                }
            }
        }
    }

    public void run() {
        cleanupDatabase();
    }
}
