package org.nakedobjects.metamodel.facets.object.ident.icon;

import java.lang.reflect.Method;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.util.NakedObjectInvokeUtils;

public class IconFacetViaMethod extends IconFacetAbstract {

    private final Method method;

    public IconFacetViaMethod(final Method method, final FacetHolder holder) {
        super(holder);
        this.method = method;
    }

    public String iconName(final NakedObject owningAdapter) {
        try {
            return (String) NakedObjectInvokeUtils.invoke(method, owningAdapter);
        } catch (RuntimeException ex) {
            return null;
        }
    }
}
