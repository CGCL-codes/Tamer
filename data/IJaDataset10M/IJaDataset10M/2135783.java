package de.ios.framework.basic;

import java.io.*;

/**
 * Interface for NestingExceptions, especially NestingException and NestingRuntimeException.
 */
public interface NestingExceptions extends java.io.Serializable {

    /**
   * Get the internal throwable that has caused this exception.
   * @return Throwbale or null.
   */
    public Throwable getInternalException();

    /**
   * Get the most internal throwable that has caused this exception.
   * @return Throwbale or null.
   */
    public Throwable getBasicException();

    /**
   * Get the trace of the lowest encapsulated exception.
   */
    public String getNestedTrace();
}
