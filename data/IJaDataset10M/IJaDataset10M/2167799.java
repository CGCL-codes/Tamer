package org.nakedobjects.nof.reflect.java.facets.properties.validate;

import org.nakedobjects.corelib.events.ValidityEvent;
import org.nakedobjects.noa.adapter.NakedObject;
import org.nakedobjects.noa.facets.FacetAbstract;
import org.nakedobjects.noa.facets.FacetHolder;
import org.nakedobjects.noa.facets.properties.validate.PropertyValidateFacet;
import org.nakedobjects.noa.interactions.ValidityContext;

/**
 * Non checking property validation facet that provides default behaviour for all properties.
 */
public class PropertyValidateFacetDefault extends FacetAbstract implements PropertyValidateFacet {

    public String invalidates(final ValidityContext<? extends ValidityEvent> ic) {
        return null;
    }

    public PropertyValidateFacetDefault(final FacetHolder holder) {
        super(PropertyValidateFacet.class, holder);
    }

    public String invalidReason(final NakedObject target, final NakedObject proposedValue) {
        return null;
    }
}
