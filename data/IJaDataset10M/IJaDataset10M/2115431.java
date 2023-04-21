package com.javascales.testing;

import java.util.List;

/**
 * Wraps an exception to display a nice message
 */
public class ReporterParameterException extends RuntimeException {

    List options = null;

    String parameter = null;

    public ReporterParameterException(String parameter, List options) {
        this.options = options;
        this.parameter = parameter;
    }

    public String getMessage() {
        StringBuffer message = new StringBuffer().append("Invalid option '").append(parameter).append("'.  Must be one of ").append(options);
        return message.toString();
    }
}
