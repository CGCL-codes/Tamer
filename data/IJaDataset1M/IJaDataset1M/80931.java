package ostf.gui.action.project;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import ostf.gui.frame.AdminConsole;

public class CloseAllTestResultPanelAction extends AbstractAction {

    private static final long serialVersionUID = 2418667921563368163L;

    public void actionPerformed(ActionEvent e) {
        AdminConsole.getInstance().closeAllTestPanels();
    }

    public CloseAllTestResultPanelAction() {
        super("closeall-testresult");
    }
}
