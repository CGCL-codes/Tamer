package org.nakedobjects.viewer.nuthatch;

import org.nakedobjects.object.Naked;
import org.nakedobjects.object.NakedCollection;
import org.nakedobjects.object.NakedObject;
import org.nakedobjects.object.NakedObjectAction;
import org.nakedobjects.object.NakedObjectSpecification;
import org.nakedobjects.object.NakedObjects;
import org.nakedobjects.object.NakedValue;
import org.nakedobjects.object.TextEntryParseException;
import org.nakedobjects.object.control.Consent;

public class ActionContext implements Context {

    public static boolean create(NakedObjectAction[] actions, Command command, NakedObject object, ContextTree contextTree, View view) {
        ActionContext actionContext = create(actions, command, object, view);
        if (actionContext == null) {
            return false;
        } else {
            contextTree.addContext(actionContext);
            if (actionContext.isReady()) {
                actionContext.execute(contextTree, view);
            }
            return true;
        }
    }

    private static ActionContext create(NakedObjectAction[] actions, final Command command, NakedObject target, View view) {
        String toMatch = command.getParameterAsLowerCase(0);
        NakedObjectAction action = findAction(actions, command.getNumberOfParameters() - 1, toMatch);
        if (action == null || !action.isVisible(target)) {
            view.error("No such action");
        } else if (!action.isAuthorised()) {
            view.error("Not authorised to use this method");
        } else if (action.isAvailable(target).isVetoed()) {
            view.error("Action not available: " + action.isAvailable(target).getReason());
        } else {
            String parameters[];
            parameters = new String[action.getParameterCount()];
            if (command.getNumberOfParameters() >= 2) {
                for (int i = 0; i < command.getNumberOfParameters() - 1; i++) {
                    parameters[i] = command.getParameter(i + 1);
                }
            }
            NakedObjectSpecification[] parameterTypes = action.getParameterTypes();
            Object[] defaultParameterValues = action.getDefaultParameterValues(target);
            Naked[] params = new Naked[parameters.length];
            boolean parameterSet[] = new boolean[parameters.length];
            for (int i = 0; i < command.getNumberOfParameters() - 1; i++) {
                String param = parameters[i];
                if (param.equals("-")) {
                    params[i] = null;
                    parameterSet[i] = true;
                } else if (param.equals("!")) {
                    if (parameterTypes[i].isValue()) {
                        params[i] = NakedObjects.getObjectLoader().createAdapterForValue(defaultParameterValues[i]);
                    } else if (parameterTypes[i].isObject()) {
                        params[i] = NakedObjects.getObjectLoader().getAdapterForElseCreateAdapterForTransient(defaultParameterValues[i]);
                    }
                    parameterSet[i] = true;
                } else if (param.equals("?")) {
                    parameterSet[i] = false;
                } else {
                    NakedObjectSpecification type = parameterTypes[i];
                    params[i] = NakedObjects.getObjectLoader().createValueInstance(type);
                    ((NakedValue) params[i]).parseTextEntry(parameters[i]);
                    parameterSet[i] = true;
                }
            }
            return new ActionContext(action, target, params, parameterSet);
        }
        return null;
    }

    private static NakedObjectAction findAction(NakedObjectAction[] actions, int parametersLength, String name) {
        for (int i = 0; i < actions.length; i++) {
            NakedObjectAction action = actions[i];
            if (action.getName().toLowerCase().indexOf(name) >= 0) {
                if (action.getParameterCount() == parametersLength || parametersLength == 0) {
                    return action;
                }
            }
        }
        return null;
    }

    public static boolean methodExists(NakedObjectAction[] actions, final Command command) {
        String toMatch = command.getParameterAsLowerCase(0);
        int parameterLength = command.getNumberOfParameters() - 1;
        for (int i = 0; i < actions.length; i++) {
            NakedObjectAction action = actions[i];
            if (action.getName().toLowerCase().indexOf(toMatch) >= 0) {
                if (action.getParameterCount() == parameterLength || parameterLength == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    private final NakedObjectAction action;

    private final boolean[] isParameterSet;

    private int parameterIndex;

    private final Naked[] params;

    private final NakedObject target;

    private ActionContext(NakedObjectAction action, NakedObject target, Naked[] params, boolean[] isParameterSet) {
        this.params = params;
        this.target = target;
        this.action = action;
        this.isParameterSet = isParameterSet;
        findNextEmptyParameter();
    }

    public boolean canSetReferenceParameter(NakedObject object) {
        return object.getSpecification().isOfType(action.getParameterTypes()[parameterIndex]);
    }

    public boolean canSetValueParameter(String value) {
        NakedObjectSpecification type = action.getParameterTypes()[parameterIndex];
        NakedValue adapter = NakedObjects.getObjectLoader().createValueInstance(type);
        try {
            adapter.parseTextEntry(value);
        } catch (TextEntryParseException e) {
            return false;
        }
        return true;
    }

    public String debug() {
        String targetTitle = target == null ? "null" : target.titleString();
        return "Action '" + action.getName() + "' on " + targetTitle;
    }

    public void display(View view) {
        view.display(action.getName());
    }

    public void execute(ContextTree contextTree, View view) {
        Consent consent = action.isParameterSetValid(target, params);
        if (consent.isAllowed()) {
            contextTree.removeFrom(this);
            Naked result = action.execute(target, params);
            if (result != null) {
                if (result instanceof NakedObject) {
                    contextTree.addContext(new ObjectContext((NakedObject) result));
                } else if (result instanceof NakedCollection) {
                    contextTree.addContext(new CollectionContext((NakedCollection) result, "Collection"));
                }
            }
        } else {
            view.error("Can't invoke action: " + consent.getReason());
        }
    }

    private void findNextEmptyParameter() {
        for (int i = 0; i < isParameterSet.length; i++) {
            if (!isParameterSet[i]) {
                parameterIndex = i;
                break;
            }
        }
    }

    public Context getObject(String lowecaseTitle) {
        return null;
    }

    public String getPrompt() {
        if (isReady()) {
            return action.getName() + " (action)";
        } else {
            String label = action.getParameterLabels()[parameterIndex];
            Object object = action.getDefaultParameterValues(target)[parameterIndex];
            String defaultValue = object == null ? "" : object.toString();
            return "" + (parameterIndex + 1) + " - " + label + (defaultValue == null || defaultValue.length() == 0 ? "" : " (" + defaultValue + ")");
        }
    }

    public boolean isReady() {
        for (int i = 0; i < isParameterSet.length; i++) {
            if (!isParameterSet[i]) {
                return false;
            }
        }
        return true;
    }

    public void objects(View view) {
    }

    public void options(View view) {
        Object[] options = action.getOptions(target)[parameterIndex];
        for (int i = 0; options != null && i < options.length; i++) {
            view.display(options[i].toString());
        }
    }

    public void review(View view) {
        view.display(action.getName());
        for (int i = 0; i < params.length; i++) {
            String parameterNumber = (i == parameterIndex ? " *" : "  ") + i;
            String parameter = params[i] == null ? "null" : params[i].titleString() + " (" + params[i].getSpecification().getSingularName() + ")";
            String isSet = isParameterSet[i] ? "" : "not set";
            view.display(parameterNumber + ". " + parameter + " " + isSet);
        }
    }

    public void setReferenceParameter(NakedObject object) {
        params[parameterIndex] = object;
        isParameterSet[parameterIndex] = true;
        findNextEmptyParameter();
    }

    public void setToDefault() {
        Object object = action.getDefaultParameterValues(target)[parameterIndex];
        if (action.getParameterTypes()[parameterIndex].isValue()) {
            setValueParameter((String) object);
        } else {
            setReferenceParameter(NakedObjects.getObjectLoader().getAdapterFor(object));
        }
    }

    public void setValueParameter(String value) {
        NakedObjectSpecification type = action.getParameterTypes()[parameterIndex];
        params[parameterIndex] = NakedObjects.getObjectLoader().createValueInstance(type);
        ((NakedValue) params[parameterIndex]).parseTextEntry(value);
        isParameterSet[parameterIndex] = true;
        findNextEmptyParameter();
    }

    public void type(View view) {
        view.display(params[parameterIndex] == null ? "null" : params[parameterIndex].getSpecification().getSingularName());
    }

    public NakedObjectSpecification getParameterType() {
        return action.getParameterTypes()[parameterIndex];
    }
}
