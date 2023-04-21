package org.nakedobjects.runtime.viewer;

import org.nakedobjects.commons.components.ApplicationScopedComponent;
import org.nakedobjects.metamodel.config.ConfigurationBuilderAware;
import org.nakedobjects.runtime.installers.InstallerLookupAware;
import org.nakedobjects.runtime.web.WebAppSpecification;

/**
 * Defines an mechanism for manipulating the domain objects.
 * 
 * <p>
 * The mechanism may be realized as a user interface (for example the DnD viewer or HTML viewer) but might
 * also be an abstract 'remoting' viewer of sockets or HTTP servlet requests.
 */
public interface NakedObjectsViewer extends ApplicationScopedComponent, InstallerLookupAware, ConfigurationBuilderAware {

    /**
     * Provide requirement for running a viewer from within an embedded web container.
     * 
     * <p>
     * Returns <tt>null</tt> if does not run within a web container.
     */
    WebAppSpecification getWebAppSpecification();
}
