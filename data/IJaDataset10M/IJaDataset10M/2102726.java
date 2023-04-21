package com.colorado.core.diagnostics.logging;

import com.colorado.core.diagnostics.ErrorSeverity;

/**
 * Defines the functionality of a logger.
 * @author Dilan Perera
 */
public interface Logger {

    /**
     * Creates a log entry, based on the given message and/or exception of the 
     * specified severity, originating from the given type.
     * @param clazz the type from which the log request originated.
     * @param severity the severity of the log entry.
     * @param message the message to be logged.
     * @param exception the exception to be logged.
     */
    void Log(Class clazz, ErrorSeverity severity, String message, Exception exception);
}
