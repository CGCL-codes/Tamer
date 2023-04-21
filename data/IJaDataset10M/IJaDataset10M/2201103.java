package com.langerra.server.channel.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Logger;
import javax.cache.Cache;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.langerra.server.channel.NamedCounter;
import com.langerra.shared.channel.Channel;
import com.langerra.shared.channel.ChannelMessage;

public class ChannelImpl<T extends Serializable> implements Channel<T> {

    static final Logger LOG = Logger.getLogger(ChannelImpl.class.getName());

    final Cache cache;

    final String IO_KEY;

    final String namespace;

    final boolean datastore;

    final NamedCounter rOffset, wOffset;

    public ChannelImpl(String namespace, Cache cache, NamedCounter rOffset, NamedCounter wOffset, boolean datastored) {
        this.datastore = datastored;
        this.namespace = namespace;
        this.IO_KEY = namespace + "IO-";
        this.rOffset = rOffset;
        this.wOffset = wOffset;
        this.cache = cache;
    }

    @SuppressWarnings("unchecked")
    long write(Object message, long key) {
        LOG.fine(key + " -> " + message);
        cache.put(IO_KEY + key, message);
        return key;
    }

    @SuppressWarnings("unchecked")
    T read(long key, boolean keep) {
        Object value = cache.get(IO_KEY + key);
        if (value == null) {
            LOG.info("(" + key + ") : cache failed");
        }
        while (value instanceof ChannelMessage) {
            value = ((ChannelMessage) value).getValue();
            LOG.fine("(" + key + ") -> " + value);
        }
        if (value != null) {
            if (!keep) {
                cache.remove(IO_KEY + key);
            }
            LOG.fine("(" + key + ") : " + value);
            return (T) value;
        }
        return null;
    }

    @Override
    public String getNamespace() {
        return namespace;
    }

    @Override
    public void async(AsyncCallback<T> callback) {
        throw new UnsupportedOperationException("Service InputChannel doesn't implement the async reader " + "because the server needs to create a thread to process the incoming messages.");
    }

    @Override
    public AsyncCallback<T> async() {
        return null;
    }

    @Override
    public long write(T message) {
        return write(message, wOffset.getAndIncrement());
    }

    @Override
    public void writeAll(Collection<? extends T> messages) {
        long first = wOffset.getAndAdd(messages.size());
        for (T m : messages) write(m, first++);
    }

    @Override
    public void write(ChannelMessage<T> message) {
        write(message, wOffset.getAndIncrement());
    }

    @Override
    public T read() {
        return read(rOffset.getAndIncrement(), false);
    }

    @Override
    public Collection<T> readAll() {
        return readAll(25 * 1000);
    }

    @Override
    public Collection<T> readAll(long timeout) {
        final long deadline = System.currentTimeMillis() + timeout;
        long first = rOffset.get();
        long last = wOffset.get();
        final ArrayList<T> values = new ArrayList<T>(Math.max(1, Math.min(1000, (int) (last - first))));
        for (int i = 0; first < last; i++) {
            final T value = read(first, false);
            if (value != null) values.add(value);
            if (first + 1 < last) {
                first = 1 + rOffset.getAndIncrement();
            } else {
                first++;
            }
            if (System.currentTimeMillis() >= deadline) break;
        }
        return values;
    }
}
