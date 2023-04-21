package com.bluemarsh.jswat.nodes.stack;

import com.bluemarsh.jswat.core.context.ContextProvider;
import com.bluemarsh.jswat.core.context.DebuggingContext;
import com.bluemarsh.jswat.core.session.Session;
import com.bluemarsh.jswat.core.session.SessionProvider;
import com.sun.jdi.IncompatibleThreadStateException;
import org.openide.ErrorManager;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.NodeAction;

/**
 * Implements the action of setting the current frame.
 *
 * @author  Nathan Fiedler
 */
public class SetCurrentAction extends NodeAction {

    /** silence the compiler warnings */
    private static final long serialVersionUID = 1L;

    @Override
    protected boolean asynchronous() {
        return false;
    }

    @Override
    protected boolean enable(Node[] activatedNodes) {
        if (activatedNodes != null && activatedNodes.length == 1) {
            Session session = SessionProvider.getCurrentSession();
            if (session.isSuspended()) {
                DebuggingContext dc = ContextProvider.getContext(session);
                return dc.getThread() != null;
            }
        }
        return false;
    }

    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    public String getName() {
        return NbBundle.getMessage(SetCurrentAction.class, "LBL_StackView_SetCurrentFrameAction");
    }

    @Override
    protected void performAction(Node[] activatedNodes) {
        if (activatedNodes != null && activatedNodes.length == 1) {
            GetFrameCookie gfc = activatedNodes[0].getCookie(GetFrameCookie.class);
            if (gfc != null) {
                int frame = gfc.getFrameIndex();
                Session session = SessionProvider.getCurrentSession();
                DebuggingContext dc = ContextProvider.getContext(session);
                try {
                    dc.setFrame(frame);
                } catch (IncompatibleThreadStateException itse) {
                    ErrorManager.getDefault().notify(itse);
                }
            }
        }
    }
}
