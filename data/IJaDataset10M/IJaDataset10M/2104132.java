package jade.tools.rma;

import java.awt.Dimension;
import javax.swing.*;
import jade.core.AID;
import jade.gui.AgentTree;
import jade.gui.AgentTreeModel;

/**   
   @author Francisco Regi, Andrea Soracchi - Universita` di Parma
   @version $Date: 2002-12-13 12:40:04 +0100 (ven, 13 dic 2002) $ $Revision: 3524 $
 */
class ChangeAgentOwnershipAction extends AgentAction {

    private rma myRMA;

    private MainWindow win;

    public ChangeAgentOwnershipAction(rma anRMA, ActionProcessor actPro, MainWindow win) {
        super("ChangeAgentOwnershipActionIcon", "Change owner", actPro);
        this.win = win;
        myRMA = anRMA;
    }

    public void doAction(AgentTree.AgentNode node) {
        PwdDialog pd = new PwdDialog();
        int result = JOptionPane.showConfirmDialog(null, pd, "Change agent ownership", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
        String owner = null;
        if (result == JOptionPane.OK_OPTION) owner = pd.getUserName() + ':' + new String(pd.getPassword());
        if (owner != null) {
            String toChange = node.getName();
            AID agentID = new AID();
            agentID.setName(toChange);
            myRMA.changeAgentOwnership(agentID, owner);
        }
    }
}
