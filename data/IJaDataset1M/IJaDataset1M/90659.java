package org.rubypeople.rdt.internal.ui.viewsupport;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.resources.IStorage;
import org.eclipse.swt.graphics.Image;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.ui.IEditorRegistry;
import org.eclipse.ui.IFileEditorMapping;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

/**
 * Standard label provider for IStorage objects.
 * Use this class when you want to present IStorage objects in a viewer.
 */
public class StorageLabelProvider extends LabelProvider {

    private IEditorRegistry fEditorRegistry = null;

    private Map fJarImageMap = new HashMap(10);

    private Image fDefaultImage;

    private IEditorRegistry getEditorRegistry() {
        if (fEditorRegistry == null) fEditorRegistry = PlatformUI.getWorkbench().getEditorRegistry();
        return fEditorRegistry;
    }

    public Image getImage(Object element) {
        if (element instanceof IStorage) return getImageForJarEntry((IStorage) element);
        return super.getImage(element);
    }

    public String getText(Object element) {
        if (element instanceof IStorage) return ((IStorage) element).getName();
        return super.getText(element);
    }

    public void dispose() {
        if (fJarImageMap != null) {
            Iterator each = fJarImageMap.values().iterator();
            while (each.hasNext()) {
                Image image = (Image) each.next();
                image.dispose();
            }
            fJarImageMap = null;
        }
        fDefaultImage = null;
    }

    private Image getImageForJarEntry(IStorage element) {
        if (fJarImageMap == null) return getDefaultImage();
        if (element == null || element.getName() == null) return getDefaultImage();
        String name = element.getName();
        Image image = (Image) fJarImageMap.get(name);
        if (image != null) return image;
        IFileEditorMapping[] mappings = getEditorRegistry().getFileEditorMappings();
        int i = 0;
        while (i < mappings.length) {
            if (mappings[i].getLabel().equals(name)) break;
            i++;
        }
        String key = name;
        if (i == mappings.length) {
            IPath path = element.getFullPath();
            if (path == null) return getDefaultImage();
            key = path.getFileExtension();
            if (key == null) return getDefaultImage();
            image = (Image) fJarImageMap.get(key);
            if (image != null) return image;
        }
        ImageDescriptor desc = getEditorRegistry().getImageDescriptor(name);
        image = desc.createImage();
        fJarImageMap.put(key, image);
        return image;
    }

    private Image getDefaultImage() {
        if (fDefaultImage == null) fDefaultImage = PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FILE);
        return fDefaultImage;
    }
}
