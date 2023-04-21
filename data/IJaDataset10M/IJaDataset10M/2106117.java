package uk.icat3.exceptions;

/**
 *
 * @author cruzcruz
 */
public class OperatorINException extends RestrictionException {

    private static final String msg = "Type no recognized. Only Collection and String.";

    public OperatorINException() {
        super(msg);
    }

    public OperatorINException(String msg) {
        super("Restriction IN exception. " + msg);
    }
}
