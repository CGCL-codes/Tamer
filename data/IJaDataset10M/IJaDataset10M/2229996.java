package org.spark.util.cache;

public interface CacheItem {

    public long getLast();

    public Object getObject();

    public long refresh();

    public boolean expired();

    public boolean expired(long timout);
}
