package org.netbeans.modules.flexbean.platform.api.ui.ide.actions;

import java.awt.Dialog;
import javax.swing.JPanel;
import org.netbeans.modules.flexbean.platform.module.ui.manager.PlatformManagerUI;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CallableSystemAction;

/**
 *
 * @author arnaud
 */
public final class PlatformAction extends CallableSystemAction {

    @Override
    public void performAction() {
        final JPanel pane = new PlatformManagerUI();
        final DialogDescriptor dialogDescriptor = new DialogDescriptor(pane, getName(), true, new Object[] { DialogDescriptor.CLOSED_OPTION }, DialogDescriptor.CLOSED_OPTION, DialogDescriptor.BOTTOM_ALIGN, null, null);
        Dialog dlg = null;
        try {
            dlg = DialogDisplayer.getDefault().createDialog(dialogDescriptor);
            dlg.setVisible(true);
        } finally {
            if (dlg != null) dlg.dispose();
        }
    }

    @Override
    public String getName() {
        return NbBundle.getMessage(PlatformAction.class, "ide.menu.title.flexplatformsdk");
    }

    @Override
    public HelpCtx getHelpCtx() {
        return null;
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }
}
