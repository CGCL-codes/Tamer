package net.sf.jdsc.asserts;

/**
 * @author <a href="mailto:twyss@users.sourceforge.net">twyss</a>
 * @version 1.0
 */
public class InvalidValueException extends AssertionException {

    private static final long serialVersionUID = 6786512618166752888L;

    private final Number value;

    public InvalidValueException(String message, Number value) {
        super(message);
        this.value = value;
    }

    public InvalidValueException(String message) {
        this(message, null);
    }

    public InvalidValueException() {
        this(null);
    }

    public Number getValue() {
        return value;
    }
}
