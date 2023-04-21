package org.nakedobjects.metamodel.facets.collections.modify;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.java5.ImperativeFacet;
import org.nakedobjects.metamodel.util.NakedObjectInvokeUtils;

public class CollectionRemoveFromFacetViaMethod extends CollectionRemoveFromFacetAbstract implements ImperativeFacet {

    private final Method method;

    public CollectionRemoveFromFacetViaMethod(final Method method, final FacetHolder holder) {
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
        return true;
    }

    public boolean impliesObjectChanged() {
        return true;
    }

    public void remove(final NakedObject owningAdapter, final NakedObject elementAdapter) {
        NakedObjectInvokeUtils.invoke(method, owningAdapter, elementAdapter);
    }

    @Override
    protected String toStringValues() {
        return "method=" + method;
    }
}
