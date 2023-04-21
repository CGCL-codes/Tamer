package com.googlecode.sarasvati.event;

import java.util.concurrent.ConcurrentHashMap;
import com.googlecode.sarasvati.util.SvUtil;

public class ListenerCache {

    protected ConcurrentHashMap<String, ExecutionListener> listenerCache = new ConcurrentHashMap<String, ExecutionListener>();

    public ExecutionListener getListener(final String type) {
        ExecutionListener listener = listenerCache.get(type);
        if (listener == null) {
            listener = (ExecutionListener) SvUtil.newInstanceOf(type, "ExecutionListener");
            listenerCache.put(type, listener);
        }
        return listener;
    }

    public ExecutionListener getListener(final Class<? extends ExecutionListener> listenerClass) {
        ExecutionListener listener = listenerCache.get(listenerClass.getName());
        if (listener == null) {
            listener = SvUtil.newInstanceOf(listenerClass, "ExecutionListener");
            listenerCache.put(listenerClass.getName(), listener);
        }
        return listener;
    }
}
