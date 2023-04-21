package org.nakedobjects.webapp.view.value;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAction;
import org.nakedobjects.metamodel.spec.feature.NakedObjectActionParameter;
import org.nakedobjects.webapp.AbstractElementProcessor;
import org.nakedobjects.webapp.ScimpiException;
import org.nakedobjects.webapp.processor.Request;
import org.nakedobjects.webapp.util.MethodsUtils;

public class ParameterName extends AbstractElementProcessor {

    public void process(Request request) {
        String objectId = request.getOptionalProperty(OBJECT);
        String methodName = request.getRequiredProperty(METHOD);
        String field = request.getOptionalProperty(PARAMETER_NUMBER);
        NakedObject object = MethodsUtils.findObject(request.getContext(), objectId);
        NakedObjectAction action = MethodsUtils.findAction(object, methodName);
        NakedObjectActionParameter[] parameters = action.getParameters();
        int index;
        if (field == null) {
            index = 0;
        } else {
            index = Integer.valueOf(field).intValue() - 1;
        }
        if (index < 0 || index >= parameters.length) {
            throw new ScimpiException("Parameter numbers should be between 1 and " + parameters.length + ": " + index);
        }
        request.appendHtml(parameters[index].getName());
    }

    public String getName() {
        return "parameter-name";
    }
}
