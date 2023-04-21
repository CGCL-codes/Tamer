package datadog.policy;

import datadog.exceptions.DatadogRuntimeException;

/**
 * @author tomichj
 */
public class PersistencePolicyFactory {

    public static final int ASSUME_EXISTS_POLICY = 1;

    public static final int ASSUME_NON_EXISTENCE_POLICY = 2;

    public static final int CHECK_CACHE_EXISTENCE_POLICY = 3;

    public static final int CHECK_CACHE_THEN_DB_EXISTENCE_POLICY = 4;

    public static PersistencePolicy create(int p) {
        if (p == ASSUME_EXISTS_POLICY) return new AssumeExistsPolicy();
        if (p == CHECK_CACHE_EXISTENCE_POLICY) return new CheckCachePolicy();
        if (p == ASSUME_NON_EXISTENCE_POLICY) {
            return new AssumeNonExistencePolicy();
        }
        if (p == CHECK_CACHE_THEN_DB_EXISTENCE_POLICY) {
            return new CheckCacheThenDbPolicy();
        }
        throw new DatadogRuntimeException("No such PersistencePolicy:" + p);
    }
}
