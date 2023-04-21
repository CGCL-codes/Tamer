package org.simpleframework.http.session;

import java.util.Map;
import org.simpleframework.util.lease.Lease;

/**
 * The <code>Session</code> object is a simple leased container for
 * state within a web application. This is essentially a map of key
 * value pairs leased on a fixed duration to ensure it remains active
 * between we requests. If the session remains idle for sufficiently
 * long then it is disposed of by the <code>SessionProvider</code> 
 * so that resources occupied can be released.   
 * 
 * @author Niall Gallagher
 * 
 * @see org.simpleframework.util.lease.Lease
 */
public interface Session<T> extends Map {

    /**
   * This is used to acquire the <code>Lease</code> object to control
   * the session. The lease is responsible for maintaining this map
   * within the application. Once the lease expires the session will
   * be removed and its mapped values will be available for recovery.
   * 
   * @return this returns the lease used to manage this session
   */
    public Lease<T> getLease();
}
