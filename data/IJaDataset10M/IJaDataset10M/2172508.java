package thinwire.render.web;

import java.util.*;
import java.util.logging.*;

/**
 * @author Joshua J. Gertzen
 */
class EventProcessorPool {

    private static final Logger log = Logger.getLogger(EventProcessorPool.class.getName());

    private static final Level LEVEL = Level.FINER;

    static final EventProcessorPool INSTANCE = new EventProcessorPool();

    private List<EventProcessor> pool = new LinkedList<EventProcessor>();

    private Map<WebApplication, EventProcessor> appToProcessor = new HashMap<WebApplication, EventProcessor>();

    private EventProcessorPool() {
    }

    EventProcessor getProcessor(WebApplication app) {
        synchronized (appToProcessor) {
            EventProcessor proc = appToProcessor.get(app);
            if (proc == null) {
                if (pool.size() > 0) {
                    proc = pool.remove(0);
                    if (log.isLoggable(LEVEL)) log.log(LEVEL, "Allocating " + proc.getName() + " from pool");
                } else {
                    proc = new EventProcessor(this);
                    proc.start();
                    if (log.isLoggable(LEVEL)) log.log(LEVEL, "New " + proc.getName() + " from pool");
                }
                proc.app = app;
                appToProcessor.put(app, proc);
            } else {
                if (log.isLoggable(LEVEL)) log.log(LEVEL, "Existing " + proc.getName() + " allocated to app");
            }
            return proc;
        }
    }

    void returnToPool(EventProcessor proc) {
        synchronized (appToProcessor) {
            if (proc.app == null) {
                if (log.isLoggable(LEVEL)) log.log(LEVEL, "No application tied to EventProcessor " + proc.getName() + ", probably removed from pool in processUserActionEvents finally block");
                return;
            }
            if (!proc.isInUse()) {
                if (log.isLoggable(LEVEL)) log.log(LEVEL, "Returning " + proc.getName() + " to pool");
                appToProcessor.remove(proc.app);
                proc.app = null;
                pool.add(proc);
            } else {
                if (log.isLoggable(LEVEL)) log.log(LEVEL, "Cannot return " + proc.getName() + " because thread is waiting in unreturnable call");
            }
        }
    }

    void removeFromPool(EventProcessor proc) {
        synchronized (appToProcessor) {
            if (log.isLoggable(LEVEL)) log.log(LEVEL, "Removing " + proc.getName() + " from pool - (proc.app == null) == " + (proc.app == null));
            if (proc.app != null) appToProcessor.remove(proc.app);
            proc.app = null;
            pool.remove(proc);
        }
    }
}
