package jade.tools.rma;

import jade.gui.AgentTree;

/**
   
   @author Tiziana Trucco - CSELT S.p.A.
   @version $Date: 2002-12-13 12:40:04 +0100 (ven, 13 dic 2002) $ $Revision: 3524 $
 */
class ManageMTPsAction extends FixedAction {

    private MainWindow myWnd;

    public ManageMTPsAction(MainWindow mWnd, ActionProcessor actPro) {
        super("ManageMTPsIcon", "Manage Installed MTPs", actPro);
        myWnd = mWnd;
    }

    public void doAction() {
        myWnd.showManageMTPsDialog();
    }
}
