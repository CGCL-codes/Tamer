package org.nakedobjects.viewer.skylark.basic;

import org.nakedobjects.NakedObjects;
import org.nakedobjects.object.Naked;
import org.nakedobjects.object.NakedObject;
import org.nakedobjects.object.NakedObjectSpecification;
import org.nakedobjects.object.control.Consent;
import org.nakedobjects.object.control.Hint;
import org.nakedobjects.object.reflect.Action;
import org.nakedobjects.object.reflect.ActionParameterSet;
import org.nakedobjects.viewer.skylark.ParameterContent;

public class ActionHelper {

    public static ActionHelper createInstance(NakedObject target, Action action) {
        int numberParameters = action.parameters().length;
        Naked[] parameters;
        parameters = new Naked[numberParameters];
        ActionParameterSet parameterHints = target.getParameters(action);
        Object[] defaultValues;
        String[] labels;
        boolean[] required;
        if (parameterHints != null) {
            labels = parameterHints.getParameterLabels();
            defaultValues = parameterHints.getDefaultParameterValues();
            required = parameterHints.getRequiredParameters();
        } else {
            labels = new String[numberParameters];
            defaultValues = new Naked[numberParameters];
            required = new boolean[numberParameters];
        }
        Naked[] parameterValues;
        Naked[] values;
        NakedObjectSpecification[] parameterTypes;
        parameterTypes = action.parameters();
        values = new Naked[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            if (parameterTypes[i].isValue()) {
                values[i] = NakedObjects.getObjectLoader().createValueInstance(parameterTypes[i]);
            } else {
                values[i] = null;
            }
        }
        parameterValues = new Naked[parameterTypes.length];
        for (int i = 0; i < parameterValues.length; i++) {
            parameterValues[i] = values[i];
        }
        for (int i = 0; i < numberParameters; i++) {
            NakedObjectSpecification type = parameterTypes[i];
            labels[i] = labels[i] == null ? type.getShortName() : labels[i];
            if (defaultValues[i] == null) {
                parameters[i] = parameterValues[i];
            } else {
                parameters[i] = NakedObjects.getObjectLoader().createAdapterForValue(defaultValues[i]);
                if (parameters[i] == null) {
                    parameters[i] = NakedObjects.getObjectLoader().getAdapterForElseCreateAdapterForTransient(defaultValues[i]);
                }
            }
        }
        return new ActionHelper(target, action, labels, parameters, parameterTypes, required);
    }

    private final Action action;

    private final String[] labels;

    private final Naked[] parameters;

    private final NakedObjectSpecification[] parameterTypes;

    private final NakedObject target;

    private final boolean[] required;

    protected ActionHelper(NakedObject target, Action action, String[] labels, Naked[] parameters, NakedObjectSpecification[] parameterTypes, boolean[] required) {
        this.target = target;
        this.action = action;
        this.labels = labels;
        this.parameters = parameters;
        this.parameterTypes = parameterTypes;
        this.required = required;
    }

    public ParameterContent[] createParameters() {
        ParameterContent[] parameterContents = new ParameterContent[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            if (parameterTypes[i].isValue()) {
                parameterContents[i] = new ValueParameter(labels[i], parameters[i], parameterTypes[i], required[i]);
            } else {
                parameterContents[i] = new ObjectParameter(labels[i], parameters[i], parameterTypes[i], required[i], i, this);
            }
        }
        return parameterContents;
    }

    public Consent disabled() {
        Hint about = target.getHint(action, parameters);
        return about.canUse();
    }

    public String getName() {
        return target.getLabel(action);
    }

    public Naked getParameter(int index) {
        return parameters[index];
    }

    public NakedObject getTarget() {
        return target;
    }

    public Naked invoke() {
        return target.execute(action, parameters);
    }

    public void setParameter(int index, Naked parameter) {
        this.parameters[index] = parameter;
    }
}
