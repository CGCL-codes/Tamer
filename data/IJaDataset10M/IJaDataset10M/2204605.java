package org.nakedobjects.nof.reflect.java.facets.object.ident.icon;

import org.nakedobjects.noa.facets.FacetHolder;
import org.nakedobjects.noa.facets.FacetUtil;
import org.nakedobjects.noa.facets.MethodRemover;
import org.nakedobjects.noa.reflect.NakedObjectFeatureType;
import org.nakedobjects.nof.reflect.java.facets.MethodPrefixBasedFacetFactoryAbstract;
import java.lang.reflect.Method;

public class IconMethodFacetFactory extends MethodPrefixBasedFacetFactoryAbstract {

    private static final String ICON_NAME_PREFIX = "iconName";

    private static final String[] PREFIXES = { ICON_NAME_PREFIX };

    public IconMethodFacetFactory() {
        super(PREFIXES, NakedObjectFeatureType.OBJECTS_ONLY);
    }

    @Override
    public boolean process(final Class cls, final MethodRemover methodRemover, final FacetHolder facetHolder) {
        final Method method = findMethod(cls, OBJECT, ICON_NAME_PREFIX, String.class, NO_PARAMETERS_TYPES);
        if (method == null) {
            return false;
        } else {
            methodRemover.removeMethod(method);
            return FacetUtil.addFacet(new IconFacetViaMethod(method, facetHolder));
        }
    }
}
