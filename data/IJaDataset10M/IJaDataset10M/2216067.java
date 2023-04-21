package org.jboss.soa.esb.infinispan.gateways;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import org.infinispan.notifications.Listener;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryCreated;
import org.infinispan.notifications.cachelistener.event.CacheEntryCreatedEvent;
import org.jboss.logging.Logger;

/**
 * Infinispan cache listener
 * Receive cache added event and adds to internal queue for ESB to pickup
 *
 * @author <a href="mailto:noconnor@redhat.com">noconnor@redhat.com</a>
 */
@Listener(sync = false)
public class ESBCacheListener {

    protected static final Logger m_logger = Logger.getLogger(ESBCacheListener.class);

    private LinkedBlockingQueue<Object> myQueue;

    private boolean localOnlyFlag;

    public ESBCacheListener(LinkedBlockingQueue<Object> targetQueue, boolean localOnly) {
        myQueue = targetQueue;
    }

    @CacheEntryCreated
    public void EntryAdded(CacheEntryCreatedEvent event) {
        if (!event.isPre()) {
            if (localOnlyFlag) {
                if (!event.isOriginLocal()) return;
            }
            m_logger.debug(this.toString() + "  New entry " + event.getKey() + " created in the cache");
            try {
                myQueue.offer(event.getKey(), 10, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
