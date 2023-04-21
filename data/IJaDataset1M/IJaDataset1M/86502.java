package net.sbbi.upnp.messages;

import net.sbbi.upnp.services.ServiceStateVariable;

/**
 * This class contains data returned by a state variable query response
 * @author <a href="mailto:superbonbon@sbbi.net">SuperBonBon</a>
 * @version 1.0
 */
public class StateVariableResponse {

    protected ServiceStateVariable stateVar;

    protected String stateVariableValue;

    protected StateVariableResponse() {
    }

    public ServiceStateVariable getStateVar() {
        return stateVar;
    }

    public String getStateVariableValue() {
        return stateVariableValue;
    }
}
