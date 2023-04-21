package org.apache.http.conn.params;

/**
 * Parameter names for HTTP client connections.
 * 
 * @since 4.0
 */
public interface ConnConnectionPNames {

    /**
     * Defines the maximum number of ignorable lines before we expect
     * a HTTP response's status line.
     * <p>
     * With HTTP/1.1 persistent connections, the problem arises that
     * broken scripts could return a wrong Content-Length
     * (there are more bytes sent than specified).
     * Unfortunately, in some cases, this cannot be detected after the
     * bad response, but only before the next one.
     * So HttpClient must be able to skip those surplus lines this way.
     * </p>
     * <p>
     * This parameter expects a value of type {@link Integer}.
     * 0 disallows all garbage/empty lines before the status line.
     * Use {@link java.lang.Integer#MAX_VALUE} for unlimited number.
     * </p>
     */
    public static final String MAX_STATUS_LINE_GARBAGE = "http.connection.max-status-line-garbage";
}
