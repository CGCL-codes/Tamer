package org.herasaf.xacml.core.policy.impl.jibx;

import org.herasaf.xacml.core.context.RequestInformation;
import org.herasaf.xacml.core.context.impl.jibx.RequestType;

/**
 * The result of the calculation if the Expression in a {@link VariableDefinitionType}.
 *
 * @author Sacha Dolski (sdolski@solnet.ch)
 * @author Lasantha Ranaweera
 * @version 1.0
 */
public class VariableValue implements Variable {

    private Object value;

    /**
	 * Initzializes an empty value.
	 */
    public VariableValue() {
    }

    /**
	 * Initializes the Variable value with the given value.
	 *
	 * @param value The value to place in this {@link VariableValue}.
	 */
    public VariableValue(Object value) {
        this();
        this.value = value;
    }

    /**
	 * Sets the value of this {@link VariableValue}.
	 *
	 * @param value The value to set.
	 */
    public void setValue(Object value) {
        this.value = value;
    }

    public Object getValue(RequestType request, RequestInformation reqInfo) {
        return value;
    }
}
