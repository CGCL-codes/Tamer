package org.tamacat.util;

public interface LimitedCacheObject {

    boolean isCacheExpired(long expire);
}
