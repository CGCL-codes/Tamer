package org.mxeclipse.utils;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

/**
 * Label provider for the MxObjectProperyTable
 * 
 * @see org.eclipse.jface.viewers.LabelProvider
 */
public class MxEclipseLabelProvider extends LabelProvider implements ITableLabelProvider {

    /**
     * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang.Object, int)
     */
    public String getColumnText(Object element, int columnIndex) {
        return getText(element);
    }

    /**
     * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java.lang.Object, int)
     */
    public Image getColumnImage(Object element, int columnIndex) {
        Image image = null;
        try {
            image = (element == null) ? MxEclipseUtils.getImageRegistry().get("All") : MxEclipseUtils.getImageRegistry().get(element.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return image;
    }
}
