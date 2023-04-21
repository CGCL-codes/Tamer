package net.sourceforge.cruisecontrol.dashboard.exception;

public class ExecutionException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ExecutionException(String message, Exception nestedException) {
        super(message, nestedException);
    }
}
