package net.sourceforge.openconferencer.client.view;

import net.sourceforge.openconferencer.client.contact.ContactInformation;
import net.sourceforge.openconferencer.client.contact.ContactProviderInformation;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

/**
 * @author aleksandar
 */
public class ContactLabelProvider extends LabelProvider {

    @Override
    public Image getImage(Object element) {
        if (element instanceof ContactProviderInformation) {
            ContactProviderInformation info = (ContactProviderInformation) element;
            return info.getSmallIcon();
        }
        if (element instanceof ContactInformation) {
            ContactInformation info = (ContactInformation) element;
            Image img = info.getProvider().getContactProvider().getContactDisplay().getContactIcon(info);
            img = img != null ? img : info.getProvider().getIcon();
            return img;
        }
        if (element instanceof ContactErrorInformation) {
            ISharedImages iis = PlatformUI.getWorkbench().getSharedImages();
            return iis.getImage(ISharedImages.IMG_OBJS_ERROR_TSK);
        }
        return super.getImage(element);
    }

    @Override
    public String getText(Object element) {
        if (element instanceof ContactProviderInformation) {
            ContactProviderInformation info = (ContactProviderInformation) element;
            return info.getName();
        }
        if (element instanceof ContactInformation) {
            ContactInformation info = (ContactInformation) element;
            String label = info.getProvider().getContactProvider().getContactDisplay().getContactLabel(info);
            return label != null ? label : info.getName();
        }
        if (element instanceof ContactErrorInformation) {
            ContactErrorInformation cei = (ContactErrorInformation) element;
            return cei.getMessage();
        }
        return super.getText(element);
    }
}
