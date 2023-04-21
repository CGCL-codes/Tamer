package com.bluemarsh.jswat.action;

import com.bluemarsh.jswat.ContextManager;
import com.bluemarsh.jswat.Session;
import com.bluemarsh.jswat.ui.UIAdapter;
import com.bluemarsh.jswat.util.Stepping;
import com.sun.jdi.ThreadReference;
import com.sun.jdi.request.StepRequest;
import java.awt.event.ActionEvent;

/**
 * Implements the step instruction action.
 *
 * @author  Nathan Fiedler
 */
public class StepiAction extends JSwatAction implements SessionAction {

    /** silence the compiler warnings */
    private static final long serialVersionUID = 1L;

    /**
     * Creates a new StepiAction object with the default action
     * command string of "stepi".
     */
    public StepiAction() {
        super("stepi");
    }

    /**
     * Performs the stepi action.
     *
     * @param  event  action event
     */
    public void actionPerformed(ActionEvent event) {
        Session session = getSession(event);
        if (session.isActive()) {
            ContextManager contextManager = (ContextManager) session.getManager(ContextManager.class);
            ThreadReference current = contextManager.getCurrentThread();
            if (current == null) {
                session.getUIAdapter().showMessage(UIAdapter.MESSAGE_ERROR, Bundle.getString("noCurrentThread"));
            } else {
                if (Stepping.step(session.getVM(), current, StepRequest.STEP_MIN, StepRequest.STEP_INTO, false, session.getProperty("excludes"))) {
                    session.resumeVM(this, true, true);
                }
            }
        }
    }

    /**
     * Returns true to indicate that this action should be disabled
     * when the debuggee is resumed.
     *
     * @return  true to disable, false to leave as-is.
     */
    public boolean disableOnResume() {
        return true;
    }

    /**
     * Returns true to indicate that this action should be disabled
     * when the debuggee is suspended.
     *
     * @return  true to disable, false to leave as-is.
     */
    public boolean disableOnSuspend() {
        return false;
    }

    /**
     * Returns true to indicate that this action should be disabled
     * while the session is active, and enabled when the session
     * is not active. This is the opposite of how SessionActions
     * normally behave.
     *
     * @return  true to disable when active, false to enable.
     */
    public boolean disableWhenActive() {
        return false;
    }
}
