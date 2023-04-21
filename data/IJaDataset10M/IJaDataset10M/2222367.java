package com.idna.trace.core.response;

import java.util.List;
import com.idna.trace.utils.KeyValuePair;
import com.idna.trace.utils.exceptions.ErrorCorporate;
import com.idna.trace.utils.parameters.Parameters;
import com.idna.trace.utils.parameters.consumer.ParametersConsumer;

public class Response {

    private Parameters parameters;

    private List<ErrorCorporate> errors;

    private boolean isMultiLocation;

    /**
     * JAXB and other XML generator tools require a default non-argument
     * constructor
     */
    public Response() {
    }

    public Response(Parameters parameters) {
        this.parameters = parameters;
    }

    public Response(Parameters parameters, boolean isMultiLocation) {
        this.parameters = parameters;
        this.isMultiLocation = isMultiLocation;
    }

    public Response(List<ErrorCorporate> errors, Parameters parameters) {
        this.errors = errors;
        this.parameters = parameters;
    }

    public List<ErrorCorporate> getErrors() {
        return errors;
    }

    public void setErrors(List<ErrorCorporate> errors) {
        this.errors = errors;
    }

    public Parameters getParams() {
        return parameters;
    }

    public List<KeyValuePair<String, Object>> getParameters() {
        return ParametersConverter.convert((ParametersConsumer) parameters);
    }

    public void setParameters(Parameters parameters) {
        this.parameters = parameters;
    }

    public boolean isMultiLocation() {
        return isMultiLocation;
    }

    public void setMultiLocation(boolean isMultiLocation) {
        this.isMultiLocation = isMultiLocation;
    }
}
