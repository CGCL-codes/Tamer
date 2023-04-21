package org.technbolts.domain.service.impl;

import static org.technbolts.domain.service.impl.QueryUtils.applyOrdering;
import static org.technbolts.domain.service.impl.QueryUtils.applyRange;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.technbolts.domain.service.Order;
import org.technbolts.util.Filter;
import org.technbolts.util.Range;

public class BasicRequester<T> {

    private static Log logger = LogFactory.getLog(BasicRequester.class);

    private Class<T> type;

    public BasicRequester(Class<T> type) {
        this.type = type;
    }

    private PersistenceManagerFactory factory;

    public BasicRequester<T> withFactory(PersistenceManagerFactory factory) {
        this.factory = factory;
        return this;
    }

    private Filter<T> filter;

    public BasicRequester<T> withFilter(Filter<T> filter) {
        this.filter = filter;
        return this;
    }

    private String orderField;

    public BasicRequester<T> withOrderField(String orderField) {
        this.orderField = orderField;
        return this;
    }

    private Range range;

    public BasicRequester<T> withRange(Range range) {
        this.range = range;
        return this;
    }

    private int fetchSize = 40;

    public BasicRequester<T> withFetchSize(int fetchSize) {
        this.fetchSize = fetchSize;
        return this;
    }

    private static <T> void notNull(T arg, String message) {
        if (arg == null) throw new IllegalStateException(message);
    }

    @SuppressWarnings("unchecked")
    public Collection<T> find() {
        notNull(orderField, "Missing order field");
        notNull(filter, "Missing filter");
        notNull(range, "Missing range");
        List<T> founds = new ArrayList<T>();
        PersistenceManager manager = factory.getPersistenceManager();
        try {
            int offset = 0;
            int cursor = 1;
            int pageSize = Math.min(fetchSize, range.getMax());
            boolean hasMore = true;
            boolean needMore = true;
            while (hasMore && needMore) {
                if (logger.isDebugEnabled()) logger.debug("Creating newQuery for paging, current cursor #" + cursor + " offset #" + offset + " pageSize #" + pageSize + " ");
                Query query = manager.newQuery(type);
                applyOrdering(query, Arrays.asList(new Order(orderField, true)));
                applyRange(query, new Range(cursor, cursor + pageSize));
                Collection<T> tfounds = (Collection<T>) query.execute();
                for (T t : tfounds) {
                    boolean match = filter.accept(t);
                    if (logger.isDebugEnabled()) logger.debug("Does project " + t + " match ? " + match);
                    if (match) {
                        offset++;
                        if (range.contains(offset)) founds.add(manager.detachCopy(t));
                        needMore = (offset <= range.getMax());
                        if (!needMore) break;
                    }
                }
                hasMore = tfounds.size() > pageSize;
                query.closeAll();
                cursor += pageSize;
            }
        } finally {
            if (manager != null) manager.close(); else logger.warn("Manager null in finally clause :'(");
        }
        return founds;
    }
}
