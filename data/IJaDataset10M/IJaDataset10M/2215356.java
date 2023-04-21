package org.medbeans.modules.search;

import org.openide.nodes.Node;
import org.openide.util.actions.NodeAction;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;

/**
 * Action which removes <code>FoundNode</code> from search result.
 *
 * @author  Peter Zavadsky
 * @see ResultModel.FoundNode
 */
public class RemoveFromSearchAction extends NodeAction {

    /** Generated serial version UID. */
    static final long serialVersionUID = -7200719415283638136L;

    /** Gets help context for the action. Implements superclass abstract method.
     * @return the help context for this action */
    public HelpCtx getHelpCtx() {
        return new HelpCtx(RemoveFromSearchAction.class);
    }

    /** Gets display name for action. Implements superclass abstract method.
     * @return the name of the action */
    public String getName() {
        return NbBundle.getBundle(RemoveFromSearchAction.class).getString("TEXT_LABEL_RemoveFromSearchActionName");
    }

    /** Enables action based on activated nodes. Implements superclass abstract method.
     * @return <code>true</code> if all activated nodes are instance of <code>FoundNode</code>
     * or <code>false</code> otherwise */
    protected boolean enable(Node[] activatedNodes) {
        if (activatedNodes == null || activatedNodes.length == 0) return false;
        for (int i = 0; i < activatedNodes.length; i++) {
            if (!(activatedNodes[i] instanceof ResultModel.FoundNode)) return false;
        }
        return true;
    }

    /** Performs the action based on the currently activated nodes. Implements superclass abstract method.
     * @param activatedNodes current activated nodes, may be empty but not <code>null</code> */
    protected void performAction(Node[] activatedNodes) {
        if (activatedNodes == null || activatedNodes.length == 0) return;
        for (int i = 0; i < activatedNodes.length; i++) {
            if (activatedNodes[i] instanceof ResultModel.FoundNode) ((ResultModel.FoundNode) activatedNodes[i]).removeFromSearch();
        }
    }
}
