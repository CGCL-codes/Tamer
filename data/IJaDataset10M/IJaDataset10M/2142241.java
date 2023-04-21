package org.j2eespider.ide.outline;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;

public class OutlineLabelProvider implements ILabelProvider {

    public Image getImage(Object element) {
        return null;
    }

    public String getText(Object element) {
        return ((TreeNode) element).getName();
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
