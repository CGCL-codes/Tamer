package com.intel.gpe.clients.api.workflow;

/**
 * The pair of the variable and its value.
 * 
 * @author Alexander Lukichev
 * @version $Id: BoundVariable.java,v 1.3 2006/04/11 08:58:52 lukichev Exp $
 */
public class BoundVariable<VariableValueType extends VariableValue> {

    private Variable variable;

    private VariableValueType value;

    public BoundVariable(Variable variable, VariableValueType value) {
        super();
        this.variable = variable;
        this.value = value;
    }

    /**
     * @return The variable's value
     */
    public VariableValueType getValue() {
        return value;
    }

    /**
     * @return The variable's name
     */
    public Variable getVariable() {
        return variable;
    }
}
