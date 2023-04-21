package org.thymeleaf.templateresolver;

/**
 * <p>
 *   Simple implementation of {@link ITemplateResolutionValidity}
 *   that considers the template resolution to be non-cacheable.
 * </p>
 * 
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.0
 *
 */
public class NonCacheableTemplateResolutionValidity implements ITemplateResolutionValidity {

    /**
     * <p>
     *   Singleton instance. Meant to avoid creating too many objects of this class.
     * </p>
     */
    public static NonCacheableTemplateResolutionValidity INSTANCE = new NonCacheableTemplateResolutionValidity();

    public NonCacheableTemplateResolutionValidity() {
        super();
    }

    /**
     * <p>
     *   Returns false. Template Resolutions using this validity are always 
     *   considered to be non-cacheable.
     * </p>
     * 
     * @return false
     */
    public boolean isCacheable() {
        return false;
    }

    /**
     * <p>
     *   This method will never be called, because templates using this
     *   validity implementation are always considered to be non-cacheable.
     * </p>
     * 
     * @return false
     */
    public boolean isCacheStillValid() {
        return false;
    }
}
