package org.nakedobjects.nof.reflect.java.facets.object.validprops;

import org.nakedobjects.noa.adapter.NakedObject;
import org.nakedobjects.noa.facets.FacetHolder;
import org.nakedobjects.noa.facets.object.validprops.ObjectValidPropertiesFacetAbstract;
import org.nakedobjects.noa.interactions.ObjectPersistContext;
import org.nakedobjects.noa.reflect.NakedObjectAssociation;
import org.nakedobjects.noa.reflect.NakedObjectAssociationFilters;
import org.nakedobjects.noa.reflect.OneToOneAssociation;

public class ObjectValidPropertiesFacetImpl extends ObjectValidPropertiesFacetAbstract {

    public ObjectValidPropertiesFacetImpl(final FacetHolder holder) {
        super(holder);
    }

    public String invalidReason(final ObjectPersistContext context) {
        final StringBuilder buf = new StringBuilder();
        final NakedObject nakedObject = context.getTarget();
        for (final NakedObjectAssociation property : nakedObject.getSpecification().getAssociations(NakedObjectAssociationFilters.PROPERTIES)) {
            if (property.isVisible(context.getSession(), nakedObject).isVetoed()) {
                continue;
            }
            if (property.isUsable(context.getSession(), nakedObject).isVetoed()) {
                continue;
            }
            final OneToOneAssociation otoa = (OneToOneAssociation) property;
            final NakedObject value = otoa.get(nakedObject);
            if (otoa.isAssociationValid(nakedObject, value).isVetoed()) {
                if (buf.length() > 0) {
                    buf.append(", ");
                }
                buf.append(property.getName());
            }
        }
        if (buf.length() > 0) {
            return "Invalid properties: " + buf.toString();
        }
        return null;
    }
}
