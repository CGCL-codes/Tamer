package org.nakedobjects.plugins.html.context;

import java.util.Enumeration;
import java.util.Vector;
import org.nakedobjects.commons.debug.DebugString;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.actcoll.typeof.TypeOfFacet;
import org.nakedobjects.metamodel.facets.collections.modify.CollectionFacet;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.util.CollectionFacetUtils;
import org.nakedobjects.runtime.context.NakedObjectsContext;
import org.nakedobjects.runtime.persistence.PersistenceSession;
import org.nakedobjects.runtime.persistence.adaptermanager.AdapterManager;

/**
 * Has value semantics based on the value semantics of the underlying list that it wraps.
 */
public class CollectionMapping {

    private final Vector list = new Vector();

    private final NakedObjectSpecification elementSpecification;

    public CollectionMapping(final Context context, final NakedObject collection) {
        final TypeOfFacet typeOfFacet = collection.getSpecification().getFacet(TypeOfFacet.class);
        elementSpecification = typeOfFacet.valueSpec();
        final CollectionFacet collectionFacet = CollectionFacetUtils.getCollectionFacetFromSpec(collection);
        final Enumeration elements = collectionFacet.elements(collection);
        while (elements.hasMoreElements()) {
            final NakedObject element = (NakedObject) elements.nextElement();
            list.add(context.mapObject(element));
        }
    }

    public NakedObject getCollection(final Context context) {
        final Vector elements = new Vector();
        final Enumeration e = list.elements();
        while (e.hasMoreElements()) {
            final String elementId = (String) e.nextElement();
            final NakedObject adapter = context.getMappedObject(elementId);
            elements.add(adapter.getObject());
        }
        return getAdapterManager().adapterFor(elements);
    }

    public NakedObjectSpecification getElementSpecification() {
        return elementSpecification;
    }

    public void debug(final DebugString debug) {
        debug.indent();
        final Enumeration e = list.elements();
        while (e.hasMoreElements()) {
            final String elementId = (String) e.nextElement();
            debug.appendln(elementId);
        }
        debug.unindent();
    }

    /**
     * Value semantics based on the identity of the underlying list that this
     * wraps. 
     */
    @Override
    public int hashCode() {
        return list.hashCode();
    }

    @Override
    public boolean equals(final Object other) {
        if (other == this) {
            return true;
        }
        if (other == null) {
            return false;
        }
        if (other.getClass() != this.getClass()) {
            return false;
        }
        return equals((CollectionMapping) other);
    }

    public boolean equals(final CollectionMapping other) {
        return this.list.equals(other.list);
    }

    public boolean contains(final String id) {
        final Enumeration e = list.elements();
        while (e.hasMoreElements()) {
            final String elementId = (String) e.nextElement();
            if (elementId.equals(id)) {
                return true;
            }
        }
        return false;
    }

    public Enumeration elements() {
        return list.elements();
    }

    public void remove(String existingId) {
        for (Object entry : list) {
            if (entry.equals(existingId)) {
                list.remove(existingId);
                break;
            }
        }
    }

    private static AdapterManager getAdapterManager() {
        return getPersistenceSession().getAdapterManager();
    }

    private static PersistenceSession getPersistenceSession() {
        return NakedObjectsContext.getPersistenceSession();
    }
}
