package net.sf.poormans.gui.workspace;

import net.sf.poormans.configuration.resource.IconHolder;
import net.sf.poormans.model.domain.IPersistentPojo;
import net.sf.poormans.model.domain.InstanceUtil;
import net.sf.poormans.model.domain.PojoInfo;
import net.sf.poormans.model.domain.pojo.Page;
import org.eclipse.jface.viewers.LabelProvider;

/**
 * LabelProvider for the site treeview.
 *
 * @version $Id: WorkspaceSiteTreeLabelProvider.java 1141 2007-11-26 15:57:51Z th-schwarz $
 * @author <a href="mailto:th-schwarz@users.sourceforge.net">Thilo Schwarz</a>
 */
public class WorkspaceSiteTreeLabelProvider extends LabelProvider {

    @Override
    public org.eclipse.swt.graphics.Image getImage(Object element) {
        if (InstanceUtil.isSite(element)) return IconHolder.SITE; else if (InstanceUtil.isLevel(element)) return IconHolder.FOLDER; else if (InstanceUtil.isImage(element)) return IconHolder.IMAGE; else if (InstanceUtil.isPage(element)) {
            Page page = (Page) element;
            if (PojoInfo.isGallery(page)) return IconHolder.GALLERY;
            return IconHolder.PAGE;
        }
        return super.getImage(element);
    }

    @Override
    public String getText(Object element) {
        return ((IPersistentPojo) element).getDecorationString();
    }
}
