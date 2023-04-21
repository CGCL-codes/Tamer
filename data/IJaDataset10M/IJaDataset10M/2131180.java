package org.nakedobjects.nof.reflect.java.facets.object.callbacks;

import org.nakedobjects.noa.adapter.NakedObject;
import org.nakedobjects.noa.facets.FacetHolder;
import org.nakedobjects.noa.facets.object.callbacks.SavingCallbackFacetAbstract;
import org.nakedobjects.nof.reflect.java.facets.ImperativeFacet;
import org.nakedobjects.nof.reflect.java.reflect.util.InvokeUtils;
import java.lang.reflect.Method;

public class SavingCallbackFacetViaMethod extends SavingCallbackFacetAbstract implements ImperativeFacet {

    private final Method method;

    public SavingCallbackFacetViaMethod(final Method method, final FacetHolder holder) {
        super(holder);
        this.method = method;
    }

    public Method getMethod() {
        return method;
    }

    @Override
    public void invoke(final NakedObject object) {
        InvokeUtils.invoke(method, object);
    }

    @Override
    protected String toStringValues() {
        return "method=" + method;
    }
}
