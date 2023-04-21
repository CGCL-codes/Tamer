package org.wsmostudio.bpmo.ui.actions;

import org.eclipse.ui.actions.RetargetAction;
import org.wsmostudio.bpmo.ImagePool;

public class ExportImageRetargetAction extends RetargetAction {

    public ExportImageRetargetAction() {
        super(ExportImageAction.ID, "Export as Image");
        setToolTipText("Export BPMO diagram as image");
        setImageDescriptor(ImagePool.getImage(ImagePool.EXPORT_IMAGE_ICON));
        setDisabledImageDescriptor(ImagePool.getImage(ImagePool.EXPORT_IMAGE_ICON_DISABLED));
    }
}
