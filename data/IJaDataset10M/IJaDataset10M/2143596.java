package org.fudaa.fudaa.crue.otfa.action;

import org.fudaa.fudaa.crue.common.action.EditNodeAction;
import org.fudaa.fudaa.crue.otfa.node.OtfaCampagneLineNode;
import org.fudaa.fudaa.crue.otfa.service.OtfaService;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;

/**
 *
 * @author Christophe CANEL (Genesis)
 */
public class UseRelativePathNodeAction extends EditNodeAction {

    OtfaService otfaService = Lookup.getDefault().lookup(OtfaService.class);

    public UseRelativePathNodeAction() {
        super(NbBundle.getMessage(UseRelativePathNodeAction.class, "UseRelativePathNodeAction.ActionName"));
    }

    @Override
    protected void performAction(Node[] nodes) {
        for (Node node : nodes) {
            final OtfaCampagneLineNode line = (OtfaCampagneLineNode) node;
            line.useRelativePath();
        }
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }

    @Override
    protected boolean isEnable(Node[] nodes) {
        if (nodes.length == 0) {
            return false;
        }
        return true;
    }

    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }
}
