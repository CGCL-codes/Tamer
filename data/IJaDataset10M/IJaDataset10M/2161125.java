package org.soatest.core.model;

import java.util.Collection;
import java.util.Vector;

public class Input {

    private Collection<Parameter> parameters;

    public Input() {
        parameters = new Vector<Parameter>();
    }

    public Collection<Parameter> getParameters() {
        return parameters;
    }

    public void setParameters(Collection<Parameter> parameters) {
        this.parameters = parameters;
    }

    public void addParameter(Parameter parameter) {
        this.parameters.add(parameter);
    }
}
