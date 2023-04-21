package org.bellard.qemoon.provider;

import org.bellard.qemoon.model.VM;
import org.bellard.qemoon.model.VMConfiguration;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

/**
 * @author Eric Bellard - eric.bellard@gmail.com
 * 
 */
public class VMNavigationViewLabelProvider extends LabelProvider {

    public String getText(Object obj) {
        if (obj == null) return "";
        if (obj instanceof VM) {
            return ((VM) obj).getName();
        }
        if (obj instanceof VMConfiguration) {
            return "configuration";
        }
        return obj.toString();
    }

    public Image getImage(Object obj) {
        String imageKey = ISharedImages.IMG_OBJ_ELEMENT;
        if (obj instanceof VM) imageKey = ISharedImages.IMG_OBJ_FOLDER;
        return PlatformUI.getWorkbench().getSharedImages().getImage(imageKey);
    }
}
