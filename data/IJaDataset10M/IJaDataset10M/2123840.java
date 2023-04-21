package com.volantis.mcs.components;

import java.util.HashMap;
import java.util.Map;

public class RolloverImageComponentUtilities {

    /**
   * An array of the allowable values for the abstract cacheable repository
   * object cacheThisPolicy.
   */
    private static Object[] cacheThisPolicyArray;

    /**
   * An array of the allowable values for the abstract cacheable repository
   * object retainDuringRetry.
   */
    private static Object[] retainDuringRetryArray;

    /**
   * An array of the allowable values for the abstract cacheable repository
   * object retryFailedRetrieval.
   */
    private static Object[] retryFailedRetrievalArray;

    static {
        Object internal;
        String external;
        cacheThisPolicyArray = new Boolean[] { new Boolean("false"), new Boolean("true") };
        retainDuringRetryArray = new Boolean[] { new Boolean("false"), new Boolean("true") };
        retryFailedRetrievalArray = new Boolean[] { new Boolean("false"), new Boolean("true") };
    }

    public static Object[] getCacheThisPolicyArray() {
        return cacheThisPolicyArray;
    }

    public static Object[] getRetainDuringRetryArray() {
        return retainDuringRetryArray;
    }

    public static Object[] getRetryFailedRetrievalArray() {
        return retryFailedRetrievalArray;
    }
}
