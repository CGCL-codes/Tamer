package com.netflexitysolutions.amazonws.sdb.orm.internal.operations;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.netflexitysolutions.amazonws.sdb.orm.SimpleDBSession;

/**
 * For concurrent SimpleDB access.
 * 
 * @author netflexity
 *
 * @param <T>
 */
public class ConcurrentFinder<T> {

    private static Log logger = LogFactory.getLog(FindAll.class.getName());

    private SimpleDBSession session;

    private Class<T> clazz;

    private List<String> ids;

    /**
	 * @param session
	 * @param clazz
	 * @param ids
	 */
    public ConcurrentFinder(SimpleDBSession session, Class<T> clazz, List<String> ids) {
        this.session = session;
        this.clazz = clazz;
        this.ids = ids;
    }

    /**
	 * @return
	 */
    public List<T> find() {
        List<T> foundItems = new ArrayList<T>();
        CompletionService<T> ecs = new ExecutorCompletionService<T>(session.getFactory().getQueryExecutor());
        int itemsToWaitFor = 0;
        for (String id : ids) {
            T cachedObject = session.cacheGet(clazz, id);
            if (cachedObject != null) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Found in cache: " + id);
                }
                foundItems.add(cachedObject);
            } else {
                itemsToWaitFor++;
                Callable<T> callable = new Find<T>(session, clazz, id);
                ecs.submit(callable);
            }
        }
        for (int i = 0; i < itemsToWaitFor; ++i) {
            T record;
            try {
                record = ecs.take().get();
                if (record != null) {
                    foundItems.add(record);
                    session.cachePut(record);
                }
            } catch (InterruptedException e) {
                if (logger.isWarnEnabled()) {
                    logger.warn(e);
                }
            } catch (ExecutionException e) {
                if (logger.isWarnEnabled()) {
                    logger.warn(e);
                }
            }
        }
        return foundItems;
    }
}
