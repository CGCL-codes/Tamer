package net.sourceforge.syncyoursecrets.gui.rcp.model;

import net.sourceforge.syncyoursecrets.xmlmapping.MappingElement;
import net.sourceforge.syncyoursecrets.xmlmapping.StringElement;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;

/**
 * The Class PWListLabelProvider retrieves the name of the selected
 * MappingElement and returns it.
 */
public class PWListLabelProvider implements ILabelProvider {

    public Image getImage(Object element) {
        return null;
    }

    public String getText(Object obj) {
        if (obj instanceof StringElement) {
            StringElement stringElement = (StringElement) obj;
            return stringElement.getElementName();
        }
        if (obj instanceof MappingElement) {
            MappingElement element = (MappingElement) obj;
            return element.getName();
        }
        return obj.toString();
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
