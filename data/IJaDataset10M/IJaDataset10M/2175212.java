package org.dancecues.gui.util;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

/**
 * A base class for any Actions, implements actionPerformed to call
 * a new method, internalActionPerformed, and if any exception is
 * thrown, catches it, logs it, displays the error dialog.  This prevents
 * the AWT thread from swallowing the exception.
 */
public abstract class CatchingAction extends AbstractAction {

    private static final long serialVersionUID = 1L;

    /**
     * The description of the action.  If the internalActionPerformed implementation
     * is really long, the child class may want to update this as it goes.
     */
    protected String _whatAmIDoing;

    /**
     * Construct an Action class with a description of what the action is,
     * so that we can log the error and/or notify the user.
     * @param actionDescription What action was being attempted.  Should fill in the
     *                          blank: "I was trying to __________ but it didn't work."
     */
    public CatchingAction(String actionDescription) {
        _whatAmIDoing = actionDescription;
    }

    /**
     * This is implemented to wrap any real code with some generic error handling.
     */
    public void actionPerformed(ActionEvent e) {
        try {
            internalActionPerformed(e);
        } catch (Throwable t) {
            GUIUtil.handleError(_whatAmIDoing, t);
        }
    }

    protected abstract void internalActionPerformed(ActionEvent e) throws Exception;
}
