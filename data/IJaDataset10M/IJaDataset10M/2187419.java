package com.continuent.tungsten.replicator.thl;

import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;
import org.apache.log4j.Logger;

/**
 * Implements a simple hash map to hold events. If the cache is full we age out
 * old items in FIFO order.
 * 
 * @author <a href="mailto:stephane.giron@continuent.com">Stephane Giron</a>
 * @version 1.0
 */
public class EventsCache {

    static Logger logger = Logger.getLogger(EventsCache.class);

    private int cacheSize = 0;

    private LinkedBlockingQueue<THLEvent> fifo;

    private HashMap<Long, THLEvent> cache;

    public EventsCache(int cacheSize) {
        this.cacheSize = cacheSize;
        if (cacheSize > 0) {
            logger.info("Allocating THL event cache; size=" + cacheSize);
            this.fifo = new LinkedBlockingQueue<THLEvent>(cacheSize);
            this.cache = new HashMap<Long, THLEvent>(cacheSize);
        }
    }

    public boolean isEmpty() {
        return (cacheSize <= 0 || cache.isEmpty());
    }

    /**
     * Add an event to the cache, clearing space if necessary.
     */
    public synchronized void put(THLEvent thlEvent) throws InterruptedException {
        if (cacheSize > 0) {
            while (cache.size() >= cacheSize) {
                THLEvent old = fifo.remove();
                cache.remove(old.getSeqno());
            }
            if (thlEvent.getFragno() == 0 && thlEvent.getLastFrag()) {
                fifo.put(thlEvent);
                cache.put(thlEvent.getSeqno(), thlEvent);
            }
        }
    }

    /**
     * Look up and return the cached item, if found.
     */
    public synchronized THLEvent get(long seqno) {
        if (cacheSize > 0) return cache.get(seqno); else return null;
    }
}
