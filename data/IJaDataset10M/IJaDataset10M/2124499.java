package edu.upenn.law.io.exception;

/**
 * @author Eric Pancoast
 */
public class InvalidAnnotationFileStateException extends Error {

    public InvalidAnnotationFileStateException(int state) {
        super("Invalid Annotation File State: " + state);
    }
}
