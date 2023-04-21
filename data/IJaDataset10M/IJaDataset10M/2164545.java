package com.c4j.composition;

import static java.lang.String.format;
import static java.util.Collections.unmodifiableSet;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import com.c4j.linker.IAnchor;
import com.c4j.sre.C4JRuntimeException;
import com.c4j.type.IType;
import com.c4j.workspace.IFacetPort;
import com.c4j.workspace.IPort;

public class PublicFacetPort extends PublicPort implements IPublicFacetPort {

    private final CompositionIface.Neighborhood neighborhood;

    private final IAnchor<PublicFacetPort> anchor;

    private final Set<PublicFacetReference> references;

    PublicFacetPort(final CompositionIface.Neighborhood neighborhood, final Composition parent, final String name, final IType iface) {
        super(parent, iface);
        this.neighborhood = neighborhood;
        anchor = neighborhood.use_linker().createAnchor(this);
        anchor.setAnchorString(name);
        references = new HashSet<PublicFacetReference>();
    }

    @Override
    IAnchor<? extends PublicFacetPort> getAnchor() {
        return anchor;
    }

    @Override
    public Set<? extends IPublicPortReference> getPublicPortReferences() {
        return getPublicFacetReferences();
    }

    @Override
    public SortedSet<? extends IPublicPortReference> getSortedPublicPortReferences() {
        return getSortedPublicFacetReferences();
    }

    @Override
    public Set<? extends PublicFacetReference> getPublicFacetReferences() {
        return unmodifiableSet(references);
    }

    @Override
    public SortedSet<? extends PublicFacetReference> getSortedPublicFacetReferences() {
        final SortedSet<PublicFacetReference> result = new TreeSet<PublicFacetReference>(PortReference.COMPARATOR);
        result.addAll(references);
        return result;
    }

    @Override
    public boolean isReferencedBy(final IInstance instance, final IPort port) {
        for (final PublicFacetReference reference : references) if (reference.getInstance() == instance && reference.getPort() == port) return true;
        return false;
    }

    @Override
    public PublicFacetReference createPublicFacetReference(final String instanceref, final String portref, final int cardinality) {
        final PublicFacetReference result = new PublicFacetReference(neighborhood.use_linker(), this, instanceref, portref, cardinality);
        references.add(result);
        result.getInstanceReference().addListener(result);
        getRoot().getInstanceResolver().addReference(result.getInstanceReference());
        getRoot().fireAddedPublicPortReference(result);
        return result;
    }

    @Override
    public IPublicFacetReference createPublicFacetReference(final IInstance instance, final IFacetPort facet, final int cardinality) {
        if (cardinality < 1) throw new C4JRuntimeException("Can not create public facet port reference. Cardinality must be at least 1.");
        if (instance.getRoot() != getRoot()) throw new C4JRuntimeException("Can not create public facet port reference. Facet instance is not " + "contained in composition.");
        if (!instance.getFragmentReference().isResolved()) throw new C4JRuntimeException("Can not create public facet port reference. Facet instance can not be " + " resolved.");
        if (instance.getFragment() != facet.getRoot()) throw new C4JRuntimeException("Can not create public facet port reference. Facet does not belong to " + "fragment of facet instance.");
        return createPublicFacetReference(instance.getName(), facet.getName(), cardinality);
    }

    @Override
    public void removePublicPortReference(final IPublicPortReference reference) {
        if (reference instanceof IPublicFacetReference) removePublicFacetReference((IPublicFacetReference) reference); else throw new C4JRuntimeException(format("Unkown type of public port reference (type is ‘%s’)", reference.getClass()));
    }

    @Override
    public void removePublicFacetReference(final IPublicFacetReference facetReference) {
        final PublicFacetReference facetReference2 = (PublicFacetReference) facetReference;
        references.remove(facetReference2);
        getRoot().fireRemovedPublicPortReference(facetReference2);
        getRoot().getInstanceResolver().removeReference(facetReference2.getInstanceReference());
        facetReference2.getInstanceReference().removeListener(facetReference2);
    }

    @Override
    public int getCardinality() {
        int result = 0;
        for (final PublicFacetReference reference : references) {
            final IFacetPort port = reference.getPort();
            if (port != null) result += port.getCardinality() * reference.getCardinality();
        }
        return result;
    }
}
