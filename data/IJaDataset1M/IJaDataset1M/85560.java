package org.qedeq.kernel.bo.module;

/**
 * Data validation error for a QEDEQ module. Occurs if a set or add method leads to wrong or
 * inconsistent data.
 *
 * @version $Revision: 1.3 $
 * @author  Michael Meyling
 */
public class IllegalModuleDataException extends ModuleDataException {

    /**
     * Constructor.
     *
     * @param   errorCode   Error code of this message.
     * @param   message     Error message.
     * @param   context     Error location.
     * @param   referenceContext  Reference location.
     * @param   cause       Detailed exception information.
     */
    public IllegalModuleDataException(final int errorCode, final String message, final ModuleContext context, final ModuleContext referenceContext, final Exception cause) {
        super(errorCode, message, context, referenceContext, cause);
    }

    /**
     * Constructor.
     *
     * @param   errorCode   Error code of this message.
     * @param   message     Error message.
     * @param   context     Error location.
     * @param   cause       Detailed exception information.
     */
    public IllegalModuleDataException(final int errorCode, final String message, final ModuleContext context, final Exception cause) {
        super(errorCode, message, context, cause);
    }

    /**
     * Constructor.
     *
     * @param   errorCode   Error code of this message.
     * @param   message     Error message.
     * @param   context     Error location.
     */
    public IllegalModuleDataException(final int errorCode, final String message, final ModuleContext context) {
        super(errorCode, message, context);
    }
}
