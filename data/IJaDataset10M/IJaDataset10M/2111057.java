package org.nakedobjects.metamodel.facets.collections.validate;

import org.nakedobjects.applib.events.ValidityEvent;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.FacetAbstract;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.interactions.CollectionRemoveFromContext;
import org.nakedobjects.metamodel.interactions.ValidityContext;

public abstract class CollectionValidateRemoveFromFacetAbstract extends FacetAbstract implements CollectionValidateRemoveFromFacet {

    public static Class<? extends Facet> type() {
        return CollectionValidateRemoveFromFacet.class;
    }

    public CollectionValidateRemoveFromFacetAbstract(final FacetHolder holder) {
        super(type(), holder, false);
    }

    public String invalidates(final ValidityContext<? extends ValidityEvent> context) {
        if (!(context instanceof CollectionRemoveFromContext)) {
            return null;
        }
        final CollectionRemoveFromContext collectionRemoveFromContext = (CollectionRemoveFromContext) context;
        return invalidReason(context.getTarget(), collectionRemoveFromContext.getProposed());
    }
}
