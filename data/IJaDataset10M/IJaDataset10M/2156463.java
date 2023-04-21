package de.mpiwg.vspace.images.ui.controller.provider;

import java.text.DateFormat;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import de.mpiwg.vspace.images.core.ImageImpl;
import de.mpiwg.vspace.images.service.Image;
import de.mpiwg.vspace.images.ui.controller.items.PropertyTreeItem;
import de.mpiwg.vspace.images.ui.controller.items.TopLevelTreeItem;

public class PlainLabelProvider implements ILabelProvider {

    public org.eclipse.swt.graphics.Image getImage(Object element) {
        if (element instanceof TopLevelTreeItem) return ((TopLevelTreeItem) element).getImage();
        return null;
    }

    public String getText(Object element) {
        if (element instanceof ImageImpl) {
            return ((Image) element).getTitle() + " [added " + DateFormat.getInstance().format(((Image) element).getEntryDate()) + "]";
        }
        if (element instanceof PropertyTreeItem) {
            return ((PropertyTreeItem) element).getPropName() + ": " + ((PropertyTreeItem) element).getPropValue();
        }
        if (element instanceof TopLevelTreeItem) return ((TopLevelTreeItem) element).getLabelText();
        return element.toString();
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
