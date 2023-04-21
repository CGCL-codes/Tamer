package it.xargon.lrpc;

public class LRpcNullPointerException extends LRpcException {

    public LRpcNullPointerException() {
        super();
    }

    public LRpcNullPointerException(String message) {
        super(message);
    }

    public LRpcNullPointerException(Throwable cause) {
        super(cause);
    }

    public LRpcNullPointerException(String message, Throwable cause) {
        super(message, cause);
    }
}
