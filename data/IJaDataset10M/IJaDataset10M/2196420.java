package org.thymeleaf.templateresolver;

/**
 * <p>
 *   Simple implementation of {@link ITemplateResolutionValidity}
 *   that uses a TTL (time-to-live) expressed in milliseconds to
 *   compute the validity of template cache entries.
 * </p>
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.0
 *
 */
public class TTLTemplateResolutionValidity implements ITemplateResolutionValidity {

    private final long cacheTTLMs;

    private final long creationTimeInMillis;

    /**
     * <p>
     *   Creates a new instance of this validity implementation. 
     * </p>
     * 
     * @param cacheTTLMs the TTL to be applied to the template resolution.
     */
    public TTLTemplateResolutionValidity(final long cacheTTLMs) {
        super();
        this.cacheTTLMs = cacheTTLMs;
        this.creationTimeInMillis = System.currentTimeMillis();
    }

    /**
     * <p>
     *   Returns the TTL in milliseconds to be applied to template
     *   validity.
     * </p>
     * 
     * @return the TTL in milliseconds
     */
    public long getCacheTTLMs() {
        return this.cacheTTLMs;
    }

    /**
     * <p>
     *   Returns true. Templates are always considered cacheable using this
     *   validity implementation.
     * </p>
     * 
     * @return true
     */
    public boolean isCacheable() {
        return true;
    }

    /**
     * <p>
     *   Returns whether the template resolution can still be considered valid. This
     *   is done by computing the difference in milliseconds between the moment when
     *   this object was created and the moment this method is called, and checking
     *   it is less than the established TTL (time-to-live).
     * </p>
     * 
     * @return whether the (cached) template resolution can still be considered valid.  
     */
    public boolean isCacheStillValid() {
        final long currentTimeInMillis = System.currentTimeMillis();
        return (currentTimeInMillis < this.creationTimeInMillis + this.cacheTTLMs);
    }
}
