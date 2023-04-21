package com.volantis.mcs.policies.impl.io.cache;

import com.volantis.mcs.policies.impl.io.JIBXTestAbstract;
import com.volantis.mcs.policies.InternalCacheControlBuilder;
import com.volantis.mcs.policies.impl.io.JIBXTestAbstract;

/**
 * Test cases ensure that properties for cache control builder round trip
 * properly if they are not set, set to the default value for the type, or
 * set to a non default value.
 *
 * <p>The reason these tests are needed is because the behaviour is different
 * whether a property is not set, or it is set to its default.</p>
 */
public class JIBXCacheControlTestCase extends JIBXTestAbstract {

    /**
     * Ensure that data round trips when none of the properties are set.
     */
    public void testNoneSet() throws Exception {
        checkBuilder(false, false, false, false, false, false, 0, false, 0, false, 0, false);
    }

    /**
     * Ensure that data round trips when cacheThisPolicy is set to the default
     * value.
     */
    public void testCacheThisPolicyDefault() throws Exception {
        checkBuilder(false, true, false, false, false, false, 0, false, 0, false, 0, false);
    }

    /**
     * Ensure that data round trips when cacheThisPolicy is set to a non
     * default value.
     */
    public void testCacheThisPolicyNonDefault() throws Exception {
        checkBuilder(true, true, false, false, false, false, 0, false, 0, false, 0, false);
    }

    /**
     * Ensure that data round trips when retainDuringRetry is set to the default
     * value.
     */
    public void testRetainDuringRetryDefault() throws Exception {
        checkBuilder(false, false, false, true, false, false, 0, false, 0, false, 0, false);
    }

    /**
     * Ensure that data round trips when retainDuringRetry is set to a non
     * default value.
     */
    public void testRetainDuringRetryNonDefault() throws Exception {
        checkBuilder(false, false, true, true, false, false, 0, false, 0, false, 0, false);
    }

    /**
     * Ensure that data round trips when retryFailedRetrieval is set to the default
     * value.
     */
    public void testRetryFailedRetrievalDefault() throws Exception {
        checkBuilder(false, false, false, false, false, true, 0, false, 0, false, 0, false);
    }

    /**
     * Ensure that data round trips when retryFailedRetrieval is set to a non
     * default value.
     */
    public void testRetryFailedRetrievalNonDefault() throws Exception {
        checkBuilder(false, false, false, false, true, true, 0, false, 0, false, 0, false);
    }

    /**
     * Ensure that data round trips when retryInterval is set to the default
     * value.
     */
    public void testRetryIntervalDefault() throws Exception {
        checkBuilder(false, false, false, false, false, false, 0, true, 0, false, 0, false);
    }

    /**
     * Ensure that data round trips when retryInterval is set to a non
     * default value.
     */
    public void testRetryIntervalNonDefault() throws Exception {
        checkBuilder(false, false, false, false, false, false, 10, true, 0, false, 0, false);
    }

    /**
     * Ensure that data round trips when retryMaxCount is set to the default
     * value.
     */
    public void testRetryMaxCountDefault() throws Exception {
        checkBuilder(false, false, false, false, false, false, 0, false, 0, true, 0, false);
    }

    /**
     * Ensure that data round trips when retryMaxCount is set to a non
     * default value.
     */
    public void testRetryMaxCountNonDefault() throws Exception {
        checkBuilder(false, false, false, false, false, false, 0, false, 3, true, 0, false);
    }

    /**
     * Ensure that data round trips when timeToLive is set to the default
     * value.
     */
    public void testTimeToLiveDefault() throws Exception {
        checkBuilder(false, false, false, false, false, false, 0, false, 0, false, 0, true);
    }

    /**
     * Ensure that data round trips when timeToLive is set to a non
     * default value.
     */
    public void testTimeToLiveNonDefault() throws Exception {
        checkBuilder(false, false, false, false, false, false, 0, false, 0, false, 99, true);
    }

    /**
     * Ensure that data round trips when all of the properties are set.
     */
    public void testAllSet() throws Exception {
        checkBuilder(true, true, true, true, false, true, 10, true, 5, true, 100, true);
    }

    private InternalCacheControlBuilder doRoundTripResource() throws Exception {
        String resource = getName() + ".xml";
        String sourceXML = RESOURCE_LOADER.getResourceAsString(resource);
        return (InternalCacheControlBuilder) doRoundTrip(sourceXML, null);
    }

    private void checkBuilder(final boolean expectedCacheThisPolicy, final boolean expectedCacheThisPolicyWasSet, final boolean expectedRetainDuringRetry, final boolean expectedRetainDuringRetryWasSet, final boolean expectedRetryFailedRetrieval, final boolean expectedRetryFailedRetrievalWasSet, final int expectedRetryInterval, final boolean expectedRetryIntervalWasSet, final int expectedRetryMaxCount, final boolean expectedRetryMaxCountWasSet, final int expectedTimeToLive, final boolean expectedTimeToLiveWasSet) throws Exception {
        InternalCacheControlBuilder builder = doRoundTripResource();
        assertEquals("cacheThisPolicy", expectedCacheThisPolicy, builder.getCacheThisPolicy());
        assertEquals("cacheThisPolicyWasSet", expectedCacheThisPolicyWasSet, builder.cacheThisPolicyWasSet());
        assertEquals("retainDuringRetry", expectedRetainDuringRetry, builder.getRetainDuringRetry());
        assertEquals("retainDuringRetryWasSet", expectedRetainDuringRetryWasSet, builder.retainDuringRetryWasSet());
        assertEquals("retryFailedRetrieval", expectedRetryFailedRetrieval, builder.getRetryFailedRetrieval());
        assertEquals("retryFailedRetrievalWasSet", expectedRetryFailedRetrievalWasSet, builder.retryFailedRetrievalWasSet());
        assertEquals("retryInterval", expectedRetryInterval, builder.getRetryInterval());
        assertEquals("retryIntervalWasSet", expectedRetryIntervalWasSet, builder.retryIntervalWasSet());
        assertEquals("retryMaxCount", expectedRetryMaxCount, builder.getRetryMaxCount());
        assertEquals("retryMaxCountWasSet", expectedRetryMaxCountWasSet, builder.retryMaxCountWasSet());
        assertEquals("timeToLive", expectedTimeToLive, builder.getTimeToLive());
        assertEquals("timeToLiveWasSet", expectedTimeToLiveWasSet, builder.timeToLiveWasSet());
    }
}
