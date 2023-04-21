package org.aiotrade.platform.core.ui.netbeans.actions;

import org.openide.cookies.EditCookie;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CookieAction;

/**
 *  
 * @author Moshe Sayag
 */
public final class UseDrawingAction extends CookieAction {

    protected void performAction(Node[] activatedNodes) {
        EditCookie editCookie = (EditCookie) activatedNodes[0].getLookup().lookup(EditCookie.class);
        System.out.println("In UseDrawingAction");
    }

    protected int mode() {
        return CookieAction.MODE_EXACTLY_ONE;
    }

    public String getName() {
        return NbBundle.getMessage(UseDrawingAction.class, "CTL_UseDrawingAction");
    }

    protected Class[] cookieClasses() {
        return new Class[] { EditCookie.class };
    }

    @Override
    protected String iconResource() {
        return "org/aiotrade/platform/core/ui/netbeans/resources/drawingLine.gif";
    }

    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }
}
