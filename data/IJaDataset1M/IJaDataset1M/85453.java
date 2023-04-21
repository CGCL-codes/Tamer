package hu.sztaki.lpds.pgportal.services.utils.timeoutExecutor;

public class FunctionTimeoutException extends Exception {

    public FunctionTimeoutException(String msg) {
        super(msg);
    }

    public FunctionTimeoutException() {
        super();
    }
}
