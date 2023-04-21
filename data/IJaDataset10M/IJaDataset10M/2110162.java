package org.nakedobjects.noa.config;

/**
 * Allows components (eg facet factories) to indicate that they have a dependency on
 * {@link NakedObjectConfiguration}. 
 */
public interface NakedObjectConfigurationAware extends NakedObjectConfigurationProvider {

    /**
     * Inject the {@link NakedObjectConfiguration} into the component.
     */
    void setConfiguration(NakedObjectConfiguration configuration);
}
