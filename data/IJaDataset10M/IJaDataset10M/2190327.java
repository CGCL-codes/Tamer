package org.nakedobjects.noa.facets.object.validprops;

import org.nakedobjects.noa.facets.Facet;
import org.nakedobjects.noa.facets.propparam.validate.mandatory.MandatoryFacet;
import org.nakedobjects.noa.interactions.ObjectPersistContext;
import org.nakedobjects.noa.interactions.ValidatingInteractionAdvisor;

/**
 * Object-level {@link ValidatingInteractionAdvisor validator} that ensures that all {@link MandatoryFacet
 * mandatory} properties are entered prior to persisting the object.
 */
public interface ObjectValidPropertiesFacet extends Facet, ValidatingInteractionAdvisor {

    /**
     * The reason the object is invalid.
     * 
     * <p>
     * . If the object is actually valid, should return <tt>null</tt>.
     */
    public String invalidReason(ObjectPersistContext context);
}
