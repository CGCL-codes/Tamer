package org.bing.zion.core;

import java.util.concurrent.atomic.AtomicLong;
import org.bing.engine.common.logging.Log;
import org.bing.engine.common.logging.LogFactory;

public class MulThreadDispatcher implements MessageDispatcher {

    protected static final Log logger = LogFactory.getLog(MulThreadDispatcher.class);

    private PerformerPool performerPool;

    private AtomicLong counter = new AtomicLong(0);

    public MulThreadDispatcher(int num) {
        performerPool = new PerformerPool(num);
    }

    public void dispatch(Session session, Object msg) {
        performerPool.perform(session, msg);
        long num = counter.incrementAndGet();
        if (num % 10000 == 0) {
            logger.info("Dispatch message " + num);
            logger.info("Performer queue " + performerPool.totalQueueSize());
        }
    }
}
