package org.qedeq.kernel.common;

/**
 * Data validation error for a QEDEQ module. An error has always a reference to its
 * location. Maybe an additional reference for another location is provided.
 *
 * @version $Revision: 1.1 $
 * @author  Michael Meyling
 */
public abstract class ModuleDataException extends QedeqException {

    /** Error location. */
    private ModuleContext context;

    /** Reference location to explain the error. */
    private ModuleContext referenceContext;

    /**
     * Constructor.
     *
     * @param   errorCode   Error code of this message.
     * @param   message     Error message.
     * @param   context     Error location.
     * @param   referenceContext  Reference location.
     * @param   cause       Detailed exception information.
     */
    public ModuleDataException(final int errorCode, final String message, final ModuleContext context, final ModuleContext referenceContext, final Exception cause) {
        super(errorCode, message, cause);
        this.context = (context == null ? null : new ModuleContext(context));
        this.referenceContext = (referenceContext == null ? null : new ModuleContext(referenceContext));
    }

    /**
     * Constructor.
     *
     * @param   errorCode   Error code of this message.
     * @param   message     Error message.
     * @param   context     Error location.
     * @param   referenceContext  Reference location.
     */
    public ModuleDataException(final int errorCode, final String message, final ModuleContext context, final ModuleContext referenceContext) {
        super(errorCode, message);
        this.context = (context == null ? null : new ModuleContext(context));
        this.referenceContext = (referenceContext == null ? null : new ModuleContext(referenceContext));
    }

    /**
     * Constructor.
     *
     * @param   errorCode   Error code of this message.
     * @param   message     Error message.
     * @param   context     Error location.
     * @param   cause       Detailed exception information.
     */
    public ModuleDataException(final int errorCode, final String message, final ModuleContext context, final Exception cause) {
        super(errorCode, message, cause);
        this.context = (context == null ? null : new ModuleContext(context));
        this.referenceContext = null;
    }

    /**
     * Constructor.
     *
     * @param   errorCode   Error code of this message.
     * @param   message     Error message.
     * @param   context     Error location.
     */
    public ModuleDataException(final int errorCode, final String message, final ModuleContext context) {
        super(errorCode, message);
        this.context = (context == null ? null : new ModuleContext(context));
        this.referenceContext = null;
    }

    /**
     * Get context information about error location.
     *
     * @return  Error location context.
     */
    public final ModuleContext getContext() {
        return context;
    }

    /**
     * Get additional context information about another associated location.
     *
     * @return  Additional error location context.
     */
    public final ModuleContext getReferenceContext() {
        return referenceContext;
    }
}
