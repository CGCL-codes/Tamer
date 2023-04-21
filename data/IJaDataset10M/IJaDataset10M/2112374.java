package org.openconcerto.erp.action;

import org.openconcerto.erp.core.common.ui.PanelFrame;
import org.openconcerto.task.ui.UserRightsPrefPanel;
import javax.swing.Action;
import javax.swing.JFrame;

public class TaskAdminAction extends CreateFrameAbstractAction {

    public TaskAdminAction() {
        super("Gestion des autorisations concernant les tâches");
    }

    @Override
    public JFrame createFrame() {
        return new PanelFrame(new UserRightsPrefPanel(), (String) getValue(Action.NAME));
    }
}
