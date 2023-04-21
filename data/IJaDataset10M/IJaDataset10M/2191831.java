package ostf.gui.action.testplan;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import ostf.gui.frame.AdminConsole;
import ostf.gui.frame.TestPlanPanel;

public class MoveUpPlanNodeAction extends AbstractAction {

    private static final long serialVersionUID = -5947051371172979811L;

    public void actionPerformed(ActionEvent e) {
        TestPlanPanel panel = AdminConsole.getInstance().getCurrentTestPlanPanel();
        if (panel != null) {
            panel.getXmlTree().moveUpSelectedElement();
        }
    }

    public MoveUpPlanNodeAction() {
        super("moveup-node");
    }
}
