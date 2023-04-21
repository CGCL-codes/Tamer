package cz.xf.tomason.tictactoe.exceptions.network;

public class UnableToOpenChannelException extends NetworkException {

    public UnableToOpenChannelException() {
    }

    public UnableToOpenChannelException(String message) {
        super(message);
    }

    public UnableToOpenChannelException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnableToOpenChannelException(Throwable cause) {
        super(cause);
    }
}
