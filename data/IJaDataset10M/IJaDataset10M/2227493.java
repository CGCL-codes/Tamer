package org.musicnotation.gef.actions;

import org.eclipse.ui.actions.RetargetAction;
import org.musicnotation.gef.Activator;

public class SharpRetargetAction extends RetargetAction {

    public SharpRetargetAction() {
        super(ChangeAlterationAction.SHARP, "Sharp");
        setToolTipText("Sharp");
        setImageDescriptor(Activator.getImageDescriptor("icons/actions/sharp.png"));
    }
}
