package com.volantis.mcs.components;

import com.volantis.mcs.objects.AbstractCacheableRepositoryObject;
import com.volantis.mcs.objects.RepositoryObjectIdentity;

/**
 * The AbstractComponent class is the parent class for all components.
 *
 * THIS CLASS IS FOR INTERNAL USE ONLY.
 *
 * @deprecated See {@link com.volantis.mcs.policies}.
 *             This was deprecated in version 3.5.1.
 */
public abstract class AbstractComponent extends AbstractCacheableRepositoryObject {

    /**
   * The copyright statement.
   */
    private static String mark = "(c) Volantis Systems Ltd 2001.";

    /**
   * Create an unnamed component.
   * @volantis-api-exclude-from PublicAPI
   * @volantis-api-exclude-from ProfessionalServicesAPI
   */
    public AbstractComponent() {
    }

    /**
   * Create a component with the specified name.
   * @param name The name of the component.
   * @volantis-api-exclude-from PublicAPI
   * @volantis-api-exclude-from ProfessionalServicesAPI
   */
    public AbstractComponent(String name) {
        setName(name);
    }

    /**
   * Create a new <code>AbstractComponent</code>.
   * @param identity The identity to use.
   * @volantis-api-exclude-from PublicAPI
   * @volantis-api-exclude-from ProfessionalServicesAPI
   */
    public AbstractComponent(RepositoryObjectIdentity identity) {
        super(identity);
    }
}
