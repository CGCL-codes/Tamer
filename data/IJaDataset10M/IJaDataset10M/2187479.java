package de.mindmatters.faces.application;

import javax.faces.application.StateManager;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.render.RenderKit;

/**
 * <strong>NoStateManager</strong> does <strong>not</strong> save the state of
 * the component tree and will thus not restore the component state.
 * 
 * <p>
 * This {@link StateManager} will work in most cases and should be used to
 * eliminate any state foot print on the client.
 * </p>
 * 
 * @author Andreas Kuhrwahl
 * 
 */
public final class NoStateManager extends AbstractViewBuildingStateManager {

    /**
     * Creates an OptimizedStateManager with the given state manager
     * <code>delegate</code> of the underlying JSF implementation.
     * 
     * @param delegate
     *            the original state manager of the underlying JSF
     *            implementation
     */
    public NoStateManager(final StateManager delegate) {
        super(delegate);
    }

    /**
     * {@inheritDoc}
     */
    protected void restoreState(final FacesContext context, final UIViewRoot viewRoot, final RenderKit renderKit) {
    }

    /**
     * {@inheritDoc}
     */
    protected Object saveState(final FacesContext context) {
        return null;
    }
}
