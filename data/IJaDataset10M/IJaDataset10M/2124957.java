package org.nakedobjects.metamodel.java5;

import org.nakedobjects.metamodel.facets.FacetFactoryAbstract;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.MethodRemover;
import org.nakedobjects.metamodel.facets.MethodScope;
import org.nakedobjects.metamodel.spec.feature.NakedObjectFeatureType;

/**
 * Removes any calls to <tt>init</tt>.
 */
public class RemoveInitMethodFacetFactory extends FacetFactoryAbstract {

    public RemoveInitMethodFacetFactory() {
        super(NakedObjectFeatureType.OBJECTS_ONLY);
    }

    @Override
    public boolean process(final Class<?> type, final MethodRemover methodRemover, final FacetHolder holder) {
        methodRemover.removeMethod(MethodScope.OBJECT, "init", void.class, new Class[0]);
        return false;
    }
}
