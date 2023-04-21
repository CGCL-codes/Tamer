package org.apache.isis.extensions.jpa.metamodel.facets.prop.id;

import javax.persistence.Transient;
import org.apache.isis.metamodel.facets.FacetHolder;
import org.apache.isis.metamodel.facets.When;
import org.apache.isis.metamodel.facets.disable.DisabledFacetImpl;

/**
 * Derived from being {@link Transient}.
 */
public class DisabledFacetDerivedFromJpaIdAnnotation extends DisabledFacetImpl {

    public DisabledFacetDerivedFromJpaIdAnnotation(final FacetHolder holder) {
        super(When.ALWAYS, holder);
    }
}
