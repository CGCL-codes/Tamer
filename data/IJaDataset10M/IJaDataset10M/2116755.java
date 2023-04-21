package org.dcopolis.algorithm.adopt;

import org.dcopolis.platform.AgentMessage;
import org.dcopolis.problem.Variable;
import java.io.Serializable;

public class ValueMessage extends AgentMessage implements Serializable {

    private static final long serialVersionUID = -405391510141406007L;

    Variable variable;

    Object value;

    public ValueMessage(Variable variable, Object value, Variable recipient) {
        super(recipient);
        this.variable = variable;
        this.value = value;
    }

    public Variable getVariable() {
        return variable;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "ValueMessage " + variable.getName() + " = " + value.toString();
    }
}
