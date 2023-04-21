package tictactoe.exceptions;

public class TTTException extends Exception {

    public TTTException(Throwable cause) {
        super(cause);
    }

    public TTTException(String message, Throwable cause) {
        super(message, cause);
    }

    public TTTException(String message) {
        super(message);
    }

    public TTTException() {
    }
}
