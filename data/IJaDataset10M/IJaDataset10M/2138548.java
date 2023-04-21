package com.bluemarsh.jswat.ui.actions;

import com.bluemarsh.jswat.core.session.Session;
import com.bluemarsh.jswat.core.session.SessionManager;
import com.bluemarsh.jswat.core.session.SessionProvider;
import com.bluemarsh.jswat.core.stepping.Stepper;
import com.bluemarsh.jswat.core.stepping.SteppingException;
import com.bluemarsh.jswat.core.stepping.SteppingProvider;
import com.bluemarsh.jswat.ui.ActionEnabler;
import org.openide.awt.StatusDisplayer;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CallableSystemAction;

/**
 * Action that performs the finish operation, leaving the current method.
 *
 * @author Nathan Fiedler
 */
public class StepOutAction extends CallableSystemAction {

    /** silence the compiler warnings */
    private static final long serialVersionUID = 1L;

    /**
     * Creates a new instance of StepOutAction.
     */
    public StepOutAction() {
        ActionEnabler ae = ActionEnabler.getDefault();
        ae.registerSuspendedAction(this);
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }

    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    public String getName() {
        return NbBundle.getMessage(getClass(), "LBL_StepOutAction");
    }

    @Override
    protected String iconResource() {
        return NbBundle.getMessage(getClass(), "IMG_StepOutAction");
    }

    @Override
    public void performAction() {
        SessionManager sm = SessionProvider.getSessionManager();
        Session session = sm.getCurrent();
        Stepper st = SteppingProvider.getStepper(session);
        try {
            st.stepOut();
        } catch (SteppingException se) {
            StatusDisplayer.getDefault().setStatusText(se.getMessage());
        }
    }
}
