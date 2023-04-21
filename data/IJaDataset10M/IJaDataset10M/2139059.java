package org.nakedobjects.metamodel.facets.properties.modify;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.java5.ImperativeFacet;
import org.nakedobjects.metamodel.util.NakedObjectInvokeUtils;

public class PropertyInitializationFacetViaSetterMethod extends PropertyInitializationFacetAbstract implements ImperativeFacet {

    private final Method method;

    public PropertyInitializationFacetViaSetterMethod(final Method method, final FacetHolder holder) {
        super(holder);
        this.method = method;
    }

    /**
     * Returns a singleton list of the {@link Method} provided in the constructor. 
     */
    public List<Method> getMethods() {
        return Collections.singletonList(method);
    }

    public boolean impliesResolve() {
        return false;
    }

    public boolean impliesObjectChanged() {
        return false;
    }

    public void initProperty(final NakedObject owningAdapter, final NakedObject initialAdapter) {
        NakedObjectInvokeUtils.invoke(method, owningAdapter, initialAdapter);
    }

    @Override
    protected String toStringValues() {
        return "method=" + method;
    }
}
