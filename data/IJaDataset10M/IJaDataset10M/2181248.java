package com.volantis.mcs.runtime.policies.cache;

import com.volantis.mcs.policies.CacheControl;
import com.volantis.mcs.policies.CacheControlBuilder;
import com.volantis.mcs.policies.InternalCacheControlBuilder;
import com.volantis.mcs.policies.PolicyFactory;
import com.volantis.mcs.runtime.configuration.RemotePolicyCacheConfiguration;

/**
 * Encapsulates constrains (both limits and defaults) on {@link CacheControl}
 * properties.
 */
public class CacheControlConstraints {

    /**
     * The hard coded default cacheThisPolicy value.
     */
    private static final boolean DEFAULT_CACHE_THIS_POLICY = true;

    /**
     * The hard coded default retainDuringRetry value.
     */
    private static final boolean DEFAULT_RETAIN_DURING_RETRY = true;

    /**
     * The hard coded default retryFailedRetrieval value.
     */
    private static final boolean DEFAULT_RETRY_FAILED_RETRIEVAL = true;

    /**
     * The hard coded default retryInterval value.
     */
    private static final int DEFAULT_RETRY_INTERVAL = 600;

    /**
     * The hard coded default retryMaxCount value.
     */
    private static final int DEFAULT_RETRY_MAX_COUNT = 5;

    /**
     * The hard coded default timeToLive value.
     */
    private static final int DEFAULT_TIME_TO_LIVE = 3600;

    /**
     * The hard coded default allowCacheThisPolicy value.
     */
    private static final boolean DEFAULT_ALLOW_CACHE_THIS_POLICY = true;

    /**
     * The hard coded default maxTimeToLive value.
     */
    private static final int DEFAULT_MAX_TIME_TO_LIVE = Integer.MAX_VALUE;

    /**
     * The hard coded default allowRetryFailedRetrieval value.
     */
    private static final boolean DEFAULT_ALLOW_RETRY_FAILED_RETRIEVAL = true;

    /**
     * The hard coded default minRetryInterval value.
     */
    private static final int DEFAULT_MIN_RETRY_INTERVAL = 0;

    /**
     * The hard coded default allowRetainDuringRetry value.
     */
    private static final boolean DEFAULT_ALLOW_RETAIN_DURING_RETRY = true;

    /**
     * The hard coded default maxRetryMaxCount value.
     */
    private static final int DEFAULT_MAX_RETRY_MAX_COUNT = 10;

    /**
     * The hard coded default constraints.
     */
    private static final CacheControlConstraints HARD_CODED_CONSTRAINTS;

    static {
        HARD_CODED_CONSTRAINTS = new CacheControlConstraints();
    }

    /**
     * Get the hard coded default constraints.
     *
     * @return The hard coded default constraints.
     */
    public static final CacheControlConstraints getDefaultConstraints() {
        return HARD_CODED_CONSTRAINTS;
    }

    /**
     * Indicates whether policies are allowed to be cached.
     */
    private final boolean allowCacheThisPolicy;

    /**
     * The maximum time to live, a value of 0 means there is no limit.
     */
    private final int maxTimeToLive;

    /**
     * Indicates whether failed retrievals are allowed to be retried.
     */
    private final boolean allowRetryFailedRetrieval;

    /**
     * The minimum retry interval.
     */
    private final int minRetryInterval;

    /**
     * Indicates whether it is allowed for a policy to be retained during a
     * retry.
     */
    private final boolean allowRetainDuringRetry;

    /**
     * The macimum retry max count.
     */
    private final int maxRetryMaxCount;

    /**
     * Default value of {@link #allowCacheThisPolicy} for derived constraints.
     */
    private final boolean defaultAllowCacheThisPolicy;

    /**
     * Default value of {@link #maxTimeToLive} for derived constraints.
     */
    private final int defaultMaxTimeToLive;

    /**
     * Default value of {@link #allowRetryFailedRetrieval} for derived
     * constraints.
     */
    private final boolean defaultAllowRetryFailedRetrieval;

    /**
     * Default value of {@link #minRetryInterval} for derived constraints.
     */
    private final int defaultMinRetryInterval;

    /**
     * Default value of {@link #allowRetainDuringRetry} for derived constraints.
     */
    private final boolean defaultAllowRetainDuringRetry;

    /**
     * Default value of {@link #maxRetryMaxCount} for derived constraints.
     */
    private final int defaultMaxRetryMaxCount;

    /**
     * The default cache control.
     */
    private final CacheControl defaultCacheControl;

    /**
     * Initialise the defaults.
     */
    public CacheControlConstraints() {
        this.allowCacheThisPolicy = true;
        this.maxTimeToLive = Integer.MAX_VALUE;
        this.allowRetryFailedRetrieval = true;
        this.minRetryInterval = 0;
        this.allowRetainDuringRetry = true;
        this.maxRetryMaxCount = Integer.MAX_VALUE;
        this.defaultAllowCacheThisPolicy = DEFAULT_ALLOW_CACHE_THIS_POLICY;
        this.defaultMaxTimeToLive = DEFAULT_MAX_TIME_TO_LIVE;
        this.defaultAllowRetryFailedRetrieval = DEFAULT_ALLOW_RETRY_FAILED_RETRIEVAL;
        this.defaultMinRetryInterval = DEFAULT_MIN_RETRY_INTERVAL;
        this.defaultAllowRetainDuringRetry = DEFAULT_ALLOW_RETAIN_DURING_RETRY;
        this.defaultMaxRetryMaxCount = DEFAULT_MAX_RETRY_MAX_COUNT;
        PolicyFactory factory = PolicyFactory.getDefaultInstance();
        CacheControlBuilder builder = factory.createCacheControlBuilder();
        builder.setCacheThisPolicy(DEFAULT_CACHE_THIS_POLICY);
        builder.setRetainDuringRetry(DEFAULT_RETAIN_DURING_RETRY);
        builder.setRetryFailedRetrieval(DEFAULT_RETRY_FAILED_RETRIEVAL);
        builder.setRetryInterval(DEFAULT_RETRY_INTERVAL);
        builder.setRetryMaxCount(DEFAULT_RETRY_MAX_COUNT);
        builder.setTimeToLive(DEFAULT_TIME_TO_LIVE);
        applyConstraints(builder);
        this.defaultCacheControl = builder.getCacheControl();
    }

    /**
     * Initialise.
     *
     * @param configuration        The configuration.
     */
    public CacheControlConstraints(RemotePolicyCacheConfiguration configuration) {
        this(HARD_CODED_CONSTRAINTS, configuration);
    }

    /**
     * Initialise.
     *
     * @param base          The set of constraints from which this is derived.
     @param configuration The configuration.

     */
    public CacheControlConstraints(CacheControlConstraints base, RemotePolicyCacheConfiguration configuration) {
        allowCacheThisPolicy = base.constrainCacheThisPolicy(getBoolean(configuration.getAllowCacheThisPolicy(), base.defaultAllowCacheThisPolicy));
        if (allowCacheThisPolicy) {
            boolean allow = getBoolean(configuration.getAllowRetryFailedRetrieval(), base.defaultAllowRetryFailedRetrieval);
            allowRetryFailedRetrieval = base.constrainRetryFailedRetrieval(allow);
            int maxTimeToLive = getInteger(configuration.getMaxTimeToLive(), base.defaultMaxTimeToLive);
            if (maxTimeToLive == 0) {
                maxTimeToLive = Integer.MAX_VALUE;
            }
            this.maxTimeToLive = base.constrainTimeToLive(maxTimeToLive);
        } else {
            allowRetryFailedRetrieval = false;
            maxTimeToLive = 0;
        }
        if (allowRetryFailedRetrieval) {
            minRetryInterval = base.constrainRetryInterval(getInteger(configuration.getMinRetryInterval(), base.defaultMinRetryInterval));
            allowRetainDuringRetry = base.constrainRetainDuringRetry(getBoolean(configuration.getAllowRetainDuringRetry(), base.defaultAllowRetainDuringRetry));
            maxRetryMaxCount = base.constrainRetryMaxCount(getInteger(configuration.getMaxRetryMaxCount(), base.defaultMaxRetryMaxCount));
        } else {
            minRetryInterval = 0;
            allowRetainDuringRetry = false;
            maxRetryMaxCount = 0;
        }
        PolicyFactory factory = PolicyFactory.getDefaultInstance();
        CacheControl baseCacheControl = base.getDefaultCacheControl();
        CacheControlBuilder builder = factory.createCacheControlBuilder();
        builder.setCacheThisPolicy(getBoolean(configuration.getDefaultCacheThisPolicy(), baseCacheControl.getCacheThisPolicy()));
        builder.setRetainDuringRetry(getBoolean(configuration.getDefaultRetainDuringRetry(), baseCacheControl.getRetainDuringRetry()));
        builder.setRetryFailedRetrieval(getBoolean(configuration.getDefaultRetryFailedRetrieval(), baseCacheControl.getRetryFailedRetrieval()));
        builder.setRetryInterval(getInteger(configuration.getDefaultRetryInterval(), baseCacheControl.getRetryInterval()));
        builder.setRetryMaxCount(getInteger(configuration.getDefaultRetryMaxCount(), baseCacheControl.getRetryMaxCount()));
        builder.setTimeToLive(getInteger(configuration.getDefaultTimeToLive(), baseCacheControl.getTimeToLive()));
        applyConstraints(builder);
        this.defaultCacheControl = builder.getCacheControl();
        defaultAllowCacheThisPolicy = allowCacheThisPolicy;
        defaultAllowRetainDuringRetry = allowRetainDuringRetry;
        defaultAllowRetryFailedRetrieval = allowRetryFailedRetrieval;
        defaultMaxRetryMaxCount = maxRetryMaxCount;
        defaultMaxTimeToLive = maxTimeToLive;
        defaultMinRetryInterval = minRetryInterval;
    }

    private int constrainRetryMaxCount(int retryMaxCount) {
        return Math.min(maxRetryMaxCount, retryMaxCount);
    }

    private boolean constrainRetainDuringRetry(boolean retainDuringRetry) {
        return allowRetainDuringRetry && retainDuringRetry;
    }

    private int constrainRetryInterval(int retryInterval) {
        return Math.max(minRetryInterval, retryInterval);
    }

    private int constrainTimeToLive(int timeToLive) {
        return Math.min(maxTimeToLive, timeToLive);
    }

    private boolean constrainCacheThisPolicy(boolean cacheThisPolicy) {
        return allowCacheThisPolicy && cacheThisPolicy;
    }

    private boolean constrainRetryFailedRetrieval(boolean retryFailedRetrieval) {
        return allowRetryFailedRetrieval && retryFailedRetrieval;
    }

    /**
     * Get the int value of the supplied Integer, or the default value if the
     * Integer is null.
     *
     * @param integer      The Integer whose value is requested, may be null.
     * @param defaultValue The default value to use if the Integer is null.
     * @return The int value of the supplied Integer, or the default value.
     */
    private int getInteger(Integer integer, int defaultValue) {
        return integer == null ? defaultValue : integer.intValue();
    }

    /**
     * Get the boolean value of the supplied Boolean, or the default value if
     * the Boolean is null.
     *
     * @param bool         The Boolean whose value is requested, may be null.
     * @param defaultValue The default value to use if the Boolean is null.
     * @return The boolean value of the supplied Boolean, or the default value.
     */
    private boolean getBoolean(Boolean bool, boolean defaultValue) {
        return bool == null ? defaultValue : bool.booleanValue();
    }

    /**
     * Apply the constraints to the specified builder.
     *
     * <p>Modifies the values in the builder to ensure that they are within the
     * constraints indicated by this class. Any unset values are set to the
     * default values contained within.</p>
     *
     * @param builder The specified builder.
     */
    public void applyConstraints(CacheControlBuilder builder) {
        applyInternalLimitations((InternalCacheControlBuilder) builder);
    }

    /**
     * Internal method to actually apply the defaults.
     *
     * @param builder The builder to modify.
     */
    private void applyInternalLimitations(InternalCacheControlBuilder builder) {
        if (builder.cacheThisPolicyWasSet()) {
            builder.setCacheThisPolicy(constrainCacheThisPolicy(builder.getCacheThisPolicy()));
        } else {
            builder.setCacheThisPolicy(defaultCacheControl.getCacheThisPolicy());
        }
        if (!builder.getCacheThisPolicy()) {
            builder.clearRetainDuringRetry();
            builder.clearRetryFailedRetrieval();
            builder.clearRetryInterval();
            builder.clearRetryMaxCount();
            builder.clearTimeToLive();
            return;
        }
        if (builder.timeToLiveWasSet()) {
            builder.setTimeToLive(constrainTimeToLive(builder.getTimeToLive()));
        } else {
            builder.setTimeToLive(defaultCacheControl.getTimeToLive());
        }
        if (builder.retryFailedRetrievalWasSet()) {
            builder.setRetryFailedRetrieval(constrainRetryFailedRetrieval(builder.getRetryFailedRetrieval()));
        } else {
            builder.setRetryFailedRetrieval(defaultCacheControl.getRetryFailedRetrieval());
        }
        boolean retryFailedRetrieval = builder.getRetryFailedRetrieval();
        if (!retryFailedRetrieval) {
            builder.clearRetainDuringRetry();
            builder.clearRetryInterval();
            builder.clearRetryMaxCount();
            return;
        }
        if (builder.retainDuringRetryWasSet()) {
            builder.setRetainDuringRetry(constrainRetainDuringRetry(builder.getRetainDuringRetry()));
        } else {
            builder.setRetainDuringRetry(defaultCacheControl.getRetainDuringRetry());
        }
        if (builder.retryIntervalWasSet()) {
            builder.setRetryInterval(constrainRetryInterval(builder.getRetryInterval()));
        } else {
            builder.setRetryInterval(defaultCacheControl.getRetryInterval());
        }
        if (builder.retryMaxCountWasSet()) {
            builder.setRetryMaxCount(constrainRetryMaxCount(builder.getRetryMaxCount()));
        } else {
            builder.setRetryMaxCount(defaultCacheControl.getRetryMaxCount());
        }
    }

    /**
     * Get the default cache control.
     *
     * @return The cache control.
     */
    public CacheControl getDefaultCacheControl() {
        return defaultCacheControl;
    }
}
