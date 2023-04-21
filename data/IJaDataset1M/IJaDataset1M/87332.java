package org.tm4j.topicmap.utils;

/**
 * Exception thrown when an attempt is made to set a runtime property for
 * a TopicMapBuilder implementation using a property name that is not recognised by
 * that implementation.
 */
public class BuilderPropertyNotRecognizedException extends Exception {

    public BuilderPropertyNotRecognizedException(String msg) {
        super(msg);
    }
}
