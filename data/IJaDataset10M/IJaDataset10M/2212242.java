package org.apache.http.conn;

import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.params.HttpParams;

/**
 * A factory for creating new {@link ClientConnectionManager} instances.
 * 
 *
 * @since 4.0
 */
public interface ClientConnectionManagerFactory {

    ClientConnectionManager newInstance(HttpParams params, SchemeRegistry schemeRegistry);
}
