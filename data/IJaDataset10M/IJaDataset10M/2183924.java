package org.nakedobjects.metamodel.facets.hide;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.java5.ImperativeFacet;
import org.nakedobjects.metamodel.util.NakedObjectInvokeUtils;

public class HideForContextFacetViaMethod extends HideForContextFacetAbstract implements ImperativeFacet {

    private final Method method;

    public HideForContextFacetViaMethod(final Method method, final FacetHolder holder) {
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
        return false;
    }

    public String hiddenReason(final NakedObject owningAdapter) {
        if (owningAdapter == null) {
            return null;
        }
        final Boolean isHidden = (Boolean) NakedObjectInvokeUtils.invoke(method, owningAdapter);
        return isHidden.booleanValue() ? "Hidden" : null;
    }

    @Override
    protected String toStringValues() {
        return "method=" + method;
    }
}
