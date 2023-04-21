package com.volantis.mcs.papi;

/**
 * This class is used when an exception occurs inside PAPI.
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
*/
public class PAPIException extends Exception {

    /**
   * Create a new <code>PAPIException</code> with no message.
   */
    public PAPIException() {
    }

    /**
   * Create a new <code>PAPIException</code>
   * with the specified message.
   * @param message The message.
   */
    public PAPIException(String message) {
        super(message);
    }

    /**
   * Create a new <code>PAPIException</code> with the specified 
   * message which was caused by the specified Throwable.
   * @param message The message.
   * @param cause The cause of this exception being thrown.
   */
    public PAPIException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
   * Create a new <code>PAPIException</code> which was caused by the
   * specified Throwable.
   * @param cause The cause of this exception being thrown.
   */
    public PAPIException(Throwable cause) {
        super(cause);
    }
}
