package protopeer.network;

public class RecoverableNetworkException extends NetworkException {

    public RecoverableNetworkException() {
        super();
    }

    public RecoverableNetworkException(String message, Throwable cause) {
        super(message, cause);
    }

    public RecoverableNetworkException(String message) {
        super(message);
    }

    public RecoverableNetworkException(Throwable cause) {
        super(cause);
    }
}
