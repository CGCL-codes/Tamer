package ostf.gui.action.testplan;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import ostf.gui.frame.AdminConsole;
import ostf.gui.frame.TestPlanPanel;

public class PastePlanNodeAction extends AbstractAction {

    private static final long serialVersionUID = 2306815407194726991L;

    public void actionPerformed(ActionEvent e) {
        TestPlanPanel panel = AdminConsole.getInstance().getCurrentTestPlanPanel();
        if (panel != null) {
            panel.getCutAndPaste().paste();
        }
    }

    public PastePlanNodeAction() {
        super("paste-testplan");
    }
}
