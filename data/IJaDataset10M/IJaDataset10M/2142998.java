package com.volantis.synergetics.cache;

/**
 * Default implementation of the FutureResultFactory. This returns null.
 * This maintains deprecated the behaviour of the old cache (lock, get, put,
 * unlock).
 *
 * @deprecated Use {@link com.volantis.cache.Cache} instead.
 */
public class DefaultFutureResultFactory extends FutureResultFactory {

    /**
     * @see com.volantis.synergetics.cache.FutureResultFactory
     */
    public DefaultFutureResultFactory() {
        super();
    }

    /**
     * Return null.
     *
     * @param key        this pararmeter is ignored.
     * @param timeToLive this pararmeter is also ignored.
     * @return null.
     */
    protected ReadThroughFutureResult createCustomFutureResult(Object key, int timeToLive) {
        return null;
    }
}
