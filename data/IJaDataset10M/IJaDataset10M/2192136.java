package com.mockturtlesolutions.snifflib.invprobs;

public class FunctionEvaluationException extends Exception {

    public FunctionEvaluationException() {
        super();
    }

    public FunctionEvaluationException(String msg) {
        super(msg);
    }

    public FunctionEvaluationException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public FunctionEvaluationException(Throwable cause) {
        super(cause);
    }
}
