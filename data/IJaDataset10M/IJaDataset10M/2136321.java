package com.jmt.game.core;

import java.util.Set;

public interface ISystemCache {

    public abstract void addEntry(String type, String name, Object entry);

    public abstract boolean exists(String type, String name);

    public abstract Object getEntry(String type, String name);

    public abstract Set<Object> getAll(String type);

    public abstract void removeEntry(String type, String name);

    public abstract void clear();

    public abstract void stop();

    public abstract Set<Object> getChanged();

    public abstract void addListener(ISystemCacheChangeListener listener);
}
