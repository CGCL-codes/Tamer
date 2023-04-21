package org.nakedobjects.metamodel.facets.collections.modify;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.nakedobjects.applib.DomainObjectContainer;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.java5.ImperativeFacet;
import org.nakedobjects.metamodel.runtimecontext.RuntimeContext;
import org.nakedobjects.metamodel.util.NakedObjectInvokeUtils;
import org.nakedobjects.metamodel.util.NakedObjectUtils;

public class CollectionRemoveFromFacetViaAccessor extends CollectionRemoveFromFacetAbstract implements ImperativeFacet {

    private final Method method;

    private final RuntimeContext runtimeContext;

    public CollectionRemoveFromFacetViaAccessor(final Method method, final FacetHolder holder, final RuntimeContext runtimeContext) {
        super(holder);
        this.method = method;
        this.runtimeContext = runtimeContext;
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

    /**
	 * Bytecode cannot automatically call {@link DomainObjectContainer#objectChanged(Object)}
	 * because cannot distinguish whether interacting with accessor to read it or to modify its contents.
	 */
    public boolean impliesObjectChanged() {
        return false;
    }

    public void remove(final NakedObject owningAdapter, final NakedObject elementAdapter) {
        final Collection collection = (Collection) NakedObjectInvokeUtils.invoke(method, owningAdapter);
        collection.remove(NakedObjectUtils.unwrap(elementAdapter));
        getRuntimeContext().objectChanged(owningAdapter);
    }

    @Override
    protected String toStringValues() {
        return "method=" + method;
    }

    private RuntimeContext getRuntimeContext() {
        return runtimeContext;
    }
}
