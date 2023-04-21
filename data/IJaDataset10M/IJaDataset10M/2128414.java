package org.bing.adapter.com.caucho.hessian.io;

import org.bing.adapter.com.caucho.hessian.HessianException;

/**
 * Exception for faults when the fault doesn't return a java exception.
 * This exception is required for MicroHessianInput.
 */
public class HessianMethodSerializationException extends HessianException {

    /**
   * Zero-arg constructor.
   */
    public HessianMethodSerializationException() {
    }

    /**
   * Create the exception.
   */
    public HessianMethodSerializationException(String message) {
        super(message);
    }

    /**
   * Create the exception.
   */
    public HessianMethodSerializationException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
   * Create the exception.
   */
    public HessianMethodSerializationException(Throwable cause) {
        super(cause);
    }
}
