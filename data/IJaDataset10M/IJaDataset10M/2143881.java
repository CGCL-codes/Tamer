package purgatory.fieldml.exception;

public abstract class FieldmlException extends Exception {

    private static final long serialVersionUID = 8276143238514044036L;

    public final int errorCode;

    public FieldmlException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
