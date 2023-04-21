package com.wdev91.eclipse.copyright.viewers;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;
import com.wdev91.eclipse.copyright.model.Copyright;

public class CopyrightLabelProvider implements ILabelProvider {

    public Image getImage(Object element) {
        return null;
    }

    public String getText(Object element) {
        return element instanceof Copyright ? ((Copyright) element).getLabel() : element.toString();
    }

    public void addListener(ILabelProviderListener listener) {
    }

    public void dispose() {
    }

    public boolean isLabelProperty(Object element, String property) {
        return false;
    }

    public void removeListener(ILabelProviderListener listener) {
    }
}
