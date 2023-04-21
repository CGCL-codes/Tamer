package com.google.code.ssm.providers.xmemcached;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.exception.MemcachedException;
import net.rubyeye.xmemcached.transcoders.CachedData;
import net.rubyeye.xmemcached.transcoders.Transcoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.code.ssm.providers.AbstractMemcacheClientWrapper;
import com.google.code.ssm.providers.CacheException;
import com.google.code.ssm.providers.CacheTranscoder;
import com.google.code.ssm.providers.CachedObject;
import com.google.code.ssm.providers.CachedObjectImpl;

/**
 * 
 * @author Jakub Białek
 * @since 2.0.0
 * 
 */
class MemcacheClientWrapper extends AbstractMemcacheClientWrapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(MemcacheClientWrapper.class);

    private final Map<CacheTranscoder<?>, Object> adapters = new HashMap<CacheTranscoder<?>, Object>();

    private final MemcachedClient memcachedClient;

    MemcacheClientWrapper(final MemcachedClient memcachedClient) {
        this.memcachedClient = memcachedClient;
    }

    @Override
    public boolean add(final String key, final int exp, final Object value) throws TimeoutException, CacheException {
        try {
            return memcachedClient.add(key, exp, value);
        } catch (MemcachedException e) {
            throw new CacheException(e);
        } catch (InterruptedException e) {
            throw new CacheException(e);
        }
    }

    @Override
    public <T> boolean add(final String key, final int exp, final T value, final CacheTranscoder<T> transcoder) throws TimeoutException, CacheException {
        try {
            return memcachedClient.add(key, exp, value, getTranscoder(transcoder));
        } catch (MemcachedException e) {
            throw new CacheException(e);
        } catch (InterruptedException e) {
            throw new CacheException(e);
        }
    }

    @Override
    public long decr(final String key, final int by) throws TimeoutException, CacheException {
        try {
            return memcachedClient.decr(key, by);
        } catch (InterruptedException e) {
            throw new CacheException(e);
        } catch (MemcachedException e) {
            throw new CacheException(e);
        }
    }

    @Override
    public long decr(final String key, final int by, final long def) throws TimeoutException, CacheException {
        try {
            return memcachedClient.decr(key, by, def);
        } catch (InterruptedException e) {
            throw new CacheException(e);
        } catch (MemcachedException e) {
            throw new CacheException(e);
        }
    }

    @Override
    public boolean delete(final String key) throws TimeoutException, CacheException {
        try {
            return memcachedClient.delete(key);
        } catch (InterruptedException e) {
            throw new CacheException(e);
        } catch (MemcachedException e) {
            throw new CacheException(e);
        }
    }

    @Override
    public void flush() throws TimeoutException, CacheException {
        try {
            memcachedClient.flushAll();
        } catch (InterruptedException e) {
            throw new CacheException(e);
        } catch (MemcachedException e) {
            throw new CacheException(e);
        }
    }

    @Override
    public Object get(final String key) throws TimeoutException, CacheException {
        try {
            return memcachedClient.get(key);
        } catch (InterruptedException e) {
            throw new CacheException(e);
        } catch (MemcachedException e) {
            throw new CacheException(e);
        }
    }

    @Override
    public <T> T get(final String key, final CacheTranscoder<T> transcoder) throws TimeoutException, CacheException {
        try {
            return memcachedClient.get(key, getTranscoder(transcoder));
        } catch (InterruptedException e) {
            throw new CacheException(e);
        } catch (MemcachedException e) {
            throw new CacheException(e);
        }
    }

    @Override
    public <T> T get(final String key, final CacheTranscoder<T> transcoder, final long timeout) throws TimeoutException, CacheException {
        try {
            return memcachedClient.get(key, timeout, getTranscoder(transcoder));
        } catch (InterruptedException e) {
            throw new CacheException(e);
        } catch (MemcachedException e) {
            throw new CacheException(e);
        }
    }

    @Override
    public Collection<SocketAddress> getAvailableServers() {
        List<SocketAddress> servers = new ArrayList<SocketAddress>();
        Collection<InetSocketAddress> inetSocketAddresses = memcachedClient.getAvaliableServers();
        if (inetSocketAddresses != null && inetSocketAddresses.size() > 0) {
            servers.addAll(memcachedClient.getAvaliableServers());
        }
        return servers;
    }

    @Override
    public Map<String, Object> getBulk(final Collection<String> keys) throws TimeoutException, CacheException {
        Map<String, Object> result = null;
        try {
            result = memcachedClient.get(keys);
            return (result == null) ? Collections.<String, Object>emptyMap() : result;
        } catch (InterruptedException e) {
            throw new CacheException(e);
        } catch (MemcachedException e) {
            throw new CacheException(e);
        }
    }

    @Override
    public <T> Map<String, T> getBulk(final Collection<String> keys, final CacheTranscoder<T> transcoder) throws TimeoutException, CacheException {
        Map<String, T> result = null;
        try {
            result = memcachedClient.get(keys, getTranscoder(transcoder));
            return (result == null) ? Collections.<String, T>emptyMap() : result;
        } catch (InterruptedException e) {
            throw new CacheException(e);
        } catch (MemcachedException e) {
            throw new CacheException(e);
        }
    }

    @Override
    public long incr(final String key, final int by) throws TimeoutException, CacheException {
        try {
            return memcachedClient.incr(key, by);
        } catch (InterruptedException e) {
            throw new CacheException(e);
        } catch (MemcachedException e) {
            throw new CacheException(e);
        }
    }

    @Override
    public long incr(final String key, final int by, final long def) throws TimeoutException, CacheException {
        try {
            return memcachedClient.incr(key, by, def);
        } catch (InterruptedException e) {
            throw new CacheException(e);
        } catch (MemcachedException e) {
            throw new CacheException(e);
        }
    }

    @Override
    public long incr(final String key, final int by, final long def, final int expiration) throws TimeoutException, CacheException {
        try {
            return memcachedClient.incr(key, by, def, memcachedClient.getOpTimeout(), expiration);
        } catch (InterruptedException e) {
            throw new CacheException(e);
        } catch (MemcachedException e) {
            throw new CacheException(e);
        }
    }

    @Override
    public boolean set(final String key, final int exp, final Object value) throws TimeoutException, CacheException {
        try {
            return memcachedClient.set(key, exp, value);
        } catch (InterruptedException e) {
            throw new CacheException(e);
        } catch (MemcachedException e) {
            throw new CacheException(e);
        }
    }

    ;

    @Override
    public <T> boolean set(final String key, final int exp, final T value, final CacheTranscoder<T> transcoder) throws TimeoutException, CacheException {
        try {
            return memcachedClient.set(key, exp, value, getTranscoder(transcoder));
        } catch (InterruptedException e) {
            throw new CacheException(e);
        } catch (MemcachedException e) {
            throw new CacheException(e);
        }
    }

    @Override
    public void shutdown() {
        try {
            memcachedClient.shutdown();
        } catch (IOException e) {
            LOGGER.error("An error occurred when closing memcache", e);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public CacheTranscoder<?> getTranscoder() {
        return new TranscoderWrapper(memcachedClient.getTranscoder());
    }

    @SuppressWarnings("unchecked")
    private <T> Transcoder<T> getTranscoder(final CacheTranscoder<T> transcoder) {
        Transcoder<T> transcoderAdapter = (Transcoder<T>) adapters.get(transcoder);
        if (transcoderAdapter == null) {
            transcoderAdapter = new TranscoderAdapter<T>(transcoder);
            adapters.put(transcoder, transcoderAdapter);
        }
        return transcoderAdapter;
    }

    private static class TranscoderWrapper implements CacheTranscoder<Object> {

        private final Transcoder<Object> transcoder;

        public TranscoderWrapper(final Transcoder<Object> transcoder) {
            this.transcoder = transcoder;
        }

        @Override
        public Object decode(final CachedObject data) {
            return transcoder.decode(new CachedData(data.getFlags(), data.getData()));
        }

        @Override
        public CachedObject encode(final Object o) {
            CachedData cachedData = transcoder.encode(o);
            return new CachedObjectImpl(cachedData.getFlag(), cachedData.getData());
        }
    }
}
