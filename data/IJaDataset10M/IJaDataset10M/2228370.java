package com.wgo.precise.client.ui.view.util;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;
import com.wgo.precise.client.ui.model.facade.ILabelProviderItem;
import com.wgo.precise.client.ui.view.editor.properties.PropertyLinkDescriptor;

/**
 * @author petterei
 *
 * @version $Id: PropertyLinkDescriptorLabelProvider.java,v 1.1 2006-01-23 19:58:37 petterei Exp $
 */
public class PropertyLinkDescriptorLabelProvider implements ILabelProvider {

    public Image getImage(Object element) {
        if (element instanceof PropertyLinkDescriptor) {
            PropertyLinkDescriptor descriptor = (PropertyLinkDescriptor) element;
            return ((ILabelProviderItem) descriptor.getId()).getImage();
        }
        return null;
    }

    public String getText(Object element) {
        if (element instanceof PropertyLinkDescriptor) {
            PropertyLinkDescriptor descriptor = (PropertyLinkDescriptor) element;
            return ((ILabelProviderItem) descriptor.getId()).getSimpleName();
        }
        return null;
    }

    public void addListener(ILabelProviderListener listener) {
    }

    public void dispose() {
    }

    public boolean isLabelProperty(Object element, String property) {
        if (element instanceof PropertyLinkDescriptor) {
            return true;
        }
        return false;
    }

    public void removeListener(ILabelProviderListener listener) {
    }
}
