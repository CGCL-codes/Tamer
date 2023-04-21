package org.semtinel.core.overview;

import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.semtinel.core.data.api.CoreManager;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.Utilities;
import org.openide.util.actions.CallableSystemAction;

public final class CreateAnnotationSetGroupAction extends CallableSystemAction {

    public CreateAnnotationSetGroupAction() {
        this(Utilities.actionsGlobalContext());
    }

    public CreateAnnotationSetGroupAction(Lookup lookup) {
    }

    public void performAction() {
        String name = JOptionPane.showInputDialog("Specify the name");
        CoreManager cm = Lookup.getDefault().lookup(CoreManager.class);
        cm.createAnnotationSetGroup(name);
    }

    public String getName() {
        return NbBundle.getMessage(CreateAnnotationSetGroupAction.class, "CTL_CreateAnnotationSetGroupAction");
    }

    @Override
    protected void initialize() {
        super.initialize();
        putValue("noIconInMenu", Boolean.TRUE);
    }

    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }
}
