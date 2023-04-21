package org.cumt.actions;

import javax.swing.Action;
import javax.swing.KeyStroke;
import org.cumt.Messages;
import org.cumt.workbench.UIResources;
import ar.com.da.swing.actions.InvokerAction;

public class CancelAction extends InvokerAction {

    private static final long serialVersionUID = 7074569405812232172L;

    public CancelAction() {
        putValue(Action.SMALL_ICON, UIResources.getIcon("/org/cumt/misc/cancel.png"));
        putValue(Action.NAME, Messages.get("actions.cancel"));
        putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("ESCAPE"));
        putValue(Action.MNEMONIC_KEY, Messages.getMnemonic("actions.cancel.mnemonic"));
    }
}
