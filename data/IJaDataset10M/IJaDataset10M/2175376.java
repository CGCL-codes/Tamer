package org.nakedobjects.plugins.dnd.view.action;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.consent.Consent;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAction;
import org.nakedobjects.metamodel.spec.feature.NakedObjectActionParameter;
import org.nakedobjects.metamodel.spec.feature.OneToOneActionParameter;
import org.nakedobjects.metamodel.spec.feature.ParseableEntryActionParameter;

public class ActionHelper {

    public static ActionHelper createInstance(final NakedObject target, final NakedObjectAction action) {
        final int numberParameters = action.getParameterCount();
        final NakedObject[] parameters = new NakedObject[numberParameters];
        final NakedObjectActionParameter[] parameterSpecs = action.getParameters();
        NakedObject[] defaultValues;
        NakedObject[][] options;
        defaultValues = new NakedObject[parameterSpecs.length];
        options = new NakedObject[parameterSpecs.length][];
        for (int i = 0; i < parameterSpecs.length; i++) {
            defaultValues[i] = parameterSpecs[i].getDefault(target);
            options[i] = parameterSpecs[i].getChoices(target);
        }
        if (!hasValues(defaultValues) && !hasValues(options)) {
            defaultValues = action.getDefaults(target);
            options = action.getChoices(target);
        }
        for (int i = 0; i < parameterSpecs.length; i++) {
            if (defaultValues[i] != null) {
                parameters[i] = defaultValues[i];
            } else {
                parameters[i] = null;
            }
        }
        return new ActionHelper(target, action, parameters, defaultValues, options);
    }

    private final NakedObjectAction action;

    private final NakedObject[] parameters;

    private final NakedObject target;

    private final NakedObject[][] options;

    private ActionHelper(final NakedObject target, final NakedObjectAction action, final NakedObject[] parameters, final NakedObject[] defaultValues, final NakedObject[][] options) {
        this.target = target;
        this.action = action;
        this.parameters = parameters;
        this.options = options;
    }

    public ParameterContent[] createParameters() {
        final ParameterContent[] parameterContents = new ParameterContent[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            final NakedObjectActionParameter[] parameters2 = action.getParameters();
            final NakedObject nakedObject = parameters[i];
            final NakedObjectSpecification specification = parameters2[i].getSpecification();
            if (specification.isParseable()) {
                final ParseableEntryActionParameter parseableEntryActionParameter = (ParseableEntryActionParameter) parameters2[i];
                parameterContents[i] = new TextParseableParameterImpl(parseableEntryActionParameter, nakedObject, options[i], i, this);
            } else {
                parameterContents[i] = new ObjectParameterImpl((OneToOneActionParameter) parameters2[i], nakedObject, options[i], i, this);
            }
        }
        return parameterContents;
    }

    public Consent disabled() {
        return action.isProposedArgumentSetValid(target, parameters);
    }

    public String getName() {
        return action.getName();
    }

    public String getDescription() {
        return action.getDescription();
    }

    public String getHelp() {
        return action.getHelp();
    }

    public NakedObject getParameter(final int index) {
        return parameters[index];
    }

    public NakedObject getTarget() {
        return action.realTarget(target);
    }

    public NakedObject invoke() {
        return action.execute(target, parameters);
    }

    public void setParameter(final int index, final NakedObject parameter) {
        this.parameters[index] = parameter;
    }

    public String title() {
        return getTarget().titleString();
    }

    public String getIconName() {
        return getTarget().getIconName();
    }

    private static boolean hasValues(NakedObject[] values) {
        if (values != null) {
            for (NakedObject nakedObject : values) {
                if (nakedObject != null) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean hasValues(NakedObject[][] values) {
        if (values != null) {
            for (NakedObject[] nakedObjectArray : values) {
                if (hasValues(nakedObjectArray)) {
                    return true;
                }
            }
        }
        return false;
    }
}
