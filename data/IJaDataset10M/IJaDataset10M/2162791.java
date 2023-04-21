package fr.univartois.cril.xtext2.alloyplugin.editor;

import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;
import fr.univartois.cril.xtext2.alloyplugin.api.ALSImageRegistry;
import fr.univartois.cril.xtext2.alloyplugin.api.IALSTreeDecorated;
import fr.univartois.cril.xtext2.alloyplugin.api.Iconable;
import fr.univartois.cril.xtext2.alloyplugin.core.OverlayImageIcon;

public class AlloyTreeLabelProvider implements ILabelProvider {

    public Image getImage(Object element) {
        if (element instanceof Iconable) {
            if (element instanceof IALSTreeDecorated) {
                if (((IALSTreeDecorated) element).isPrivate()) {
                    OverlayImageIcon overlayIcon = new OverlayImageIcon(((IALSTreeDecorated) element).getIcon(), ALSImageRegistry.getImage(ALSImageRegistry.PRIVATE_ICON_ID));
                    return overlayIcon.getImage();
                }
            }
            return ((Iconable) element).getIcon();
        }
        return null;
    }

    public String getText(Object element) {
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

    public static IBaseLabelProvider getDefault() {
        return null;
    }
}
