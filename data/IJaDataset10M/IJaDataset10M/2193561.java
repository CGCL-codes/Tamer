package org.nakedobjects.viewer.web.task;

import org.nakedobjects.object.Naked;
import org.nakedobjects.object.NakedObject;
import org.nakedobjects.object.NakedObjectAction;
import org.nakedobjects.object.NakedObjectSpecification;
import org.nakedobjects.object.NakedObjects;
import org.nakedobjects.object.control.Consent;
import org.nakedobjects.viewer.web.component.DebugPane;
import org.nakedobjects.viewer.web.component.Page;
import org.nakedobjects.viewer.web.request.Request;
import org.nakedobjects.viewer.web.request.Task;

public class MethodTask extends Task {

    private NakedObjectAction action;

    protected MethodTask(final NakedObject target, final NakedObjectAction action) {
        super(action.getName(), action.getDescription(), target, action.getParameterCount());
        this.action = action;
        String[] names = action.getParameterLabels();
        NakedObjectSpecification[] types = action.getParameterTypes();
        Object[][] options = action.getOptions(target);
        Object[] defaultParameterValues = action.getDefaultParameterValues(target);
        boolean[] required = action.getRequiredParameters();
        for (int i = 0; i < names.length; i++) {
            this.names[i] = names[i];
            this.types[i] = types[i];
            this.options[i] = options[i];
            this.required[i] = required[i];
            if (defaultParameterValues[i] == null) {
                initialState[i] = null;
            } else if (types[i].isObject()) {
                initialState[i] = NakedObjects.getObjectLoader().getAdapterFor(defaultParameterValues[i]);
            } else if (types[i].isValue()) {
                initialState[i] = NakedObjects.getObjectLoader().createAdapterForValue(defaultParameterValues[i]);
            }
        }
    }

    public void checkForValidity(Request request) {
        NakedObject target = getTarget();
        Naked[] parameters = getParameters(request);
        Consent consent = action.isParameterSetValid(target, parameters);
        error = null;
        if (consent.isVetoed()) {
            error = consent.getReason();
        }
    }

    public Naked completeTask(Request request, Page page) {
        NakedObject target = getTarget();
        Naked[] parameters = getParameters(request);
        return action.execute(target, parameters);
    }

    public void debug(DebugPane debugPane) {
        debugPane.appendln("action: " + action);
        super.debug(debugPane);
    }
}
