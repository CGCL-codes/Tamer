package net.sf.poormans.gui.treeview;

import net.sf.poormans.configuration.resource.ImageHolder;
import net.sf.poormans.model.InstanceUtil;
import net.sf.poormans.model.domain.pojo.APoormansObject;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

/**
 * LabelProvider for the site treeview.
 *
 * @version $Id: TreeViewLabelProvider.java 2092 2011-05-29 13:42:38Z th-schwarz $
 * @author <a href="mailto:th-schwarz@users.sourceforge.net">Thilo Schwarz</a>
 */
public class TreeViewLabelProvider extends LabelProvider {

    @Override
    public Image getImage(Object element) {
        if (InstanceUtil.isPoormansObject(element)) return ImageHolder.getImage((APoormansObject<?>) element);
        return super.getImage(element);
    }

    @Override
    public String getText(Object element) {
        return ((APoormansObject<?>) element).getDecorationString();
    }
}
