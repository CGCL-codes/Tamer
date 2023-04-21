package com.hazelcast.core;

import java.util.Map;

public interface MapEntry extends Map.Entry {

    long getCost();

    long getCreationTime();

    long getExpirationTime();

    int getHits();

    long getLastAccessTime();

    long getLastUpdateTime();

    long getVersion();

    boolean isValid();
}
