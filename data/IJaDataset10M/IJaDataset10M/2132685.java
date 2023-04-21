package org.streets.context;

/**
 * Filter interface for {@link org.apache.tapestry5.services.ApplicationInitializer}.
 *
 * @see org.apache.tapestry5.services.ApplicationInitializer
 */
public interface HiveInitializerFilter {

    /**
     * Peforms one step of initializing the application before passing off to the next step.
     */
    void initialize(HiveContext context, HiveInitializer initializer);
}
