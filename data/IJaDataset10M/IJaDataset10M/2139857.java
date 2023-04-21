package org.nakedobjects.nof.reflect.transaction.facets.collections.write;

import org.nakedobjects.noa.adapter.NakedObject;
import org.nakedobjects.noa.facets.DecoratingFacet;
import org.nakedobjects.noa.facets.collections.modify.CollectionAddToFacet;
import org.nakedobjects.noa.facets.collections.modify.CollectionAddToFacetAbstract;
import org.nakedobjects.noa.persist.NakedObjectPersistor;
import org.nakedobjects.nof.core.context.NakedObjectsContext;
import org.nakedobjects.nof.reflect.transaction.TransactionUtils;

public class CollectionAddToFacetWrapTransaction extends CollectionAddToFacetAbstract implements DecoratingFacet<CollectionAddToFacet> {

    private final CollectionAddToFacet underlyingFacet;

    public CollectionAddToFacet getDecoratedFacet() {
        return underlyingFacet;
    }

    public CollectionAddToFacetWrapTransaction(final CollectionAddToFacet underlyingFacet) {
        super(underlyingFacet.getFacetHolder());
        this.underlyingFacet = underlyingFacet;
    }

    public void add(final NakedObject inObject, final NakedObject value) {
        final NakedObjectPersistor objectManager = NakedObjectsContext.getObjectPersistor();
        if (TransactionUtils.isTransient(inObject)) {
            underlyingFacet.add(inObject, value);
            return;
        }
        try {
            objectManager.startTransaction();
            underlyingFacet.add(inObject, value);
            objectManager.saveChanges();
            objectManager.endTransaction();
        } catch (final RuntimeException e) {
            TransactionUtils.abort(objectManager, getFacetHolder());
            throw e;
        }
    }

    @Override
    public String toString() {
        return super.toString() + " --> " + underlyingFacet.toString();
    }
}
