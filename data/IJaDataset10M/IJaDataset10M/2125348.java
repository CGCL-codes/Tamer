package org.sgodden.ui.mvc.impl;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sgodden.ui.mvc.Context;
import org.sgodden.ui.mvc.ContextAware;
import org.sgodden.ui.mvc.Flow;
import org.sgodden.ui.mvc.FlowOutcome;
import org.sgodden.ui.mvc.ObjectFactory;
import org.sgodden.ui.mvc.View;
import org.sgodden.ui.mvc.config.FlowStep;
import org.sgodden.ui.mvc.config.Transition;

/**
 * Default implementation of the {@link Flow} interface.
 * @author goddens
 */
public class FlowImpl implements Flow {

    private static final transient Log log = LogFactory.getLog(FlowImpl.class);

    /**
     * A simple internal variable to tell us whether we are being invoked for
     * the first time.
     */
    private boolean firstInvocation = true;

    private Map<String, String> namedObjects;

    private Map<String, Object> namedObjectInstances = new HashMap<String, Object>();

    private Map<String, FlowStep> flowStepConfigurations;

    private Map<String, Set<Transition>> globalResolutionMappings;

    private Map<String, Object> variables;

    /**
     * If we have been daisy-chained from a previous flow resolution factory,
     * then this is the outcome in the previous factory that caused us to be
     * invoked. We will reinvoke this resolution once this flow terminates. In
     * this respect, flow resolution factories operate as a simple linked list.
     */
    private FlowOutcome precedingFactoryFlowResolution;

    /**
     * The name of the initial step to be invoked.
     */
    private String initialStepName;

    /**
     * A factory used to create instances of participating objects.
     */
    private ObjectFactory objectFactory;

    /**
     * The flow step representing the last view shown to the user.
     */
    private FlowStep lastViewFlowStep;

    /**
     * The flow context.
     */
    private Context context;

    /**
     * Sets the context to be used in this flow.
     * @param context the context.
     */
    public void setContext(Context context) {
        this.context = context;
    }

    /**
     * Sets the steps of the flow.
     * @param flowSteps the steps of the flow.
     */
    public void setSteps(FlowStep[] flowSteps) {
        flowStepConfigurations = new HashMap<String, FlowStep>();
        for (FlowStep step : flowSteps) {
            if (step.getName() == null) {
                throw new IllegalArgumentException("All flow steps must have the 'name' attribute specified");
            }
            flowStepConfigurations.put(step.getName(), step);
        }
    }

    /**
     * Sets the factory which will create participant objects.
     * @param objectFactory the object factory.
     */
    public void setObjectFactory(ObjectFactory objectFactory) {
        this.objectFactory = objectFactory;
    }

    /**
     * Sets the global transitions to be applied to the flow.
     * @param transitions the global transitions to be applied to the flow.
     */
    public void setGlobalTransitions(Map<String, Set<Transition>> transitions) {
        this.globalResolutionMappings = transitions;
    }

    /**
     * Sets the map of named objects that will be used to provide view and
     * controller implementations.
     * @param namedObjects the set of managed objects.
     */
    public void setNamedObjects(Map<String, String> namedObjects) {
        this.namedObjects = namedObjects;
    }

    /**
     * Sets the name of the initial step to be invoked.
     * @param stepName the initial step to be invoked.
     */
    public void setInitialStepName(String stepName) {
        this.initialStepName = stepName;
    }

    /**
     * Executes a series of controller methods.
     * @param methodStrings an array of strings of the form
     *            'controllerName#methodName'. The methods must take no
     *            arguments, and any return value will be ignored.
     */
    private void executeControllerMethods(String[] methodStrings) {
        if (methodStrings != null) {
            for (String methodString : methodStrings) {
                log.debug("Executing controller method: " + methodString);
                String[] strings = methodString.split("#");
                if (strings.length != 2) {
                    throw new IllegalArgumentException("Invalid controller method specification: " + methodString);
                }
                String controllerName = strings[0];
                String methodName = strings[1];
                Object controller = getNamedObject(controllerName);
                try {
                    Method m = controller.getClass().getMethod(methodName, (Class[]) null);
                    m.invoke(controller, (Object[]) null);
                } catch (Exception e) {
                    throw new Error("Error invoking controller method: " + methodString, e);
                }
            }
        }
    }

    public FlowOutcome getFlowOutcome(String controllerResolution, FlowOutcome previousFlowResolution) {
        FlowOutcome ret = null;
        log.debug("Obtaining flow outcome for resolution: " + controllerResolution);
        if (firstInvocation) {
            log.debug("First invocation - configuring from initial flow step");
            firstInvocation = false;
            precedingFactoryFlowResolution = previousFlowResolution;
            FlowStep step = getInitialFlowStep();
            executeControllerMethods(step.getEntryControllerMethods());
            ret = configureFlowOutcomeFromFlowStep(step, previousFlowResolution);
        } else {
            ret = handleResolution(controllerResolution, (FlowOutcomeImpl) previousFlowResolution);
        }
        log.debug("Returning flow outcome: " + ret);
        return ret;
    }

    @SuppressWarnings("unchecked")
    private FlowOutcome handleResolution(String resolutionName, FlowOutcomeImpl previousFlowResolution) {
        FlowOutcome ret = null;
        log.debug("Processing resolution: flow step=" + previousFlowResolution.getFlowStepName() + ", resolutionName=" + resolutionName);
        if (!flowStepConfigurations.containsKey(previousFlowResolution.getFlowStepName())) {
            throw new IllegalArgumentException("Unknown flow step: " + previousFlowResolution.getFlowStepName());
        }
        FlowStep source = flowStepConfigurations.get(previousFlowResolution.getFlowStepName());
        Transition transition = getTransition(resolutionName, source);
        executeControllerMethods(transition.getControllerMethods());
        String destination = transition.getTo();
        log.debug("Found destination: " + destination);
        executeControllerMethods(source.getExitControllerMethods());
        FlowStep target = null;
        if (destination.equals("*LAST-VIEW")) {
            target = lastViewFlowStep;
        } else {
            target = flowStepConfigurations.get(destination);
        }
        if (target == null) {
            throw new IllegalArgumentException("Unknown flow step: " + destination);
        }
        executeControllerMethods(target.getEntryControllerMethods());
        if (target.getEndState() != null) {
            log.debug("Terminating flow with resolution: " + target.getEndState());
            if (precedingFactoryFlowResolution != null) {
                ret = precedingFactoryFlowResolution.getFlowOutcome(target.getEndState());
            }
        } else if (target.getSubFlowName() != null) {
            log.debug("Invoking nested flow: " + target.getSubFlowName());
            try {
                FlowImpl nextFlow = (FlowImpl) objectFactory.makeObject(target.getSubFlowName());
                Context newContext = new Context();
                newContext.setControllerResolutionHandler(this.context.getControllerResolutionHandler());
                newContext.setFlow(nextFlow);
                nextFlow.setContext(newContext);
                if (target.getSubFlowInputParameters() != null) {
                    Date date = new Date();
                    Map<String, String> mappings = target.getSubFlowInputParameters();
                    for (String name : mappings.keySet()) {
                        String value = mappings.get(name);
                        Object evaluated = value;
                        if (value.startsWith("${")) {
                            value = value.trim();
                            evaluated = context.evaluate(value.substring(2, value.length() - 1));
                        }
                        log.debug("Setting parameter: " + name + ", " + evaluated);
                        nextFlow.setVariable(name, evaluated);
                    }
                    if (log.isDebugEnabled()) {
                        Date date2 = new Date();
                        long millis = date2.getTime() - date.getTime();
                        log.debug("Took " + millis + " ms to evaulate all parameter expressions");
                    }
                }
                ret = nextFlow.getFlowOutcome(resolutionName, new FlowOutcomeImpl(this, this, previousFlowResolution, target.getName(), target.getDescription()));
            } catch (Exception e) {
                throw new Error("Error trying to instantiate the " + "next flow: " + target.getSubFlowName(), e);
            }
        } else {
            ret = configureFlowOutcomeFromFlowStep(target, previousFlowResolution);
        }
        return ret;
    }

    /**
     * Determines the destination for a particular resolution from a previous
     * flow step.
     * @param resolutionName
     * @param source
     * @return
     */
    @SuppressWarnings("unchecked")
    private Transition getTransition(String resolutionName, FlowStep source) {
        Transition destination = null;
        Set<Transition> transitions = source.getTransitions(resolutionName);
        destination = getTransition(transitions);
        if (destination == null && globalResolutionMappings != null) {
            transitions = globalResolutionMappings.get(resolutionName);
            destination = getTransition(transitions);
        }
        if (destination == null) {
            throw new IllegalArgumentException("No valid destination could be found: " + resolutionName + "; Flow resolution factory class name: " + this.getClass().getName());
        }
        return destination;
    }

    /**
     * Returns the destination of the first valid transition in the passed set
     * (which may be null).
     * @param transitions the set of transitions to test, which may be null.
     * @return the destination of the first matching transition, or
     *         <code>null</code> if either there is no matching transition, or
     *         the input argument is <code>null</code>.
     */
    private Transition getTransition(Set<Transition> transitions) {
        Transition ret = null;
        if (transitions != null) {
            for (Transition transition : transitions) {
                if (transition.getGuard() == null || transition.getGuard().approve(context)) {
                    ret = transition;
                    break;
                }
            }
        }
        return ret;
    }

    /**
     * Returns the initial flow step.
     * @return the initial flow step.
     */
    private FlowStep getInitialFlowStep() {
        FlowStep ret = null;
        if (initialStepName == null) {
            throw new IllegalStateException("The initial step name has not been set");
        }
        if (!(flowStepConfigurations.containsKey(initialStepName))) {
            throw new IllegalArgumentException("No step exists for name: " + initialStepName);
        }
        ret = flowStepConfigurations.get(initialStepName);
        return ret;
    }

    /**
     * Configures a flow outcome from a destination flow step.
     * @param destination the destination flow step.
     * @param previousFlowOutcome the previous flow outcome.
     * @return the new flow outcome.
     */
    private FlowOutcome configureFlowOutcomeFromFlowStep(FlowStep target, FlowOutcome previousFlowOutcome) {
        FlowOutcome ret;
        if (target.getControllerName() != null) {
            ret = configureFlowResolutionFromControllerFlowStep(target, previousFlowOutcome);
        } else {
            lastViewFlowStep = target;
            ret = configureFlowResolutionFromViewFlowStep(target, previousFlowOutcome);
        }
        return ret;
    }

    /**
     * Configures a controller flow outcome from the destination controller flow
     * step.
     * @param destination the destination controller flow step.
     * @param previousFlowOutcome the previous flow outcome.
     * @return the new flow outcome.
     */
    private FlowOutcome configureFlowResolutionFromControllerFlowStep(FlowStep destination, FlowOutcome previousFlowOutcome) {
        Object controller = getNamedObject(destination.getControllerName());
        return new ControllerFlowOutcomeImpl(this, controller, destination.getControllerMethod(), this, destination.getName(), destination.getDescription(), previousFlowOutcome);
    }

    /**
     * Configures a view flow outcome from the destination view flow step.
     * @param destination the destination view flow step.
     * @param previousFlowOutcome the previous flow outcome.
     * @return
     */
    private FlowOutcome configureFlowResolutionFromViewFlowStep(FlowStep destination, FlowOutcome previousFlowOutcome) {
        View controller = (View) getNamedObject(destination.getViewName());
        return new ViewFlowOutcomeImpl(this, controller, this, previousFlowOutcome, destination);
    }

    public Object getNamedObject(String objectName) {
        Object ret = null;
        if (namedObjectInstances.containsKey(objectName)) {
            ret = namedObjectInstances.get(objectName);
        } else if (namedObjects.containsKey(objectName)) {
            String actualObjectName = namedObjects.get(objectName);
            log.debug("Creating object with name: " + actualObjectName);
            ret = objectFactory.makeObject(actualObjectName);
            namedObjectInstances.put(objectName, ret);
            if (ret instanceof ContextAware && context != null) {
                ((ContextAware) ret).setContext(context);
            }
        } else {
            throw new IllegalArgumentException("Unknown named object: " + objectName);
        }
        return ret;
    }

    /**
     * Returns the value of the specified flow variable, or <code>null</code> if
     * no variable by that name is set.
     * @param name the name of the variable.
     * @return the value, or <code>null</code> if no value is set.
     */
    public Object getVariable(String name) {
        if (variables != null && variables.containsKey(name)) {
            return variables.get(name);
        } else {
            return null;
        }
    }

    /**
     * See {@link Flow#getVariables()}.
     * @return an unmodifiable map of the current flow variables.
     */
    public Map<String, Object> getVariables() {
        return Collections.unmodifiableMap(variables);
    }

    /**
     * Sets or unsets a variable on the flow.
     * @param name the name of the variable.
     * @param value the value, or <code>null</code> to unset the variable.
     */
    public void setVariable(String name, Object value) {
        if (variables == null) {
            variables = new HashMap<String, Object>();
        }
        if (value != null) {
            variables.put(name, value);
        } else {
            variables.remove(name);
        }
    }
}
