package com.volantis.mcs.components;

import com.volantis.mcs.objects.AbstractRepositoryObjectIdentity;
import com.volantis.mcs.project.Project;

/**
 * Encapsulates those properties of a DynamicVisualComponent which uniquely
 * identify it.
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 * @deprecated See {@link com.volantis.mcs.policies}.
 * This was deprecated in version 3.5.1.
 */
public class DynamicVisualComponentIdentity extends AbstractRepositoryObjectIdentity {

    /**
   * Create a new <code>DynamicVisualComponentIdentity</code>.
   * @param project  The project used with this object.   A null value is
   * possible if it
   * has not been set.
   * @param name  The name of the object.
   */
    public DynamicVisualComponentIdentity(Project project, String name) {
        super(project, name);
    }

    /**
   * Create a new <code>DynamicVisualComponentIdentity</code>.
   * @param name  The name of the object.
   */
    public DynamicVisualComponentIdentity(String name) {
        super(null, name);
    }

    public Class getObjectClass() {
        return DynamicVisualComponent.class;
    }
}
