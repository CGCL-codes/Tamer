package org.nakedobjects.nof.reflect.java.facets.actions.choices;

import org.nakedobjects.noa.adapter.NakedObject;
import org.nakedobjects.noa.facets.FacetHolder;
import org.nakedobjects.noa.facets.actions.choices.ActionChoicesFacetAbstract;
import org.nakedobjects.noa.spec.NakedObjectSpecification;
import org.nakedobjects.nof.core.context.NakedObjectsContext;
import org.nakedobjects.nof.core.util.NakedObjectAdapterUtils;
import org.nakedobjects.nof.reflect.java.facets.ImperativeFacet;
import org.nakedobjects.nof.reflect.java.reflect.util.AdapterUtils;
import org.nakedobjects.nof.reflect.java.reflect.util.InvokeUtils;
import java.lang.reflect.Method;

public class ActionChoicesFacetViaMethod extends ActionChoicesFacetAbstract implements ImperativeFacet {

    private final Method choicesMethod;

    private final Class choicesType;

    public ActionChoicesFacetViaMethod(final Method choicesMethod, final Class choicesType, final FacetHolder holder) {
        super(holder);
        this.choicesMethod = choicesMethod;
        this.choicesType = choicesType;
    }

    public Method getMethod() {
        return choicesMethod;
    }

    public Object[][] getOptions(final NakedObject inObject) {
        final Object[] options = (Object[]) InvokeUtils.invoke(choicesMethod, inObject);
        final Object[][] results = new Object[options.length][];
        for (int i = 0; i < results.length; i++) {
            if (options[i] == null) {
                results[i] = null;
            } else if (options.getClass().isArray()) {
                results[i] = AdapterUtils.getObjectAsObjectArray(options[i]);
            } else {
                final NakedObjectSpecification specification = NakedObjectsContext.getReflector().loadSpecification(choicesType);
                results[i] = NakedObjectAdapterUtils.getCollectionAsObjectArray(options[i], specification);
            }
        }
        return results;
    }

    @Override
    protected String toStringValues() {
        return "method=" + choicesMethod + ",type=" + choicesType;
    }
}
