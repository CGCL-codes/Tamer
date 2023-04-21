package com.volantis.mcs.policies.impl;

import com.volantis.mcs.model.validation.ValidationContext;
import com.volantis.mcs.policies.CacheControl;
import com.volantis.mcs.policies.InternalCacheControl;
import com.volantis.mcs.policies.InternalCacheControlBuilder;

public class CacheControlBuilderImpl extends AbstractBuilder implements InternalCacheControlBuilder {

    private CacheControl cacheControl;

    /**
     * Flag to determine if the component is to be cached
     */
    private Boolean cacheThisPolicy;

    /**
     * Flag to determine if the component is to be retained during retries
     */
    private Boolean retainDuringRetry;

    /**
     * Flag to determine if retries are attempted if retrieval fails
     */
    private Boolean retryFailedRetrieval;

    /**
     * Number of seconds between retreival attempts
     */
    private Integer retryInterval;

    /**
     * Number of retry attempts before failing
     */
    private Integer retryMaxCount;

    /**
     * Number of seconds to hold this component in the cache
     */
    private Integer timeToLive;

    public CacheControlBuilderImpl(InternalCacheControl cacheControl) {
        if (cacheControl != null) {
            this.cacheControl = cacheControl;
            cacheThisPolicy = getOptionalBoolean(cacheControl.cacheThisPolicyWasSet(), cacheControl.getCacheThisPolicy());
            retainDuringRetry = getOptionalBoolean(cacheControl.retainDuringRetryWasSet(), cacheControl.getRetainDuringRetry());
            retryFailedRetrieval = getOptionalBoolean(cacheControl.retryFailedRetrievalWasSet(), cacheControl.getRetryFailedRetrieval());
            retryInterval = getOptionalInteger(cacheControl.retryIntervalWasSet(), cacheControl.getRetryInterval());
            retryMaxCount = getOptionalInteger(cacheControl.retryMaxCountWasSet(), cacheControl.getRetryMaxCount());
            timeToLive = getOptionalInteger(cacheControl.timeToLiveWasSet(), cacheControl.getTimeToLive());
        }
    }

    private Integer getOptionalInteger(boolean flag, int value) {
        Integer result;
        if (flag) {
            result = getInteger(value);
        } else {
            result = null;
        }
        return result;
    }

    private Boolean getOptionalBoolean(boolean flag, boolean value) {
        Boolean result;
        if (flag) {
            result = getBoolean(value);
        } else {
            result = null;
        }
        return result;
    }

    public CacheControlBuilderImpl() {
        this(null);
    }

    public CacheControl getCacheControl() {
        if (cacheControl == null) {
            validate();
            cacheControl = new CacheControlImpl(this);
        }
        return cacheControl;
    }

    protected Object getBuiltObject() {
        return getCacheControl();
    }

    protected void clearBuiltObject() {
        cacheControl = null;
    }

    public boolean getCacheThisPolicy() {
        return getValueFromOptionalBoolean(cacheThisPolicy);
    }

    public void setCacheThisPolicy(boolean cacheThisPolicy) {
        Boolean b = getBoolean(cacheThisPolicy);
        if (!equals(this.cacheThisPolicy, b)) {
            stateChanged();
        }
        this.cacheThisPolicy = b;
    }

    public boolean getRetainDuringRetry() {
        return getValueFromOptionalBoolean(retainDuringRetry);
    }

    public void setRetainDuringRetry(boolean retainDuringRetry) {
        Boolean b = getBoolean(retainDuringRetry);
        if (!equals(this.retainDuringRetry, b)) {
            stateChanged();
        }
        this.retainDuringRetry = b;
    }

    public boolean getRetryFailedRetrieval() {
        return getValueFromOptionalBoolean(retryFailedRetrieval);
    }

    public void setRetryFailedRetrieval(boolean retryFailedRetrieval) {
        Boolean b = getBoolean(retryFailedRetrieval);
        if (!equals(this.retryFailedRetrieval, b)) {
            stateChanged();
        }
        this.retryFailedRetrieval = b;
    }

    public int getRetryInterval() {
        return getValue(retryInterval);
    }

    public void setRetryInterval(int retryInterval) {
        Integer i = getInteger(retryInterval);
        if (!equals(this.retryInterval, i)) {
            stateChanged();
        }
        this.retryInterval = i;
    }

    public int getRetryMaxCount() {
        return getValue(retryMaxCount);
    }

    public void setRetryMaxCount(int retryMaxCount) {
        Integer i = getInteger(retryMaxCount);
        if (!equals(this.retryMaxCount, i)) {
            stateChanged();
        }
        this.retryMaxCount = i;
    }

    public int getTimeToLive() {
        return getValue(timeToLive);
    }

    public void setTimeToLive(int timeToLive) {
        Integer i = getInteger(timeToLive);
        if (!equals(this.timeToLive, i)) {
            stateChanged();
        }
        this.timeToLive = i;
    }

    private boolean wasSet(Object object) {
        return object != null;
    }

    public boolean cacheThisPolicyWasSet() {
        return wasSet(cacheThisPolicy);
    }

    public boolean retainDuringRetryWasSet() {
        return wasSet(retainDuringRetry);
    }

    public boolean retryFailedRetrievalWasSet() {
        return wasSet(retryFailedRetrieval);
    }

    public boolean retryIntervalWasSet() {
        return wasSet(retryInterval);
    }

    public boolean retryMaxCountWasSet() {
        return wasSet(retryMaxCount);
    }

    public boolean timeToLiveWasSet() {
        return wasSet(timeToLive);
    }

    public void clearCacheThisPolicy() {
        cacheThisPolicy = null;
    }

    public void clearRetainDuringRetry() {
        retainDuringRetry = null;
    }

    public void clearRetryFailedRetrieval() {
        retryFailedRetrieval = null;
    }

    public void clearRetryInterval() {
        retryInterval = null;
    }

    public void clearRetryMaxCount() {
        retryMaxCount = null;
    }

    public void clearTimeToLive() {
        timeToLive = null;
    }

    public void validate(ValidationContext context) {
    }

    protected boolean castThenCheckEquality(Object obj) {
        return (obj instanceof CacheControlBuilderImpl) ? equalsSpecific((CacheControlBuilderImpl) obj) : false;
    }

    /**
     * @see #equalsSpecific(EqualsHashCodeBase)
     */
    public boolean equalsSpecific(CacheControlBuilderImpl other) {
        return super.equalsSpecific(other) && equals(cacheThisPolicy, other.cacheThisPolicy) && equals(retainDuringRetry, other.retainDuringRetry) && equals(retryFailedRetrieval, other.retryFailedRetrieval) && equals(retryInterval, other.retryInterval) && equals(retryMaxCount, other.retryMaxCount) && equals(timeToLive, other.timeToLive);
    }

    public int hashCode() {
        int result = super.hashCode();
        result = hashCode(result, cacheThisPolicy);
        result = hashCode(result, retainDuringRetry);
        result = hashCode(result, retryFailedRetrieval);
        result = hashCode(result, retryInterval);
        result = hashCode(result, retryMaxCount);
        result = hashCode(result, timeToLive);
        return result;
    }
}
