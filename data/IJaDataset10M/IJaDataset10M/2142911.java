package jade.tools.rma;

import java.awt.Frame;
import jade.gui.AgentTree;
import jade.core.AID;

/**
   
   @author Tiziana Trucco - CSELT S.p.A.
   @version $Date: 2002-12-13 14:34:24 +0100 (ven, 13 dic 2002) $ $Revision: 3529 $
 */
class MoveAgentAction extends AgentAction {

    private rma myRMA;

    private Frame mainWnd;

    MoveAgentAction(rma anRMA, ActionProcessor act, Frame f) {
        super("MoveAgentActionIcon", "Migrate Agent", act);
        myRMA = anRMA;
        mainWnd = f;
    }

    public void doAction(AgentTree.AgentNode node) {
        String agentName = node.getName();
        int result = MoveDialog.showMoveDialog(agentName, mainWnd, false);
        if (result == MoveDialog.OK_BUTTON) {
            String container = MoveDialog.getContainer();
            if (container.trim().length() > 0) {
                AID agentAid = new AID();
                agentAid.setName(agentName);
                myRMA.moveAgent(agentAid, container);
            }
        }
    }
}
