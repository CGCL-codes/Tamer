package bookez.model.exception;

public class BusinessLogicLayerException extends BookezException {

    public BusinessLogicLayerException(String message) {
        super(message);
    }

    public BusinessLogicLayerException(Throwable cause) {
        super(cause);
    }

    public BusinessLogicLayerException(String message, Throwable cause) {
        super(message, cause);
    }

    private static final long serialVersionUID = 4507335690429976102L;
}
