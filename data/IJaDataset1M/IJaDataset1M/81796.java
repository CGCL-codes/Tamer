package org.web3d.x3d.options;

import java.awt.Dialog;
import javax.swing.JButton;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CookieAction;
import org.web3d.x3d.X3DDataObject;

@ActionID(id = "org.web3d.x3d.options.OptionsMiscellaneousX3dPanelAction", category = "Tools")
@ActionRegistration(displayName = "#CTL_OptionsMiscellaneousX3dPanel")
@ActionReferences(value = { @ActionReference(path = "Menu/X3D", position = 700), @ActionReference(path = "Editors/model/x3d+xml/Popup", position = 900) })
public final class OptionsMiscellaneousX3dPanelAction extends CookieAction {

    @Override
    protected void performAction(Node[] activatedNodes) {
        OptionsMiscellaneousX3dPanel optionsPanel = new OptionsMiscellaneousX3dPanel();
        JButton acceptButton = new JButton(NbBundle.getMessage(getClass(), "MSG_Accept"));
        JButton cancelButton = new JButton(NbBundle.getMessage(getClass(), "MSG_Discard"));
        acceptButton.setToolTipText(NbBundle.getMessage(getClass(), "TIP_Accept"));
        cancelButton.setToolTipText(NbBundle.getMessage(getClass(), "TIP_Discard"));
        DialogDescriptor descriptor = new DialogDescriptor(optionsPanel, NbBundle.getMessage(getClass(), "OptionsMiscellaneousX3dPanelDialogTitle"), true, new Object[] { acceptButton, cancelButton }, acceptButton, DialogDescriptor.DEFAULT_ALIGN, HelpCtx.DEFAULT_HELP, null);
        Dialog dialog = DialogDisplayer.getDefault().createDialog(descriptor);
        dialog.setResizable(true);
        dialog.pack();
        dialog.setVisible(true);
        if (descriptor.getValue() == acceptButton) optionsPanel.store();
        return;
    }

    @Override
    protected int mode() {
        return CookieAction.MODE_EXACTLY_ONE;
    }

    @Override
    public String getName() {
        return NbBundle.getMessage(OptionsMiscellaneousX3dPanelAction.class, "CTL_OptionsMiscellaneousX3dPanel");
    }

    @Override
    protected Class[] cookieClasses() {
        return new Class[] { X3DDataObject.class };
    }

    @Override
    protected void initialize() {
        super.initialize();
        putValue("noIconInMenu", Boolean.TRUE);
    }

    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }
}
