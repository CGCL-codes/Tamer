package org.openorb.orb.net;

/**
 * This is the interface for an object adapter manager.
 *
 * @author Unknown
 */
public interface AdapterManager extends org.omg.PortableServer.POAManager {

    /**
     * Set the maximum number of held requests.
     */
    void setMaxManagerHeldRequests(int max);
}
