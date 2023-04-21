package uk.icat3.exceptions;

import uk.icat3.exceptions.ParameterSearchException;

/**
 *
 * @author cruzcruz
 */
public class NoParametersException extends ParameterSearchException {

    private static final String msg = "No parameters were defined";

    public NoParametersException() {
        super(NoParametersException.msg);
    }

    public NoParametersException(String msg) {
        super(NoParametersException.msg + ":" + msg);
    }
}
