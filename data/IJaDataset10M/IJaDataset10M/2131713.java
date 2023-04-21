package de.mindmatters.faces.lifecycle;

import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;

/**
 * Implements the default <em>Invoke Application</em> phase of the faces
 * lifecycle.
 * 
 * @author Andreas Kuhrwahl
 */
public class InvokeApplicationPhase extends AbstractPhase {

    /**
     * {@inheritDoc}
     */
    public final PhaseId getId() {
        return PhaseId.INVOKE_APPLICATION;
    }

    /**
     * {@inheritDoc}
     */
    protected void executePhase(final FacesContext context) {
        try {
            context.getViewRoot().processApplication(context);
        } catch (RuntimeException ex) {
            if (ex instanceof FacesException) {
                throw ex;
            } else {
                throw new FacesException(ex.getMessage(), ex);
            }
        }
    }
}
