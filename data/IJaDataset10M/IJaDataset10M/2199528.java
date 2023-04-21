package com.outbrain.pajamasproxy.memcached.proxy.future;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.util.Assert;
import com.outbrain.pajamasproxy.memcached.adapter.CacheElement;

public class GetFuture extends AbstractSpyFutureWrapper<Map<String, Object>, CacheElement[]> {

    private final List<String> keys;

    private final AtomicInteger getHits;

    private final AtomicInteger getMisses;

    public GetFuture(final Future<Map<String, Object>> future, final List<String> keys, final AtomicInteger getHits, final AtomicInteger getMisses) {
        super(future);
        Assert.notNull(keys, "keys may not be null");
        Assert.notNull(getHits, "getHits may not be null");
        Assert.notNull(getMisses, "getMisses may not be null");
        this.keys = keys;
        this.getHits = getHits;
        this.getMisses = getMisses;
    }

    @Override
    protected CacheElement[] convertSpyResponse(final Map<String, Object> spyResponse) {
        log.debug("spy get found keys={} for requested keys={}", spyResponse.keySet(), keys);
        final CacheElement[] response = new CacheElement[keys.size()];
        int i = 0;
        for (final String key : keys) {
            final CacheElement cacheElement = (CacheElement) spyResponse.get(key);
            if (null == cacheElement) {
                getMisses.incrementAndGet();
            } else {
                getHits.incrementAndGet();
            }
            response[i++] = cacheElement;
        }
        return response;
    }
}
