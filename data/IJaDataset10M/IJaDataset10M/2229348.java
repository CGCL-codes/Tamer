package es.ulpgc.dis.heuristicide.rcp.actions;

import java.net.URL;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;

public class Images {

    public static ImageDescriptor createImageDescriptorFor(String id) {
        URL url = Platform.getBundle("Heuriskein").getEntry(id);
        return ImageDescriptor.createFromURL(url);
    }
}
