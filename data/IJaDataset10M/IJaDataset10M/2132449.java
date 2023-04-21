package com.agentfactory.afapl2.debugger.model;

import java.io.Serializable;
import java.util.Map;
import com.agentfactory.afapl2.interpreter.mentalState.Action;

/**
 *
 * @author Administrator
 */
public class ActionDescription implements Serializable {

    private String identifier;

    private String preCondition;

    private String postCondition;

    private String actuator;

    private int cardinality;

    private Map configuration;

    /** Creates a new instance of ActionDescription */
    public ActionDescription(Action action) {
        identifier = action.getIdentifier().toString();
        preCondition = action.getPreCondition().toString();
        postCondition = action.getPostCondition().toString();
        actuator = action.getActuator().getClass().getName();
        cardinality = action.getCardinality();
        configuration = action.getActuator().getConfiguration();
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getPreCondition() {
        return preCondition;
    }

    public String getPostCondition() {
        return postCondition;
    }

    public String getActuator() {
        return actuator;
    }

    public int getCardinality() {
        return cardinality;
    }

    public Map getConfiguration() {
        return configuration;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ActionDescription) {
            ActionDescription desc = (ActionDescription) obj;
            return identifier.equals(desc.identifier) && preCondition.equals(desc.preCondition) && postCondition.equals(desc.postCondition) && actuator.equals(desc.actuator) && (cardinality == desc.cardinality) && configuration.equals(desc.configuration);
        }
        return false;
    }
}
