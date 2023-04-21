package org.gcli.annotations;

public class ExtraParameterException extends RuntimeException {

    private final String extraParameters;

    public ExtraParameterException(String extraParameters) {
        this.extraParameters = extraParameters;
    }

    public String getExtraParameters() {
        return extraParameters;
    }
}
