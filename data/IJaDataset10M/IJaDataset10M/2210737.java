package org.springframework.webflow;

import org.springframework.binding.method.MethodSignature;
import org.springframework.core.style.ToStringCreator;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * An action proxy/decorator that stores arbitrary properties about a target
 * <code>Action</code> implementation for use within a specific Action
 * execution context, for example an <code>ActionState</code> definition, a
 * <code>TransitionCriteria</code> definition, or in a test environment.
 * <p>
 * An annotated action is an action that wraps another action (referred to as
 * the <i>target action), setting up the target action's execution properties
 * before invoking {@link Action#execute}
 * 
 * @author Keith Donald
 */
public class AnnotatedAction extends AnnotatedObject implements Action {

    /**
	 * The action name property ("name").
	 * <p>
	 * The name property is often used as a qualifier for an action's result
	 * event, and is typically used to allow the flow to respond to a specific
	 * action's outcome within a larger action execution chain.
	 * <p>
	 * @see ActionState
	 */
    public static final String NAME_PROPERTY = "name";

    /**
	 * The action execution method property ("method").
	 * <p>
	 * The method property is a hint about what method should be invoked; e.g
	 * the name of a specific method on a
	 * <code>{@link org.springframework.webflow.action.MultiAction}</code> to
	 * execute or the name of a specific method on a arbitrary POJO (plain old
	 * java.lang.Object).
	 * <p>
	 * @see ActionState
	 */
    public static final String METHOD_PROPERTY = "method";

    /**
	 * The action execution method result attribute property ("resultName");
	 */
    public static final String RESULT_NAME_PROPERTY = "resultName";

    /**
	 * The action execution method result attribute scope property
	 * ("resultScope");
	 */
    public static final String RESULT_SCOPE_PROPERTY = "resultScope";

    /**
	 * The target action to execute.
	 */
    private Action targetAction;

    /**
	 * Creates a new annotated action for bean style usage.
	 * @see #setTargetAction(Action)
	 * @see #setCaption(String)
	 * @see #setDescription(String)
	 * @see #setName(String)
	 * @see #setMethod(MethodSignature)
	 * @see #setResultName(String)
	 * @see #setResultScope(ScopeType)
	 */
    public AnnotatedAction() {
    }

    /**
	 * Creates a new annotated action object for the specified action. No
	 * contextual properties are provided.
	 * @param targetAction the action
	 */
    public AnnotatedAction(Action targetAction) {
        setTargetAction(targetAction);
    }

    /**
	 * Creates a new annotated action object for the specified action. No
	 * contextual properties are provided.
	 * @param targetAction the action
	 */
    public AnnotatedAction(Action targetAction, AttributeMap attributes) {
        setTargetAction(targetAction);
        getAttributeMap().putAll(attributes);
    }

    /**
	 * Returns the wrapped target action.
	 * @return the action
	 */
    public Action getTargetAction() {
        return targetAction;
    }

    /**
	 * Set the target action wrapped by this decorator.
	 */
    public void setTargetAction(Action targetAction) {
        Assert.notNull(targetAction, "The targetAction is required");
        this.targetAction = targetAction;
    }

    /**
	 * Returns the name of a named action, or <code>null</code> if the action
	 * is unnamed. Used when mapping action result events to transitions.
	 * @see #isNamed()
	 * @see #postProcessResult(Event)
	 */
    public String getName() {
        return getAttributeMap().getString(NAME_PROPERTY);
    }

    /**
	 * Sets the name of a named action. This is optional and can be
	 * <code>null</code>.
	 * @param name the action name
	 */
    public void setName(String name) {
        getAttributeMap().put(NAME_PROPERTY, name);
    }

    /**
	 * Returns whether or not the wrapped target action is a named action.
	 * @see #setName(String)
	 */
    public boolean isNamed() {
        return StringUtils.hasText(getName());
    }

    /**
	 * Returns the name of the action method to invoke when the target action is
	 * executed.
	 */
    public MethodSignature getMethod() {
        return (MethodSignature) getAttributeMap().get(METHOD_PROPERTY, MethodSignature.class);
    }

    /**
	 * Sets the name of the action method to invoke when the target action is
	 * executed.
	 * @param method the action method name.
	 */
    public void setMethod(MethodSignature method) {
        getAttributeMap().put(METHOD_PROPERTY, method);
    }

    /**
	 * Returns the name of the attribute to export the action method return
	 * value under.
	 */
    public String getResultName() {
        return getAttributeMap().getString(RESULT_NAME_PROPERTY);
    }

    /**
	 * Sets the name of the action method to invoke when the target action is
	 * executed.
	 * @param resultName the action return value attribute name
	 */
    public void setResultName(String resultName) {
        getAttributeMap().put(RESULT_NAME_PROPERTY, resultName);
    }

    /**
	 * Returns the scope of the attribute to export the action method return
	 * value under.
	 */
    public ScopeType getResultScope() {
        return (ScopeType) getAttributeMap().get(RESULT_SCOPE_PROPERTY, ScopeType.class);
    }

    /**
	 * Sets the scope of the attribute storing the action method return value.
	 * @param resultScope the result scope
	 */
    public void setResultScope(ScopeType resultScope) {
        getAttributeMap().put(RESULT_SCOPE_PROPERTY, resultScope);
    }

    public Event execute(RequestContext context) throws Exception {
        try {
            context.setAttributes(getAttributeMap());
            Event result = getTargetAction().execute(context);
            return postProcessResult(result);
        } finally {
            context.setAttributes(null);
        }
    }

    /**
	 * Get the event id to be used as grounds for a transition in the containing
	 * state, based on given result returned from action execution.
	 * <p>
	 * If the wrapped action is named, the name will be used as a qualifier for
	 * the event (e.g. "myAction.success").
	 * @param resultEvent the action result event
	 */
    protected Event postProcessResult(Event resultEvent) {
        if (resultEvent == null) {
            return null;
        }
        if (isNamed()) {
            String qualifiedId = getName() + "." + resultEvent.getId();
            resultEvent = new Event(resultEvent.getSource(), qualifiedId, resultEvent.getAttributes());
        }
        return resultEvent;
    }

    public String toString() {
        return new ToStringCreator(this).append("targetAction", getTargetAction()).append("attributes", getAttributeMap()).toString();
    }
}
