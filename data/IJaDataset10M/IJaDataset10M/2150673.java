package com.alphacsp.cit.validators;

import com.alphacsp.cit.VariableValidator;
import java.util.Arrays;

/**
 * @author Yoav Hakman
 */
public class EnumValidator implements VariableValidator {

    private String[] allowedValues;

    public EnumValidator(String[] allowedValues) {
        this.allowedValues = allowedValues;
    }

    public EnumValidator() {
    }

    public String[] getAllowedValues() {
        return allowedValues;
    }

    public void setAllowedValues(String[] allowedValues) {
        this.allowedValues = allowedValues;
    }

    public void validate(EnvironmentVariableContext environmentVariableContext) throws ValidationException {
        boolean foundValue = false;
        for (String allowedValue : allowedValues) {
            if (allowedValue.equals(environmentVariableContext.getVariableValue().getAsText())) {
                foundValue = true;
                break;
            }
        }
        if (!foundValue) {
            StringBuilder message = new StringBuilder();
            message.append("The value of the variable ");
            message.append(environmentVariableContext.getVariableName());
            message.append(" (");
            message.append(environmentVariableContext.getVariableValue());
            message.append(") is not allowed. Allowed values: ");
            for (int i = 0; i < allowedValues.length; i++) {
                message.append(allowedValues[i]);
                if (i < allowedValues.length - 1) {
                    message.append(',');
                }
            }
            throw new ValidationException(message.toString());
        }
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final EnumValidator that = (EnumValidator) o;
        if (!Arrays.equals(allowedValues, that.allowedValues)) return false;
        return true;
    }
}
