package org.apache.http.nio.params;

import org.apache.http.params.HttpParams;

/**
 * Utility class for accessing I/O reactor parameters in {@link HttpParams}.
 * 
 * 
 * @version $Revision: 744543 $
 * 
 * @since 4.0
 *
 * @see NIOReactorPNames
 */
public final class NIOReactorParams implements NIOReactorPNames {

    private NIOReactorParams() {
        super();
    }

    public static int getContentBufferSize(final HttpParams params) {
        if (params == null) {
            throw new IllegalArgumentException("HTTP parameters may not be null");
        }
        return params.getIntParameter(CONTENT_BUFFER_SIZE, 1024);
    }

    public static void setContentBufferSize(final HttpParams params, int size) {
        if (params == null) {
            throw new IllegalArgumentException("HTTP parameters may not be null");
        }
        params.setIntParameter(CONTENT_BUFFER_SIZE, size);
    }

    public static long getSelectInterval(final HttpParams params) {
        if (params == null) {
            throw new IllegalArgumentException("HTTP parameters may not be null");
        }
        return params.getLongParameter(SELECT_INTERVAL, 1000);
    }

    public static void setSelectInterval(final HttpParams params, long ms) {
        if (params == null) {
            throw new IllegalArgumentException("HTTP parameters may not be null");
        }
        params.setLongParameter(SELECT_INTERVAL, ms);
    }

    public static long getGracePeriod(final HttpParams params) {
        if (params == null) {
            throw new IllegalArgumentException("HTTP parameters may not be null");
        }
        return params.getLongParameter(GRACE_PERIOD, 500);
    }

    public static void setGracePeriod(final HttpParams params, long ms) {
        if (params == null) {
            throw new IllegalArgumentException("HTTP parameters may not be null");
        }
        params.setLongParameter(GRACE_PERIOD, ms);
    }
}
