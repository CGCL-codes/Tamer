package org.wsmostudio.bpmo.ui.actions;

import org.eclipse.ui.actions.RetargetAction;
import org.wsmostudio.bpmo.ImagePool;

public class LayoutRetargetAction extends RetargetAction {

    public LayoutRetargetAction() {
        super(LayoutAction.ID, "Reset Layout");
        setToolTipText("Reset selection's layout");
        setImageDescriptor(ImagePool.getImage(ImagePool.LAYOUT_ICON));
        setDisabledImageDescriptor(ImagePool.getImage(ImagePool.LAYOUT_ICON_DISABLED));
    }
}
