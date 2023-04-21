package org.tuba.spatschorke.diploma.repository.adoxx;

import java.net.URL;
import org.eclipse.jface.resource.ImageDescriptor;

public abstract class Utility {

    public static ImageDescriptor getImageDescriptor(String name) {
        ImageDescriptor descriptor = null;
        URL imageURL = Activator.getDefault().getBundle().getResource(name);
        descriptor = ImageDescriptor.createFromURL(imageURL);
        return descriptor;
    }
}
