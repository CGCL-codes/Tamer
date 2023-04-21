package org.nakedobjects.reflector.java.reflect;

import java.lang.reflect.Method;

class ParameterMethods {

    private final Method labelMethod;

    private final Method mandatoryMethod;

    private final Method optionsMethod;

    private final Method defaultsMethod;

    public ParameterMethods(final Method labelMethod, final Method mandatoryMethod, final Method defaultsMethod, final Method optionsMethod) {
        this.labelMethod = labelMethod;
        this.mandatoryMethod = mandatoryMethod;
        this.defaultsMethod = defaultsMethod;
        this.optionsMethod = optionsMethod;
    }

    public final Method getLabelMethod() {
        return labelMethod;
    }

    public final Method getMandatoryMethod() {
        return mandatoryMethod;
    }

    public final Method getDefaultsMethod() {
        return defaultsMethod;
    }

    public final Method getOptionsMethod() {
        return optionsMethod;
    }
}
