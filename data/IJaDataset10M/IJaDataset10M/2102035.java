package org.nakedobjects.metamodel.specloader.internal.peer;

import org.nakedobjects.applib.Identifier;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.specloader.SpecificationLoader;

public class JavaOneToOneAssociationPeer extends JavaNakedObjectAssociationPeer implements NakedObjectAssociationPeer {

    public JavaOneToOneAssociationPeer(final Identifier identifier, final Class<?> returnType, SpecificationLoader specificationLoader) {
        super(identifier, returnType, false, specificationLoader);
    }

    @Override
    public <T extends Facet> T getFacet(final Class<T> facetType) {
        return super.getFacet(facetType);
    }

    @Override
    public String toString() {
        return "Reference Association [name=\"" + getIdentifier() + ", type=" + getSpecification() + " ]";
    }
}
