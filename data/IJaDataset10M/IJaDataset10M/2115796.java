package uk.icat3.exceptions;

/**
 *
 * @author cruzcruz
 */
public class RestrictionNullException extends RestrictionException {

    private static final String msg = "Values can't be null";

    public RestrictionNullException() {
        super(msg);
    }
}
