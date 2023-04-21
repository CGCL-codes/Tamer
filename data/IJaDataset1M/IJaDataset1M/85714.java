package org.yawlfoundation.yawl.editor.actions.net;

import org.yawlfoundation.yawl.editor.net.NetGraph;
import org.yawlfoundation.yawl.editor.specification.SpecificationModel;
import org.yawlfoundation.yawl.editor.specification.SpecificationUndoManager;
import org.yawlfoundation.yawl.editor.swing.TooltipTogglingWidget;
import org.yawlfoundation.yawl.editor.swing.YAWLEditorDesktop;
import org.yawlfoundation.yawl.editor.swing.menu.MenuUtilities;
import javax.swing.*;
import java.awt.event.ActionEvent;

public class RemoveNetAction extends YAWLSelectedNetAction implements TooltipTogglingWidget {

    /**
   * 
   */
    private static final long serialVersionUID = 1L;

    {
        putValue(Action.SHORT_DESCRIPTION, getDisabledTooltipText());
        putValue(Action.NAME, "Remove Net");
        putValue(Action.LONG_DESCRIPTION, "Remove the selected net ");
        putValue(Action.SMALL_ICON, getPNGIcon("application_delete"));
        putValue(Action.MNEMONIC_KEY, new Integer(java.awt.event.KeyEvent.VK_R));
        putValue(Action.ACCELERATOR_KEY, MenuUtilities.getAcceleratorKeyStroke("R"));
    }

    public void actionPerformed(ActionEvent event) {
        YAWLEditorDesktop.getInstance().removeActiveNet();
        SpecificationUndoManager.getInstance().setDirty(true);
    }

    public void receiveSpecificationModelNotification(SpecificationModel.State state) {
        if (!(state == SpecificationModel.State.SOME_NET_SELECTED)) {
            super.receiveSpecificationModelNotification(state);
        } else {
            NetGraph graph = YAWLEditorDesktop.getInstance().getSelectedGraph();
            setEnabled((graph != null) && (!graph.getNetModel().isStartingNet()));
        }
    }

    public String getEnabledTooltipText() {
        return " Remove the selected net ";
    }

    public String getDisabledTooltipText() {
        return " You must have a net (other than the starting net)" + " selected to remove it ";
    }
}
