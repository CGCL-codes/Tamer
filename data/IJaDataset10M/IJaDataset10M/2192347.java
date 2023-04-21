package org.escapek.client.ui.cmdb.providers;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.navigator.IDescriptionProvider;
import org.escapek.client.tools.repository.CITools;
import org.escapek.client.ui.SharedImages;
import org.escapek.core.dto.cmdb.CIDTO;
import org.escapek.core.dto.cmdb.LocationDTO;
import org.escapek.i18n.LocaleService;
import org.escapek.i18n.MessageService;

public class LocationLabelProvider implements ILabelProvider, IDescriptionProvider {

    private MessageService message;

    public LocationLabelProvider() {
        message = LocaleService.getMessageService(LocaleService.uiMessages);
    }

    public Image getImage(Object element) {
        if (element instanceof LocationDTO) return SharedImages.getImage(SharedImages.IMAGE_SITE);
        return null;
    }

    public String getText(Object element) {
        if (element == CITools.ROOT_SITE) return message.getString("siteSelection.rootSite.label");
        if (element instanceof LocationDTO) return ((LocationDTO) element).getDisplayName();
        return null;
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

    public String getDescription(Object element) {
        if (element instanceof CIDTO) return ((CIDTO) element).getDescription();
        return null;
    }
}
