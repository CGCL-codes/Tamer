package de.fraunhofer.isst.axbench.editors.properties;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;
import de.fraunhofer.isst.axbench.utilities.Constants;

/**
 * @brief this class realize the label provider for the editor tabbed property page.
 * @author skaegebein
 * @version 0.9.0
 * @since 0.9.0
 */
public class AXLEditorTabbedLabelProvider implements ILabelProvider {

    public Image getImage(Object element) {
        if (Constants.getInstance().getCurrentmultipage() != null) {
            return Constants.getInstance().getCurrentmultipage().getTitleImage();
        }
        return null;
    }

    public String getText(Object element) {
        if (Constants.getInstance().getCurrentmultipage() != null) {
            return Constants.getInstance().getCurrentmultipage().getTitle();
        }
        return "";
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
