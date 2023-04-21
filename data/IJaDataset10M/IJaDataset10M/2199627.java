package com.angel.io.exceptions;

/**
 * @author William
 *
 */
public class FileProcessorRunnerValidatorException extends RuntimeException {

    private static final long serialVersionUID = -1516792405947461745L;

    public FileProcessorRunnerValidatorException(String message) {
        super(message);
    }

    public FileProcessorRunnerValidatorException(Throwable cause) {
        super(cause);
    }

    public FileProcessorRunnerValidatorException(String message, Throwable cause) {
        super(message, cause);
    }
}
