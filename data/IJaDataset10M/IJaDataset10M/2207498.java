package us.wthr.jdem846.model.exceptions;

@SuppressWarnings("serial")
public class MethodContainerInvokeException extends Exception {

    public MethodContainerInvokeException() {
        super();
    }

    public MethodContainerInvokeException(String message) {
        super(message);
    }

    public MethodContainerInvokeException(String message, Throwable thrown) {
        super(message, thrown);
    }
}
