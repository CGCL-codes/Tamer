package eu.medeia.ui.internal.provider;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import eu.medeia.model.ESBKnowledgeRepository;
import eu.medeia.treemodel.TreeObject;
import eu.medeia.treemodel.TreeParent;

/**
 * The Class InstanceModelLabelProvider.
 */
public class InstanceModelLabelProvider extends LabelProvider {

    public String getText(Object obj) {
        if (obj instanceof TreeObject) {
            TreeObject node = ((TreeObject) obj);
            TreeObject metaNode = ESBKnowledgeRepository.getInstance().getTreeElementForID(node.getMetaID());
            return obj.toString() + " : " + (metaNode != null ? metaNode.getName() : "N/A");
        }
        return obj.toString();
    }

    public Image getImage(Object obj) {
        String imageKey = ISharedImages.IMG_OBJ_ELEMENT;
        if (obj instanceof TreeParent) {
            String iconID = ((TreeParent) obj).getIconID();
            if (iconID != null) {
                Image image = ESBKnowledgeRepository.getInstance().getIcon(iconID);
                if (image != null) return image;
            }
            imageKey = ISharedImages.IMG_OBJ_FOLDER;
        }
        return PlatformUI.getWorkbench().getSharedImages().getImage(imageKey);
    }
}
