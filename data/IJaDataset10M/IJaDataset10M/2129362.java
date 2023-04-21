package org.escapek.client.ui.cmdb.providers;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;
import org.escapek.core.dto.cmdb.CIDTO;

public class ContactLabelProvider extends CIClassLabelProvider {

    public ContactLabelProvider() {
        super();
    }

    public Image getImage(Object element) {
        if (element instanceof CIDTO) return null; else return super.getImage(element);
    }

    public String getText(Object element) {
        return super.getText(element);
    }

    public void removeListener(ILabelProviderListener listener) {
    }

    public String getDescription(Object anElement) {
        return super.getDescription(anElement);
    }
}
