package org.gwanted.gwt.core.client.data;

import com.google.gwt.http.client.Response;
import org.gwanted.gwt.core.client.exception.BaseException;

/**
 * Common Datasource Exception
 *
 * @author Diego Fernandez Santos
 */
public class DataSourceException extends BaseException {

    private Response response = null;

    /**
   * Constructor for DataSourceException class
   * @param response the Response object
   * @param mensaje message string for exception
   */
    public DataSourceException(final Response response, final String mensaje) {
        super(mensaje);
        this.response = response;
    }

    /**
   * Constructor that receives a message and the execution trace
   * @param message String, Cadena de error
   * @param stackTraceElement StackTraceElement, context error information
   */
    public DataSourceException(final String message, final StackTraceElement stackTraceElement) {
        super(message, stackTraceElement);
    }

    /**
   * @param trace StackTraceElement, Store the place where the exception
   * where raised
   * @param cause Throwable, The source exception
   */
    public DataSourceException(final StackTraceElement trace, final Throwable cause) {
        super(trace, cause);
    }

    /**
   * @param message String, Excepcion message
   * @param trace StackTraceElement, Store the place where the exception
   * was raised
   * @param cause Throwable, The source exception
   */
    public DataSourceException(final String message, final StackTraceElement trace, final Throwable cause) {
        super(message, trace, cause);
    }

    /**
   * Returns datasource Response object if any
   *
   * @return the Response object
   */
    public Response getResponse() {
        return this.response;
    }
}
