package com.thimbleware.jmemcached;

import java.io.Serializable;

/**
 * Represents information about a cache entry.
 */
public final class MCElement implements Serializable {

    public int expire = 0;

    public String flags;

    public int data_length = 0;

    public byte[] data;

    public String keystring;

    public long cas_unique;

    public boolean blocked = false;

    public long blocked_until = 0;
}
