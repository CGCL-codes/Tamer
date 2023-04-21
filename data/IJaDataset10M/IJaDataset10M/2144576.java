package org.icehockeymanager.ihm.clients.datacollector.collectors;

import org.icehockeymanager.ihm.game.scheduler.events.SchedulerEvent;

/**
 * The Class MemoryDataCollector.
 */
public class MemoryDataCollector extends BaseCollector {

    /** The total memory before. */
    private long totalMemoryBefore;

    /** The free memory before. */
    private long freeMemoryBefore;

    /** The total memory after. */
    private long totalMemoryAfter;

    /** The free memory after. */
    private long freeMemoryAfter;

    /**
   * Instantiates a new memory data collector.
   */
    public MemoryDataCollector() {
        super(SchedulerEvent.class, "memory.log");
    }

    @Override
    public void collectDataAfterEvent(SchedulerEvent event) {
        System.gc();
        System.runFinalization();
        totalMemoryAfter = Runtime.getRuntime().totalMemory();
        freeMemoryAfter = Runtime.getRuntime().freeMemory();
    }

    @Override
    public void collectDataBeforeEvent(SchedulerEvent event) {
        System.gc();
        System.runFinalization();
        totalMemoryBefore = Runtime.getRuntime().totalMemory();
        freeMemoryBefore = Runtime.getRuntime().freeMemory();
    }

    @Override
    public void dumpCollectedData(SchedulerEvent event) {
        String message = event.getMessageKey() + "," + totalMemoryBefore + "," + freeMemoryBefore + "," + totalMemoryAfter + "," + freeMemoryAfter;
        logger.info(message);
        collectorlogger.info(message);
    }
}
