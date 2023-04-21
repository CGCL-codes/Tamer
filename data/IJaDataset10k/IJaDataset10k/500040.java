package org.exist.eclipse.auto.internal.control;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.exist.eclipse.auto.internal.model.QueryEntity;

/**
 * This class provides the labels for the TableViewer on the automation form
 * page.
 * 
 * @author Markus Tanner
 */
public class AutoLabelProvider extends LabelProvider implements ITableLabelProvider {

    public String getColumnText(Object obj, int index) {
        if (obj instanceof QueryEntity) {
            QueryEntity entity = (QueryEntity) obj;
            return entity.getName();
        }
        return "---";
    }

    public Image getColumnImage(Object obj, int index) {
        if (obj instanceof QueryEntity) {
            return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FILE);
        }
        return null;
    }
}
