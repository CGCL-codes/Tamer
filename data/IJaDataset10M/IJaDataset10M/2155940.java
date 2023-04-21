package org.in4ama.documentengine.exception;

/** Thrown when the GROOVY evaluation went wrong */
public class EvaluationException extends Exception {

    public EvaluationException(String msg) {
        super(msg);
    }

    public EvaluationException(String msg, Throwable t) {
        super(msg, t);
    }
}
