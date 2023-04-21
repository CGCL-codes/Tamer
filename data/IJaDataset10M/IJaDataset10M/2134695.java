package org.jaffa.modules.printing.services.exceptions;

import org.jaffa.exceptions.FrameworkException;

/** This exception is thrown if there is a problem processing the form in
 * the engine
 */
public class FormTemplateUrlNotSupportedException extends FormPrintException {

    private static final String ERROR_CODE = "exception.org.jaffa.modules.printing.exceptions.FormTemplateUrlNotSupportedException";

    /** Creates an exception with the errorCode.
     */
    public FormTemplateUrlNotSupportedException() {
        this((Object[]) null);
    }

    /** Creates an exception with the errorCode and a cause.
     * @param argument1 an argument, if any, that needs to be merged into the error message from the resource bundle.
     */
    public FormTemplateUrlNotSupportedException(String argument1) {
        this(new String[] { argument1 }, null);
    }

    /** Creates an exception with the errorCode and a cause.
     * @param argument1 an argument, if any, that needs to be merged into the error message from the resource bundle.
     * @param cause the cause.
     */
    public FormTemplateUrlNotSupportedException(String argument1, Throwable cause) {
        this(new String[] { argument1 }, cause);
    }

    /** Creates an exception with the errorCode and a cause.
     * @param arguments the arguments, if any, that needs to be merged into the error message from the resource bundle.
     */
    public FormTemplateUrlNotSupportedException(Object[] arguments) {
        this(arguments, null);
    }

    /** Creates an exception with the errorCode and a cause.
     * @param arguments the arguments, if any, that need to be merged into the error message from the resource bundle.
     * @param cause the cause.
     */
    public FormTemplateUrlNotSupportedException(Object[] arguments, Throwable cause) {
        super(ERROR_CODE, arguments, cause);
    }
}
