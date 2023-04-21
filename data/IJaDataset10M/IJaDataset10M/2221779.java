package org.ikasan.flow.visitorPattern;

import java.util.HashMap;
import java.util.Map;
import org.ikasan.spec.flow.FlowElement;

/**
 * Simple implementation of <code>FlowElement</code>
 * 
 * @author Ikasan Development Team
 */
public class FlowElementImpl<COMPONENT> implements FlowElement<COMPONENT> {

    /** <code>FlowComponent</code> being wrapped and given flow context */
    private COMPONENT flowComponent;

    /** Flow context specific name for the wrapped component */
    private String componentName;

    /** <code>Map</code> of all flowComponent results to downstream <code>FlowElement</code>s */
    private Map<String, FlowElement> transitions;

    /**
     * Human readable description of this FlowElement
     */
    private String description;

    /**
     * Constructor for when there are more than one subsequent <code>FlowElement</code>s
     * 
     * @param componentName The name of the component
     * @param flowComponent The FlowComponent
     * @param transitions A map of transitions
     */
    public FlowElementImpl(String componentName, COMPONENT flowComponent, Map<String, FlowElement> transitions) {
        this.componentName = componentName;
        this.flowComponent = flowComponent;
        this.transitions = transitions;
    }

    /**
     * Overloaded constructor for when there is at most one subsequent <code>FlowElement</code>
     * 
     * @param componentName The name of the component
     * @param flowComponent The FlowComponent
     * @param defaultTransition The default transition
     */
    public FlowElementImpl(String componentName, COMPONENT flowComponent, FlowElement defaultTransition) {
        this(componentName, flowComponent, createTransitionMap(defaultTransition));
    }

    /**
     * Overloaded constructor for a <code>FlowElement</code> with no downstream
     * 
     * @param componentName The name of the component
     * @param flowComponent The FlowComponent
     */
    public FlowElementImpl(String componentName, COMPONENT flowComponent) {
        this(componentName, flowComponent, (Map<String, FlowElement>) null);
    }

    /**
     * Creates the transition map when there is just the default transition
     * 
     * @param defaultTransition The default transition
     * @return Map<String, FlowElement> mapping "default" to the specified <code>FlowElement</code>
     */
    private static Map<String, FlowElement> createTransitionMap(FlowElement defaultTransition) {
        Map<String, FlowElement> defaultTransitions = new HashMap<String, FlowElement>();
        defaultTransitions.put(DEFAULT_TRANSITION_NAME, defaultTransition);
        return defaultTransitions;
    }

    public COMPONENT getFlowComponent() {
        return flowComponent;
    }

    public String getComponentName() {
        return componentName;
    }

    public FlowElement getTransition(String transitionName) {
        FlowElement result = null;
        if (transitions != null) {
            result = transitions.get(transitionName);
        }
        return result;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Setter for description
     * 
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(getClass().getName() + " [");
        sb.append("name=");
        sb.append(componentName);
        sb.append(",");
        sb.append("flowComponent=");
        sb.append(flowComponent);
        sb.append(",");
        sb.append("transitions=");
        sb.append(transitions);
        sb.append("]");
        return sb.toString();
    }

    public Map<String, FlowElement> getTransitions() {
        Map<String, FlowElement> result = new HashMap<String, FlowElement>();
        if (transitions != null) {
            result.putAll(transitions);
        }
        return result;
    }
}
