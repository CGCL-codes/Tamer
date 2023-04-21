package net.sf.twip.internal;

import net.sf.twip.util.Parameter;

public class TwipConfigurationErrorInvalidParameterType extends TwipConfigurationError {

    private static final long serialVersionUID = -7530965110214226835L;

    private final Parameter parameter;

    private final Object parameterValue;

    public TwipConfigurationErrorInvalidParameterType(Parameter parameter, Object parameterValue) {
        super((String) null);
        this.parameter = parameter;
        this.parameterValue = parameterValue;
    }

    @Override
    public String getMessage() {
        return "parameter #" + parameter.getIndex() + " is of type " + parameter.getType().getSimpleName() + ", so you can't assign it a " + parameterValue.getClass().getSimpleName();
    }
}
