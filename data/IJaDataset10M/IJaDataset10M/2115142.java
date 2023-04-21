package net.assimilator.resources.servicecore;

import net.jini.core.lease.Lease;
import net.jini.id.Uuid;
import net.jini.id.UuidFactory;
import com.sun.jini.landlord.LeasedResource;

/**
 * Leased service resources to be used with <code>LeaseDurationPolicy</code> and/or
 * <code>LeaseManager</code>
 */
public class ServiceResource implements LeasedResource {

    protected Uuid cookie;

    protected long expiration;

    public Lease lease;

    public Object resource;

    /**
     * Create a ServiceResource
     * 
     * @param resource The resource being leased
     */
    public ServiceResource(Object resource) {
        this.resource = resource;
        synchronized (ServiceResource.class) {
            cookie = UuidFactory.generate();
        }
    }

    /**
     * Returns the expiration time of the leased resource.
     * 
     * @return The expiration time in milliseconds since the beginning of the epoch
     */
    public long getExpiration() {
        return (expiration);
    }

    /**
     * Changes the expiration time of the leased resource.
     * 
     * @param expiration The new expiration time in milliseconds since the beginning of 
     * the epoch
     */
    public void setExpiration(long expiration) {
        this.expiration = expiration;
    }

    /**
     * Returns a unique identifier that can be used by the grantor of
     * the resource to identify it
     */
    public Uuid getCookie() {
        return (cookie);
    }

    /**
     * Returns the resource that is being leased
     */
    public Object getResource() {
        return (resource);
    }

    /**
     * Overrides <code>equals()</code> to be based on the value of the cookie attribute
     */
    public boolean equals(Object o) {
        if (o instanceof ServiceResource) {
            ServiceResource sr = (ServiceResource) o;
            return (cookie.equals(sr.cookie));
        }
        return (false);
    }

    /**
     * Overrides <code>hashcode()</code> to be based on the hashcode of the 
     * cookie attribute
     */
    public int hashCode() {
        return (cookie.hashCode());
    }
}
