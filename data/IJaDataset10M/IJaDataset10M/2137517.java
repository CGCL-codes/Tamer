package com.googlecodes.tfhwiz.resourceutil;

import java.io.IOException;

/**
 * Resource Load utils
 * 
 *
 */
public interface ResourceLoader<R> {

    /**
     * Load resource and return resource value.
     * @return resource. if is not possible, return null.
     * @throws IOException 
     */
    public R load() throws IOException;

    /**
     * return resource timestampe.
     */
    public long getTimestamp();
}
