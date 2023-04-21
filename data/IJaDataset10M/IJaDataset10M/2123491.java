package edu.ua.j3dengine.processors;

public class ProcessorValidationException extends Exception {

    public ProcessorValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProcessorValidationException(String message) {
        super(message);
    }

    public ProcessorValidationException(Throwable cause) {
        super(cause);
    }
}
