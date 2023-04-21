package org.jaffa.modules.printing.services.exceptions;

import org.jaffa.exceptions.FrameworkException;

/** This exception is thrown if the Persistence Engine could not be instantiated.
 */
public class EngineInstantiationException extends FormPrintException {

    private static final String ERROR_CODE = "exception.org.jaffa.modules.printing.exceptions.EngineInstantiationException";

    /** Creates an exception with the errorCode.
     */
    public EngineInstantiationException() {
        this(null);
    }

    /** Creates an exception with the errorCode and a cause.
     * @param argument0 the arguments, if any, that need to be merged into the error message from the resource bundle.
     */
    public EngineInstantiationException(Object argument0) {
        this(new Object[] { argument0 }, null);
    }

    /** Creates an exception with the errorCode and a cause.
     * @param argument0 the arguments, if any, that need to be merged into the error message from the resource bundle.
     * @param cause the cause.
     */
    public EngineInstantiationException(Object argument0, Throwable cause) {
        this(new Object[] { argument0 }, cause);
    }

    /** Creates an exception with the errorCode and a cause.
     * @param arguments the arguments, if any, that need to be merged into the error message from the resource bundle.
     */
    public EngineInstantiationException(Object[] arguments) {
        this(arguments, null);
    }

    /** Creates an exception with the errorCode and a cause.
     * @param arguments the arguments, if any, that need to be merged into the error message from the resource bundle.
     * @param cause the cause.
     */
    public EngineInstantiationException(Object[] arguments, Throwable cause) {
        super(ERROR_CODE, arguments, cause);
    }
}
